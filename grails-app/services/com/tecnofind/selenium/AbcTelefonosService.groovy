package com.tecnofind.selenium

import com.tecnofind.AccessRulesService
import org.openqa.selenium.*
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.remote.UselessFileDetector
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.net.URL
import java.net.URLEncoder
import java.net.URI
import com.tecnofind.Auxiliar
import com.tecnofind.sitio.Sitio
import com.tecnofind.oficina.Oficina
import com.tecnofind.credencial.Credencial
import com.tecnofind.User
import com.tecnofind.domicilio.Poblacion
import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import com.tecnofind.domicilio.Provincia
import com.tecnofind.persona.Persona
import com.tecnofind.Estado
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.persona.Telefono
import com.tecnofind.Auxiliar
import com.tecnofind.domicilio.DomicilioCommand
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.busqueda.BuscarCommand
import org.joda.time.LocalDateTime

class AbcTelefonosService extends SeleniumService{

    def domicilioService
    
    private Sitio getSitio() {
        return Sitio.findByNombre("Abcteléfonos")
    }

    def busquedaAbcTelefonos(BuscarCommand personaBuscarCommand = null, Persona buscarRelacionada = null){
        def driver
        def resultado
        try {
            User userActual = accessRulesService.getCurrentUser()
            assert userActual.oficina != null:"El usuario actual no pertenece a ninguna oficinafinerror"
            driver = inicializarDriver()            
            
            resultado = buscarAbctelefonos(driver, personaBuscarCommand, buscarRelacionada)
            if (!resultado?.personaBuscada && personaBuscarCommand && tieneSinonimos(personaBuscarCommand)) {
                BuscarCommand buscarCommandTraducido = traducirNombres(personaBuscarCommand)
                resultado = buscarAbctelefonos(driver, buscarCommandTraducido)
            }
        } 
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_abctelefonos" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultado.personaBuscada = null
            mensajeError = "AbcTeléfonos: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            resultado.personaBuscada = null
            mensajeError = "AbcTeléfonos: " + e.message.split("finerror")[0]
        }
        finally {
            cerrar(driver)
            return [resultado: resultado?.personaBuscada, mensajeError: resultado?.mensajeError]
        }
    }

    def buscarAbctelefonos(WebDriver driver, BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        PersonaCommand personaBuscada = new PersonaCommand()
        Sitio sitio = getSitio()
        String mensajeError = null
        personaBuscada.oficinaId = accessRulesService.getCurrentUser().oficina.id
        personaBuscada.sitioId = sitio.id
        try {
            def datosBuscar
            if(buscarRelacionada) {
                datosBuscar = buscarRelacionada
            }
            else {
                datosBuscar = buscarCommand
                String mensajePrint = "Intentando buscar con los parámetros:\n"  
                Auxiliar.separador (mensajePrint + buscarCommand.parametros)
            }
            String nombreBusqueda = datosBuscar.apellidoPadre?.capitalize()
            if (datosBuscar.apellidoMadre) {
                nombreBusqueda += " ${datosBuscar.apellidoMadre.capitalize()}"
            }
            if (datosBuscar.primerNombre) {
                nombreBusqueda += " ${datosBuscar.primerNombre.capitalize()}"
            }
            if (datosBuscar.segundoNombre) {
                nombreBusqueda += " ${datosBuscar.segundoNombre.capitalize()}"
            }

            def hayde = contieneDE(nombreBusqueda)
            def url = "${sitio.url.replace("?country=espana", "")}search?q=${URLEncoder.encode(nombreBusqueda, "ISO-8859-1")}&l=&t=persona&country=espana"
            driver.get(url)
            def actions = new Actions(driver)
            Boolean sonResultadosSimilares = false
            try{
                List<WebElement> similares = driver.findElements(By.xpath("//*[contains(text(), 'similares')]"))
                if (similares.size() > 0){
                    sonResultadosSimilares = true
                }
            }
            catch (Exception e){
                Auxiliar.printearError(e)
                sonResultadosSimilares = false
            }
            assert !sonResultadosSimilares: "No se encontraron resultados para esta búsquedafinerror"
            Boolean resultadosEnEspaña = false
            try{
                if(buscar(driver, '//h1[@class="title"]').getText().contains("España")){
                    resultadosEnEspaña = true
                }
            }
            catch (e){
                resultadosEnEspaña = false
            }
            assert resultadosEnEspaña: "No se han encontrado resultados en Españafinerror"

            def nombreParaResultado = buscar(driver, "//*[@id='mainContent']/div[1]/div/span[1]/a/span").getText()
            int telefonosGuardados = 0
            for(int pagina = 0; pagina < 3; pagina++){
                if (telefonosGuardados >= 25) {
                    break
                }
                actions.moveByOffset(0, 0).click().perform()
                if (pagina == 1){
                    try{
                        esperarElemento(driver, "//a[text()='Anterior']")
                    } catch(e){
                        break
                    }
                    url = driver.getCurrentUrl()
                }
                List<WebElement> resultados = driver.findElements(By.xpath("//span[@class='name']/a/span[@itemprop='name' and text()='${nombreParaResultado}']"))
                for (int i = 0; i < resultados.size(); i++) {
                    DomicilioCommand domicilioCommand = new DomicilioCommand()
                    def provincia
                    def poblacion
                    def calle
                    def numero
                    def nombreCompleto = quitarArticulos(buscar(driver, "(//span[@itemprop='name'])[${i+1}]").getText(), hayde)
                    personaBuscada = obtenerNombreyApellidos(personaBuscada, nombreCompleto)

                    String telefonoCompleto
                    try {
                        telefonoCompleto = buscar(driver, "(//span[contains(@itemprop, 'telephone')])[${i+1}]").getText().replace(".", "")
                        telefonoCompleto = eliminarCodigoArea(telefonoCompleto)
                    }
                    catch(Exception e) {
                        telefonoCompleto = buscar(driver, "(//span[contains(@itemprop, 'telephone')])[${i+1}]").getText().replace(".", "") // Vuelvo a intentar por las dudas
                        telefonoCompleto = eliminarCodigoArea(telefonoCompleto)
                    }
                    String calleDireccionMasNumero = buscar(driver, "(//span[@itemprop='streetAddress'])[${i+1}]").getText()
                    String datosDomicilio = buscar(driver, "(//span[@itemprop='addressLocality'])[${i+1}]").getText()
                    List<String> provinciaPoblacion = (datosDomicilio.replaceAll("\\(\\d+\\)", "")).split(",")
                    (calle, numero) = separarStrings(calleDireccionMasNumero, ", ")
                    String codigoPostal = datosDomicilio.find(/\((\d+)\)/) { fullMatch, group1 -> group1 }

                    if(provinciaPoblacion.size() > 1){
                        poblacion = normalizeName(provinciaPoblacion[0])
                        provincia = normalizeName(provinciaPoblacion[1])
                    }else{
                        provincia = normalizeName(provinciaPoblacion[0])
                        poblacion = null
                    }
                    def listNombre = personaBuscada.nombreCompleto.split(" ")
                    if (!revisaResultadoCorrecto(listNombre, buscarCommand, buscarRelacionada)) continue
                    domicilioCommand.calle = calle
                    try {
                        domicilioCommand.numero = numero != 'S/n' ? new Integer(numero) : 0
                    } catch (Exception e){
                        List<String> partesString = numero.split('\\.')
                        domicilioCommand.calle += " " + partesString[0]
                        domicilioCommand.numero = partesString[1].replace(",", "").toInteger()
                    }

                    domicilioCommand.codigoPostal = codigoPostal
                    def provinciaDomicilio = domicilioService.asignarProvincia(provincia, sitio)
                    def poblacionDomicilio = domicilioService.asignarPoblacion(poblacion, provinciaDomicilio)
                    domicilioCommand.provinciaId = provinciaDomicilio?.id
                    domicilioCommand.poblacionId = poblacionDomicilio?.id ?: null
                    domicilioCommand.estadoId = Estado.findByNombre("Sin Confirmar").id

                    personaBuscada.domicilios.add(domicilioCommand)
                    personaBuscada.telefonos.add(telefonoCompleto)
                    telefonosGuardados++
                    if (telefonosGuardados >= 25) {
                        break
                    }
                }
                try{
                    if (pagina == 0){
                        clickear(driver, "//a[text()='Siguiente']")
                    } else if (pagina == 1){
                        driver.get(url.replace('pag_2', 'pag_3'))
                    } else {
                        break
                    }
                } catch(e){
                    break
                }
            }
        } 
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_abctelefonos" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            personaBuscada = null
            mensajeError = "AbcTeléfonos: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            personaBuscada = null
            mensajeError = "AbcTeléfonos: " + e.message.split("finerror")[0]
        }
        finally {
            return [personaBuscada: personaBuscada, mensajeError: mensajeError]
        }
    }
}
