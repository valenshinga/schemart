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
import org.openqa.selenium.support.ui.Select;

class BittoolsService extends SeleniumService {

    def grailsApplication
    def accessRulesService
    def domicilioService

    private Sitio getSitio() {
        return Sitio.findByNombre('Bittools')
    }

    def busquedaBittools(BuscarCommand buscarCommand, Persona buscarRelacionada = null) {
        def driver
        PersonaCommand personaBuscada = new PersonaCommand()
        String mensajeError = null
        Sitio sitio = getSitio()
        def resultado
        try {
            User userActual = accessRulesService.getCurrentUser()
            Oficina oficina = userActual.oficina
            assert oficina : "El usuario actual no pertenece a ninguna oficinafinerror"
            Credencial credencial = Credencial.findBySitioAndOficina(sitio, oficina)
            assert credencial : "No se encontraron las credenciales necesarias para acceder al sitiofinerror"
         

            driver = inicializarDriver()    
            driver.get(sitio.url) 
            def usuarioLogin = credencial.usuario
            def password = credencial.password   
            clickearConTry(driver, "//*[@id='wrapper']/div[4]/nav/div[2]/a[1]/span") 
            escribir(driver, "//*[@id='username']", usuarioLogin)
            escribir(driver, "//*[@id='password']", password)
            clickear(driver, 'nota_legal', javascript:true)
            clickearConTry(driver, "/html/body/div[2]/div/div/div/div/form/div[4]/div[1]/button") 
            resultado = buscarBittools(driver, buscarCommand, buscarRelacionada)
            if (!resultado?.personaBuscada && buscarCommand && tieneSinonimos(buscarCommand)) {
                BuscarCommand buscarCommandTraducido = traducirNombres(buscarCommand)
                resultado = buscarBittools(driver, buscarCommandTraducido)
            }
        } 
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_bittools" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultado.personaBuscada = null
            mensajeError = "Bittools: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            mensajeError = "Bittools: " + e.message.split("finerror")[0]
            resultado.personaBuscada = null
        }
        finally {
            cerrar(driver)
            return [resultado:resultado?.personaBuscada, mensajeError:resultado.mensajeError]
        }
    } 

    def importarPoblacionesBittools(){
        def driver
        String mensajeError = null
        Sitio sitio = getSitio()
        try {
            Oficina oficina = accessRulesService.getCurrentUser().oficina
            assert oficina : "El usuario actual no pertenece a ninguna oficinafinerror"
            Credencial credencial = Credencial.findBySitioAndOficina(sitio, oficina)
            assert credencial : "No se encontraron las credenciales necesarias para acceder al sitiofinerror"
            def usuarioLogin = credencial.usuario
            def password = credencial.password            
           
            driver = inicializarDriver()     
            def url = sitio.url
            driver.get(url)
            clickearConTry(driver, "//*[@id='wrapper']/div[4]/nav/div[2]/a[1]/span")  
            escribir(driver, "//*[@id='username']", usuarioLogin)
            escribir(driver, "//*[@id='password']", password)
            clickear(driver, 'nota_legal', javascript:true)
            clickearConTry(driver, "/html/body/div[2]/div/div/div/div/form/div[4]/div[1]/button") 
            clickearConTry(driver, "//*[@id='side-menu']/li[4]/a")
            Select provinciaSelect = new Select(driver.findElement(By.id("provincia")))
            List<WebElement> provinciaOptions = provinciaSelect.getOptions()
            List<WebElement> poblacionOptions = []
            int i = 3
            while (i <= provinciaOptions.size()-1) { 
                try{ 
                    clickear(driver, "provincia")
                    clickear(driver, "//*[@id='provincia']/option[${i}]" )
                    clickear(driver, "//*[@id='provincia']/option[${i}]" )
                    String nombre = provinciaSelect.getFirstSelectedOption().getText()
                    def provincia = domicilioService.asignarProvincia(nombre.toUpperCase(), Sitio.findByNombre("Bittools"))
                    Select poblacionSelect = new Select(driver.findElement(By.id("poblacion")))
                    poblacionOptions.clear()
                    poblacionOptions = poblacionSelect.getOptions()
                    List<Poblacion> poblaciones = new ArrayList<>()
                    for (WebElement poblacionOption : poblacionOptions) {
                        Poblacion poblacion = new Poblacion()
                        poblacion.nombre = poblacionOption.getText()
                        poblacion.provincia = provincia
                        poblaciones.add(poblacion)
                    }
                    poblaciones.each { pobl ->
                        if(Poblacion.findByNombreAndProvincia(pobl.nombre, pobl.provincia)) return
                        pobl.save(flush:true, failOnError:true)
                    }           
                    i+=1
                } catch(Exception e){
                    Auxiliar.printearError(e)
                    provinciaSelect = new Select(driver.findElement(By.id("provincia")))
                    provinciaOptions = provinciaSelect.getOptions()
                    continue
                }
            }
        } catch (Exception e){
            Auxiliar.printearError(e)
        }finally{
            cerrar(driver)
        }
    }

    def buscarBittools(WebDriver driver, BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        PersonaCommand personaBuscada = new PersonaCommand()
        String mensajeError = null
        try{
            personaBuscada.oficinaId = accessRulesService.getCurrentUser().oficina.id
            personaBuscada.sitioId = getSitio().id
            def provincia
            def poblacion
            def datosBuscar
            def domicilioBuscar
            List<String> telefonos
            String telefono
            if(buscarRelacionada) {
                datosBuscar = buscarRelacionada
                domicilioBuscar = buscarRelacionada.domicilios?.first()
            }
            else {
                datosBuscar = buscarCommand
                domicilioBuscar = buscarCommand.domicilio
                String mensajePrint = "Intentando buscar con los parámetros:\n"  
                Auxiliar.separador (mensajePrint + buscarCommand.parametros)
            }       
    

            clickearConTry(driver, "//*[@id='side-menu']/li[2]/a") 
            if (datosBuscar.primerNombre && datosBuscar.segundoNombre) {
                escribir(driver, "//*[@id='nombre']", datosBuscar.primerNombre + ' ' + datosBuscar.segundoNombre)
            }
            else if (datosBuscar.primerNombre) {
                escribir(driver, "//*[@id='nombre']", datosBuscar.primerNombre)
            }
            if (datosBuscar.apellidoPadre) {
                escribir(driver, "//*[@id='apellido1']", datosBuscar.apellidoPadre)
            }
            if (datosBuscar.apellidoMadre) {
                escribir(driver, "//*[@id='apellido2']", datosBuscar.apellidoMadre)
            }
            def hayde = contieneDE(datosBuscar.primerNombre + " " + datosBuscar.segundoNombre + " " + datosBuscar.apellidoMadre + " " + datosBuscar.apellidoPadre)
            clickearConTry(driver, "//*[@id='provincia']")
            clickearConTry(driver, "//*[@id='provincia']")
            esperarElemento(driver, "//*[@id='provincia']/option[2]") 
            clickearConTry(driver, "//*[@id='provincia']/option[2]")
            clickearConTry(driver, "//*[@id='botonDescarga']")
            esperarElemento(driver, "//*[@id='wrapper']/div[6]/div[2]/div/div[3]/div[1]")

            if (buscar(driver, "//*[@id='load_registros']/tbody/tr/td").getText() == 'Resultados: 0') {
                assert false: "No se encontraron resultados para esta búsquedafinerror"
            }
            else if (buscar(driver, "//*[@id='load_registros']/tbody/tr[1]/td[2]").isDisplayed()) {
                List<WebElement> registros = driver.findElements(By.xpath("//*[@id='load_registros']/tbody/tr"))
                int cantidadRegistros = registros.size()
                if (cantidadRegistros > 10) {
                    assert false: "Hay demasiados resultados para esta búsquedafinerror"
                }
                for(int i = 1; i <= cantidadRegistros; i++){
                    def nombre = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[2]").getText().toUpperCase() ?: null

                    def listNombre
                    if (nombre) {
                        personaBuscada.primerNombre = separarStrings(nombre, " ")[0]
                        personaBuscada.segundoNombre = separarStrings(nombre, " ")[1] ?: null
                    }
                    personaBuscada.apellidoPadre = quitarArticulos(buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[3]").getText(), hayde).toUpperCase() ?: null
                    personaBuscada.apellidoMadre = quitarArticulos(buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[4]").getText(), hayde).toUpperCase() ?: null
                    listNombre = personaBuscada.nombreCompleto.split(" ")
                    if (!revisaResultadoCorrecto(listNombre, buscarCommand, buscarRelacionada)) continue

                    try {
                        WebElement elemento = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[11]/i")
                        String valorTitle = elemento.getAttribute("title");
                        valorTitle = valorTitle.replace(" ", "")
                        telefonos = valorTitle.split('-')
                        telefonos.each { telefonoEncontrado ->
                            personaBuscada.telefonos.add(telefonoEncontrado)
                        }
                    } 
                    catch (Exception e) {
                        telefono = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[11]/a").getText() ?: null
                        personaBuscada.telefonos.add(telefono)
                    }
                    DomicilioCommand domicilio = new DomicilioCommand()
                    domicilio.calle = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[6]").getText() ?: null
                    domicilio.numero = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[7]").getText().toInteger() ?: 0
                    domicilio.codigoPostal = buscar(driver, "//*[@id='load_registros']/tbody/tr/td[8]").getText() ?: null
                    domicilio.estadoId = Estado.findByNombre('Sin Confirmar').id
                    provincia = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[10]").getText() ?: null
                    poblacion = buscar(driver, "//*[@id='load_registros']/tbody/tr[${i}]/td[9]").getText() ?: null
                    Provincia provinciaReal = domicilioService.asignarProvincia(provincia.toUpperCase(), sitio)
                    domicilio.provinciaId = provinciaReal?.id
                    domicilio.poblacionId = domicilioService.asignarPoblacion(poblacion.toUpperCase() , provinciaReal)?.id
                    personaBuscada.domicilios.add(domicilio)
                }
            }
        } 
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_bittools" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            personaBuscada = null
            mensajeError = "Bittools: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            personaBuscada = null
            mensajeError = "Bittools: " + e.message.split("finerror")[0]
        }
        finally {
            return [personaBuscada: personaBuscada, mensajeError: mensajeError]
        }
    }
}
