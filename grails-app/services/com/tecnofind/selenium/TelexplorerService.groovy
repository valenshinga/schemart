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
import com.tecnofind.sitio.Sitio
import com.tecnofind.credencial.Credencial
import com.tecnofind.User
import com.tecnofind.domicilio.Poblacion
import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import com.tecnofind.domicilio.Provincia
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.persona.Persona
import com.tecnofind.Estado
import com.tecnofind.Auxiliar
import com.tecnofind.AccessRulesService
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.domicilio.DomicilioCommand
import com.tecnofind.busqueda.BuscarCommand
import org.joda.time.LocalDateTime

class TelexplorerService extends SeleniumService {

    def grailsApplication
    def accessRulesService
    def domicilioService

    private Sitio getSitio() {
        return Sitio.findByNombre("Telexplorer")
    }

    def busquedaTelexplorer(BuscarCommand personaBuscarCommand = null, Persona buscarRelacionada = null){
        def driver
        Sitio sitio = getSitio()
        def resultado
        try {
            User userActual = accessRulesService.getCurrentUser()
            assert userActual.oficina != null:"El usuario actual no pertenece a ninguna oficinafinerror"
            driver = inicializarDriver()

            resultado = buscarTelexplorer(driver, personaBuscarCommand, buscarRelacionada)
            if (!resultado?.personaBuscada && personaBuscarCommand && tieneSinonimos(personaBuscarCommand)) {
                BuscarCommand buscarCommandTraducido = traducirNombres(personaBuscarCommand)
                resultado = buscarTelexplorer(driver, buscarCommandTraducido)
            }
        }
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_telexplorer" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultado.personaBuscada = null
            mensajeError = "Telexprorer: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            mensajeError = "Telexprorer: " + e.message.split("finerror")[0]
            resultado.personaBuscada = null
        }
        finally {
            cerrar(driver)
            return [resultado:resultado?.personaBuscada, mensajeError:resultado?.mensajeError]
        }
    }

    def buscarTelexplorer(WebDriver driver, BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        PersonaCommand personaBuscada = new PersonaCommand()
        String mensajeError = null
        try {
            def url = getSitio().url
            driver.get(url)
            def datosBuscar
            def domicilioBuscar
            personaBuscada.oficinaId = accessRulesService.getCurrentUser().oficina.id
            personaBuscada.sitioId = getSitio().id
            if(buscarRelacionada) {
                datosBuscar = buscarRelacionada
                domicilioBuscar = buscarRelacionada?.domicilios?.first()
            }
            else {
                datosBuscar = buscarCommand
                domicilioBuscar = buscarCommand.domicilio
                String mensajePrint = "Intentando buscar con los parámetros:\n"  
                Auxiliar.separador (mensajePrint + buscarCommand.parametros)
            }
            String nombreBusqueda = ""
            if(datosBuscar.apellidoPadre){
                nombreBusqueda += datosBuscar.apellidoPadre.toUpperCase()
            }
            if(datosBuscar.apellidoMadre){
                nombreBusqueda += " ${datosBuscar.apellidoMadre.toUpperCase() ?: ""}"
            }
            def hayde = contieneDE(nombreBusqueda)
            escribir(driver, "//input[@id='nombre']", nombreBusqueda)
            clickearConTry(driver, "//input[@id='boton_buscar_1']")

            def paginaDeResultados = buscar(driver, "/html/body/div[1]").getAttribute("id")
            if(paginaDeResultados != "todo2"){
                assert false: "No se encontraron resultados para esta búsquedafinerror"
            }

            def cantidadResultados = quitarArticulos(buscar(driver, "//*[@id='resultados_cant']/p/font").getText(), hayde).split(" ")[-1].toInteger()
            if (cantidadResultados > 25 || cantidadResultados == 0) {
                nombreBusqueda += " ${datosBuscar.primerNombre.toUpperCase()} ${datosBuscar.segundoNombre?.toUpperCase() ?: ""}"
                clickearConTry(driver, "//*[@id='boton_busqueda']/a")
                esperarElemento(driver, '//input[@id="nombre"]')
                escribir(driver, "//input[@id='nombre']", nombreBusqueda)
                clickearConTry(driver, "//input[@id='boton_buscar_1']")
                paginaDeResultados = buscar(driver, "/html/body/div[1]").getAttribute("id")
                if(paginaDeResultados != "todo2"){
                    assert false: "No se encontraron resultados para esta búsquedafinerror"
                }
                cantidadResultados = buscar(driver, "//*[@id='resultados_cant']/p/font").getText().split(" ")[-1].toInteger()
            }
            assert cantidadResultados > 0: "No se encontraron resultados que coinciden con la búsquedafinerror"
            assert cantidadResultados <= 25: "Se han encontrado muchos resultados para esta búsquedafinerror"
            int cambioPagina = 0
            int telefonosGuardados = 0
            for(int i = 1; i <= cantidadResultados; i++){
                if (telefonosGuardados >= 25) {
                    break
                }
                if(i > 20 && cambioPagina != 20){
                    clickear(driver, '//a[text()="Siguiente>"]')
                    esperarElemento(driver, '//a[text()="2"]')
                    cambioPagina = 20
                } else if (i > 10 && cambioPagina != 10 && cambioPagina != 20){
                    clickear(driver, '//a[text()="Siguiente>"]')
                    esperarElemento(driver, '//a[text()="1"]')
                    cambioPagina = 10
                }
                def nombreCompleto = buscar(driver, "//*[@id='listado_resultados_residenciales']/li[${i - cambioPagina}]/p[1]").getText()
                def listNombre = nombreCompleto.split(" ") 
                if (!revisaResultadoCorrecto(listNombre, buscarCommand, buscarRelacionada)) continue
                DomicilioCommand domicilioCommand = new DomicilioCommand()
                def telefono = buscar(driver,"//ol/li[${i - cambioPagina}]//p[@class='resultado_telefono']").getText().replace(" ", "")
                telefono = eliminarCodigoArea(telefono)
                personaBuscada.telefonos.add(telefono)
                telefonosGuardados++
                personaBuscada = obtenerNombreyApellidos(personaBuscada, nombreCompleto)
                def provinciaMasPoblacionMasCalleMasNumero = buscar(driver, "//ol/li[${i - cambioPagina}]//p[@class='resultado_domicilio']").getText()
                def calleMasNumero = normalizeName(separarStrings(provinciaMasPoblacionMasCalleMasNumero, "\n")[0])
                def provinciaMasPoblacion = normalizeName(separarStrings(provinciaMasPoblacionMasCalleMasNumero, "\n")[1])
                def provincia = separarStrings(provinciaMasPoblacion, "-")[0]
                def poblacion = separarStrings(provinciaMasPoblacion, "-")[1]
                if(poblacion == provincia){
                    poblacion = null
                }
                def calle = separarStrings(calleMasNumero, ",")[0]
                def numero
                try{
                    numero = separarStrings(calleMasNumero, ",")[1].toInteger()
                } catch(e){
                    numero = null
                }
                domicilioCommand.calle = calle
                domicilioCommand.numero = numero
                def provinciaDomicilio = domicilioService.asignarProvincia(provincia, sitio)
                domicilioCommand.provinciaId = provinciaDomicilio.id
                domicilioCommand.poblacionId = domicilioService.asignarPoblacion(poblacion, provinciaDomicilio)?.id
                domicilioCommand.estadoId = Estado.findByNombre("Sin Confirmar").id
                personaBuscada.domicilios.add(domicilioCommand)
            }
        }
        catch (Exception e) {
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_telexplorer" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            personaBuscada = null
            mensajeError = "Telexplorer: Error buscando la persona"
        }
        catch (AssertionError e) {
            println e.message.split("finerror")[0]
            personaBuscada = null
            mensajeError = "Telexprorer: " + e.message.split("finerror")[0]
        }
        finally {
            return [personaBuscada: personaBuscada, mensajeError: mensajeError]
        }
    }
}
