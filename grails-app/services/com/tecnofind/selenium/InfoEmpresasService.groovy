package com.tecnofind.selenium
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
import com.tecnofind.AccessRulesService
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.busqueda.BuscarCommand
import com.tecnofind.Auxiliar
import com.tecnofind.persona.Telefono
import com.tecnofind.domicilio.DomicilioCommand
import com.tecnofind.Estado
import com.tecnofind.persona.Persona
import com.tecnofind.persona.Telefono
import com.tecnofind.Estado
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.AccessRulesService
import com.tecnofind.oficina.Oficina
import com.tecnofind.domicilio.DomicilioService
import org.openqa.selenium.remote.RemoteWebElement
import org.joda.time.LocalDateTime

class InfoEmpresasService extends SeleniumService {

    def grailsApplication
    def accessRulesService

    private Sitio getSitio() {
        return Sitio.findByNombre("Infoempresa")
    }

    def busquedaInfoEmpresas(BuscarCommand buscarCommand, Persona buscarRelacionada = null) {
        def driver
        def resultado
        try {
            User userActual = accessRulesService.getCurrentUser()
            assert userActual.oficina != null:"El usuario actual no pertenece a ninguna oficinafinerror"
            driver = inicializarDriver(true, true)
            resultado = buscarInfoEmpresa(driver, buscarCommand, buscarRelacionada)
            if (!resultado?.personaBuscada && buscarCommand && tieneSinonimos(buscarCommand)) {
                BuscarCommand buscarCommandTraducido = traducirNombres(buscarCommand)
                resultado = buscarInfoEmpresa(driver, buscarCommandTraducido)
            }
        }
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_infoempresas" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultado.personaBuscada = null
            mensajeError = "InfoEmpresas: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            mensajeError = "InfoEmpresas: " + e.message.split("finerror")[0]
            resultado.personaBuscada = null
        }
        finally {
            cerrar(driver)
            return [resultado: resultado?.personaBuscada, mensajeError: resultado.mensajeError]
        }
    }

    def buscarInfoEmpresa(WebDriver driver, BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        PersonaCommand personaBuscada = new PersonaCommand()
        String mensajeError = null
        try{
            personaBuscada.oficinaId = accessRulesService.getCurrentUser().oficina.id
            personaBuscada.sitioId = getSitio().id
            
            driver.get(getSitio().url)
            def datosBuscar
            if(buscarRelacionada) {
                datosBuscar = buscarRelacionada
            }
            else {
                datosBuscar = buscarCommand
                String mensajePrint = "Intentando buscar con los parámetros:\n"  
                Auxiliar.separador (mensajePrint + buscarCommand.parametros)
            }
            try {
                clickear(driver, '//*[@id="cookies-wrapper"]/div/div[1]/div[2]/button')
            }
            catch(e) {
            }
        
            String campoBusqueda = ""
            if(datosBuscar.primerNombre){
                campoBusqueda += " ${datosBuscar.primerNombre.toUpperCase()}"
            }
            if(datosBuscar.segundoNombre){
                campoBusqueda += " ${datosBuscar.segundoNombre.toUpperCase()}"
            }
            if(datosBuscar.apellidoPadre){
                campoBusqueda += " ${datosBuscar.apellidoPadre.toUpperCase()}"
            }
            if(datosBuscar.apellidoMadre){
                campoBusqueda += " ${datosBuscar.apellidoMadre.toUpperCase()}"
            }

            String campoBusquedaInvertido = ""
            if(datosBuscar.apellidoPadre){
                campoBusquedaInvertido += " ${datosBuscar.apellidoPadre.toUpperCase()}"
            }
            if(datosBuscar.apellidoMadre){
                campoBusquedaInvertido += " ${datosBuscar.apellidoMadre.toUpperCase()}"
            }
            if(datosBuscar.primerNombre){
                campoBusquedaInvertido += " ${datosBuscar.primerNombre.toUpperCase()}"
            }
            if(datosBuscar.segundoNombre){
                campoBusquedaInvertido += " ${datosBuscar.segundoNombre.toUpperCase()}"
            }
            escribir(driver, "//*[@id='buscador-home']/div[2]/form/div[2]/div/input", campoBusqueda)
            clickearConTry(driver,"//*[@id='buscador-home']/div[2]/form/div[2]/button[1]")
            clickearConTry(driver, "//*[@id='#search-leaders']/a")

            List<WebElement> elementos = driver.findElements(By.xpath("//*[@id='search-leaders']/ul/li/a/span[1]/span"))
            WebElement elementoEncontrado = null
            def nombreSinArticulos
            for (WebElement elemento : elementos) {
                nombreSinArticulos = quitarArticulos(elemento.getText(), contieneDE(elemento.getText()))
                if ((nombreSinArticulos).trim().equals(campoBusqueda.trim())) {
                    personaBuscada = obtenerNombreyApellidos(personaBuscada, quitarArticulos(nombreSinArticulos, contieneDE(elemento.getText())))
                    elemento.click()
                    break 
                }
                else if ((nombreSinArticulos).trim().equals(campoBusquedaInvertido.trim())) {
                    personaBuscada = obtenerNombreyApellidos(personaBuscada, quitarArticulos(nombreSinArticulos, contieneDE(elemento.getText())))
                    elemento.click()
                    break
                }
                else{
                    assert false: "No se encontraron resultados para esta búsquedafinerror"
                }
            }

            clickearConTry(driver, "//*[@id='content-box']/div[1]/div[2]/div[1]/div/div/div[2]/div/div/div/div[2]/div/div/a")
            personaBuscada.razonSocialEmpresa = buscar(driver, "//div[@class='empresa-title']/h1").getText()
            try{
                def telefonoEmpresa = buscar(driver, "//*[@id='company-data']/div/div/div/div/div/ul/li[2]/span[2]").getText() 
                telefonoEmpresa = eliminarCodigoArea(telefonoEmpresa)
                personaBuscada.telefonosEmpresa.add(telefonoEmpresa) 
            }
            catch(e){}
            try {
                String doc = buscar(driver, "//span[contains(text(), 'NIF/CIF')]").getText().split(": ")[1]
                personaBuscada.documentoEmpresa = doc != "No disponible" ? doc : null
            }
            catch(e){}
            try {
                personaBuscada.urlEmpresa = buscar(driver, "//span[contains(text(), 'Sitio web')]//following-sibling::span").getText()
            }
            catch(e){}
            try {
                personaBuscada.domicilioAutonomo.add(buscar(driver, "//span[contains(text(), 'Dirección:')]//following-sibling::span").getText())
            }
            catch(e){}
            personaBuscada = busquedaInfoEmpresasRelacionadas(driver, personaBuscada)
        }
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_infoempresas" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            personaBuscada = null
            mensajeError = "Infoempresas: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            personaBuscada = null
            mensajeError = "Infoempresas: " + e.message.split("finerror")[0]
        }
        finally {
            return [personaBuscada: personaBuscada, mensajeError: mensajeError]
        }
    }

    def busquedaInfoEmpresasRelacionadas(WebDriver driver, PersonaCommand personaCommand){
        def listaPersonasRelacionadas = buscar(driver, '//ul[@class="empresa-directivos__list"]')
        List<WebElement> listElements = listaPersonasRelacionadas.findElements(By.xpath('.//div[@class="tree-title underlined f-bold blue-grey-text fs-std"]'))
        if (listElements.size() > 1) {
            for (WebElement elemento : listElements) {
                try{
                    PersonaCommand personaRelacionadaCommand = new PersonaCommand()
                    personaRelacionadaCommand.sitioId = personaCommand.sitioId
                    personaRelacionadaCommand.oficinaId = personaCommand.oficinaId
                    personaRelacionadaCommand = obtenerNombreyApellidos(personaRelacionadaCommand, quitarArticulos(elemento.getText(), contieneDE(elemento.getText())))
                    personaRelacionadaCommand.documentoEmpresa = personaCommand.documentoEmpresa
                    personaRelacionadaCommand.urlEmpresa = personaCommand.urlEmpresa
                    personaRelacionadaCommand.domicilioAutonomo.addAll(personaCommand.domicilioAutonomo)
                    personaRelacionadaCommand.telefonosEmpresa.addAll(personaCommand.telefonosEmpresa)               
                    boolean personaRelacionadaExistente = personaCommand.personasRelacionadas.any { 
                        it.primerNombre == personaRelacionadaCommand.primerNombre &&
                        it.segundoNombre == personaRelacionadaCommand.segundoNombre &&
                        it.apellidoPadre == personaRelacionadaCommand.apellidoPadre &&
                        it.apellidoMadre == personaRelacionadaCommand.apellidoMadre
                    }

                    if (!personaRelacionadaExistente) {
                        personaCommand.personasRelacionadas.add(personaRelacionadaCommand)
                    }
                }
                catch(Exception e){
                    Auxiliar.printearError(e)
                }
            }            
        }
        return personaCommand
    }
}
