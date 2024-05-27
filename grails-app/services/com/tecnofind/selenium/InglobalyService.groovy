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
import com.tecnofind.persona.PersonaService
import com.tecnofind.Estado
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.Auxiliar
import com.tecnofind.domicilio.DomicilioCommand
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.busqueda.BuscarCommand
import org.joda.time.LocalDateTime

class InglobalyService extends SeleniumService{

    def domicilioService
    Sitio getSitio() {
        return Sitio.findByNombre("Inglobaly")
    }

    def guardarInformacionIngloblalyPersonas(BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        def driver
        Sitio sitio = getSitio()
        def resultado
        try{
            Oficina oficina = accessRulesService.getCurrentUser().oficina
            assert oficina : "El usuario actual no pertenece a ninguna oficinafinerror"
            driver = inicializarDriver()
            def url = sitio.url
            Credencial credencial = Credencial.findBySitioAndOficina(Sitio.findByNombre("Inglobaly"), oficina)
            assert credencial : "No se encontraron las credenciales necesarias para acceder al sitiofinerror"
            def usuarioLogin = credencial.usuario
            def password = credencial.password
            driver.get(url)
            escribir(driver, "j_id_j:login", usuarioLogin)
            escribir(driver, "j_id_j:pass", password)
            clickearConTry(driver, "//*[@id='j_id_j']/ul/li[3]/a")

            assert driver.getCurrentUrl() != 'https://www.inglobaly.com/privado/consumos_lock.jsf' : "La cuenta de Inglobaly se ha quedado sin saldofinerror"
            assert driver.getCurrentUrl() != 'https://www.inglobaly.com/utils/unlockUser.xhtml' : "El usuario de Inglobaly está en usofinerror"
            
            resultado = buscarInglobaly(driver, buscarCommand, buscarRelacionada)
            if (!resultado?.personaBuscada && buscarCommand && tieneSinonimos(buscarCommand)) {
                try{
                    clickear(driver, "//label[@id='domicilios:provincia_label']//following-sibling::div")
                    clickear(driver, '//li[@id="domicilios:provincia_0"]')
                } catch(e){}
                BuscarCommand buscarCommandTraducido = traducirNombres(buscarCommand)
                resultado = buscarInglobaly(driver, buscarCommandTraducido)
            }
        }
        catch(Exception e){
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_save_persona_inglobaly" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultado.personaBuscada = null
            mensajeError = "Inglobaly: Error buscando la persona"
        }
        catch(AssertionError e){
            println e.message.split("finerror")[0]
            resultado.personaBuscada = null
            mensajeError = "Inglobaly: " + e.message.split("finerror")[0]
        }
        finally{
            try{
                driver.get("https://www.inglobaly.com/salir")
            } catch(Exception ex){
                Auxiliar.printearError(ex)
            }
            cerrar(driver)
            return [resultado: resultado?.personaBuscada, mensajeError: resultado?.mensajeError]
        }
    }

    def buscarInglobaly(WebDriver driver, BuscarCommand buscarCommand = null, Persona buscarRelacionada = null) {
        def personaBuscada = new PersonaCommand()
        def hayDe
        String mensajeError = null
        personaBuscada.oficinaId = accessRulesService.getCurrentUser().oficina.id
        personaBuscada.sitioId = getSitio().id
        try{
            driver.get("https://www.inglobaly.com/privado/domicilios/avanzada.xhtml") //Ir a domicilios

            clickearConTry(driver, "//*[@id='domicilios:tipo_busqueda:1']")
            clickearConTry(driver, "//*[@id='domicilios:tipo_busqueda:0']")
            String documentoBuscar = buscarCommand?.documento ?: buscarRelacionada?.documento
            if (documentoBuscar){
                limpiarInput(driver, "//input[@id='cif:nif']")
                escribir(driver, "//input[@id='cif:nif']", documentoBuscar) //Buscar persona por NIF
                clickear(driver, "//input[@id='cif:buscarnif']")
            } else{
                String nombres
                String apellidoPadre
                String apellidoMadre
                DomicilioCommand domicilioBuscar 
                if(buscarCommand) {
                    nombres = (buscarCommand.primerNombre ? buscarCommand.primerNombre + " " : "") + (buscarCommand.segundoNombre ?: "")
                    apellidoPadre = buscarCommand.apellidoPadre
                    apellidoMadre = buscarCommand.apellidoMadre
                    domicilioBuscar = buscarCommand.domicilio
                    String mensajePrint = "Intentando buscar con los parámetros:\n"  
                    Auxiliar.separador (mensajePrint + buscarCommand.parametros)
                }
                else if (buscarRelacionada){
                    nombres = (buscarRelacionada.primerNombre ? buscarRelacionada.primerNombre + " " : "") + (buscarRelacionada.segundoNombre ?: "")
                    apellidoPadre = buscarRelacionada.apellidoPadre
                    apellidoMadre = buscarRelacionada.apellidoMadre
                    domicilioBuscar = buscarRelacionada.domicilios.first()
                }
                limpiarInput(driver, "//input[@id='domicilios:name']")
                escribir(driver, "//input[@id='domicilios:name']", nombres)
                hayDe = contieneDE(nombres + " " + apellidoPadre + " " + apellidoMadre)
                if(apellidoPadre){
                    limpiarInput(driver, "//input[@id='domicilios:primer_apellido']")
                    escribir(driver, "//input[@id='domicilios:primer_apellido']", apellidoPadre)
                }
                if(apellidoMadre){
                    limpiarInput(driver, "//input[@id='domicilios:segundo_apellido']")
                    escribir(driver, "//input[@id='domicilios:segundo_apellido']", apellidoMadre)
                }
                if(domicilioBuscar?.provinciaId) {
                    clickear(driver, "//label[@id='domicilios:provincia_label']//following-sibling::div")
                    WebElement searchProvincia = buscar(driver, '//*[@id="domicilios:provincia_filter"]')
                    escribir(driver, '//*[@id="domicilios:provincia_filter"]', domicilioService.getProvincia(domicilioBuscar.provinciaId).nombre)
                    searchProvincia.sendKeys(Keys.ENTER)
                    try{
                        esperarElemento(driver, '//*[@id="domicilios:poblacion_label"]', '-All-')
                    } catch(e){}
                    if(domicilioBuscar?.poblacionId) {                    
                        Integer intentosPoblacion = 0
                        Integer maxIntentosPoblacion = 10
                        while((buscar(driver, '//*[@id="domicilios:poblacion_label"]').getText() != domicilioService.getPoblacion(domicilioBuscar.poblacionId).nombre) && intentosPoblacion < maxIntentosPoblacion){
                            try{
                                clickear(driver, "//label[@id='domicilios:poblacion_label']//following-sibling::div")
                                clickear(driver, "//li[text()='${domicilioService.getPoblacion(domicilioBuscar.poblacionId).nombre}' and contains(@id,'poblacion')]")
                            } catch(e){
                                try{
                                    clickear(driver, "//label[@id='domicilios:poblacion_label']")
                                    WebElement searchPoblacion = buscar(driver, '//*[@id="domicilios:poblacion_filter"]')
                                    limpiarInput(driver, '//*[@id="domicilios:poblacion_filter"]')
                                    escribir(driver, '//*[@id="domicilios:poblacion_filter"]', domicilioService.getPoblacion(domicilioBuscar.poblacionId).nombre)
                                    Thread.sleep(1000)
                                    searchPoblacion.sendKeys(Keys.ENTER)
                                } catch(e2){}
                            }
                            intentosPoblacion++
                        }
                    }
                }
                if(domicilioBuscar?.calle) {
                    limpiarInput(driver, "//input[@id='domicilios:calle_input']")
                    escribir(driver, "//input[@id='domicilios:calle_input']", domicilioBuscar.calle)
                }
                if(domicilioBuscar?.numero) {
                    limpiarInput(driver, "//input[@id='domicilios:numero']")
                    escribir(driver, "//input[@id='domicilios:numero']", domicilioBuscar.numero.toString())
                }
                if(domicilioBuscar?.codigoPostal) {
                    limpiarInput(driver, "//input[@id='domicilios:cpostal']")
                    escribir(driver, "//input[@id='domicilios:cpostal']", domicilioBuscar.codigoPostal)
                }
                clickear(driver, "//input[@id='domicilios:buscar']")
            }

            List<WebElement> resultados = driver.findElements(By.xpath("//*[@id='datos:dt1_data']/tr/td[2]/a"))
            assert resultados.size() > 0: "No se encontraron resultados para esta búsquedafinerror"
            assert resultados.size() < 20: "Hay demasiados resultados para esta búsquedafinerror"
            WebElement resultadoCorrecto = null
            if(resultados.size() == 1){
                resultadoCorrecto = resultados[0]
            }
            else if(resultados.size() > 1){
                for(WebElement nombreResultado : resultados){
                    def listNombre = nombreResultado.getText().split(/[\s,]+/)
                    if(revisaResultadoCorrecto(listNombre, buscarCommand, buscarRelacionada)){
                        resultadoCorrecto = nombreResultado
                        break
                    }
                }
                assert resultadoCorrecto : "La persona encontrada no coincide con la buscadafinerror"
            }
            String nombreCelda = resultadoCorrecto.getText()
            personaBuscada = obtenerNombreyApellidos(personaBuscada, nombreCelda)
            resultadoCorrecto.click()
            WebElement anoNacimientoCell = buscar(driver, "//td[@class='year']/span")
            Integer anoNacimiento = anoNacimientoCell ? anoNacimientoCell.getText().split("/").last() as Integer : null
            //Cambio página Entra a la persona
            String direccionActualCompleta = ""
            String direccionCompletaAnterior = ""
            WebElement documentoCell = buscar(driver, "//*[@id='datos:ficha_nif']")
            String documento = "DESCONOCIDO".equals(documentoCell.getText()) ? null : documentoCell.getText()
            List<WebElement> elementsPoblacion = driver.findElements(By.xpath("//*[@id='datos']/div[3]/table/thead/tr/th[3]"))   
            if (elementsPoblacion[0].getText() == 'City' || elementsPoblacion[0].getText() == 'Población'){
                WebElement poblacionCell = buscar(driver, "//*[@id='datos:ficha_poblnac']")
                Poblacion poblacionNacimiento = Poblacion.findByNombre(poblacionCell.getText())
                personaBuscada.poblacionNacimientoId = poblacionNacimiento?.id ?: null
            } else{
                personaBuscada.poblacionNacimientoId = null
            }
            DomicilioCommand domicilio
            //Obtener poblacion de nacimiento
            Boolean hayDomicilio = false
            List<WebElement> elementsDomicilioActual = driver.findElements(By.xpath("//*[@id='datos:ficha_direccion_actual']"))
            if (elementsDomicilioActual.size > 0){
                hayDomicilio = true
            }
            List<WebElement> elementsDomicilioAnterior = driver.findElements(By.xpath("//*[@id='datos:ficha_direccion_anterior']"))
            Boolean hayDomicilioAnterior = false
            if (elementsDomicilioAnterior.size > 0){
                hayDomicilioAnterior = true
            }
            if (hayDomicilio || hayDomicilioAnterior){
                if (hayDomicilioAnterior){
                    WebElement domicilioAnteriorCell = buscar(driver, "//*[@id='datos:ficha_direccion_anterior']")
                    direccionCompletaAnterior = domicilioAnteriorCell.getText()
                }
                if (hayDomicilio){
                    WebElement domicilioActualCell = buscar(driver, "//*[@id='datos:ficha_direccion_actual']")
                    direccionActualCompleta = domicilioActualCell.getText()
                    try{
                        domicilio = guardarDomicilioInglobaly(driver, true, nombreCelda, hayDomicilio)
                    } catch(Exception e){
                        domicilio = null
                    }
                    personaBuscada.domicilios.add(domicilio)
                }
                if (hayDomicilioAnterior) {
                    DomicilioCommand domicilioAnterior
                    if (hayDomicilio){
                        if (direccionCompletaAnterior != direccionActualCompleta){
                            try{
                                domicilioAnterior = guardarDomicilioInglobaly(driver, false, nombreCelda, hayDomicilio)
                            } catch(Exception e){
                                domicilioAnterior = null
                            }
                            personaBuscada.domicilios.add(domicilioAnterior)
                        }
                    }else{
                        try{
                            domicilioAnterior = guardarDomicilioInglobaly(driver, false, nombreCelda, hayDomicilio)
                        } catch(Exception e){
                            domicilioAnterior = null
                        }
                        personaBuscada.domicilios.add(domicilioAnterior)
                    }
                }
            }else{
                assert false : "No se encontraron domiciliosfinerror"
            }
            personaBuscada.anoNacimiento = anoNacimiento
            personaBuscada.documento = documento
            if (buscarCommand){
                try{
                    personaBuscada = guardarPersonasRelacionadasDomicilio(driver, personaBuscada)
                } catch(Exception errorPersonasRelacionadasporDomicilio){
                    Auxiliar.printearError(errorPersonasRelacionadasporDomicilio)
                }
                try{
                    personaBuscada = guardarPersonasRelacionadasApellido(driver, personaBuscada, true)
                } catch(Exception errorPersonasRelacionadasporApellido){
                    Auxiliar.printearError(errorPersonasRelacionadasporApellido)
                }
            }
            try{
                guardarAutonomo(driver, personaBuscada, buscarCommand)
            } catch(Exception errorGuardadoAutonomo){
                Auxiliar.printearError(errorGuardadoAutonomo)
            }
        }
        catch(Exception e){
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_save_persona_inglobaly" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            personaBuscada = null
            mensajeError = "Inglobaly: Error buscando la persona"
        }
        catch(AssertionError e){
            println e.message.split("finerror")[0]
            personaBuscada = null
            mensajeError = "Inglobaly: " + e.message.split("finerror")[0]
        }
        finally{
            return [personaBuscada: personaBuscada, mensajeError: mensajeError]
        }
    }
    

    def guardarAutonomo(WebDriver driver, PersonaCommand personaCommand, BuscarCommand buscarCommand){
        driver.get("https://www.inglobaly.com/privado/autonomos/busqueda.xhtml")
        personaCommand.autonomo = false
        if (personaCommand.documento){
            escribir(driver, "//*[@id='nif:nif']", personaCommand.documento)
            clickearConTry(driver, "//*[@id='nif:buscarnif']")
        } else{
            if (!personaCommand.primerNombre && !personaCommand.segundoNombre){
                return personaCommand
            }
            String nombres = (personaCommand.primerNombre ? personaCommand.primerNombre + " " : "") + (personaCommand.segundoNombre ?: "")
            escribir(driver, "//*[@id='autonomos:nombre']", nombres)
            if(personaCommand.apellidoPadre){
                escribir(driver, "//*[@id='autonomos:primer_apellido']", personaCommand.apellidoPadre)
            }
            if(personaCommand.apellidoMadre){
                escribir(driver, "//*[@id='autonomos:segundo_apellido']", personaCommand.apellidoMadre)
            }
            if(personaCommand.domicilios) {
                clickear(driver, "//*[@id='autonomos:provincia']/div[3]")
                clickear(driver, "//li[contains(text(),'${domicilioService.getProvincia(personaCommand.domicilios[0].provinciaId).nombre}') and contains(@id,'provincia')]")
            }
            clickearConTry(driver, "//*[@id='autonomos:buscar']")
        }
        try{
            List<WebElement> tables = driver.findElements(By.xpath("//table[@role='grid']"))
            if (!tables.isEmpty()) {
                WebElement table = tables.get(0)
                List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"))
                int numberOfRows = rows.size()
                if (rows.size() < 10 && rows.size > 0){
                    clickearConTry(driver, "//*[@id='datos:dt1:0:nombre']")
                    personaCommand.autonomo = true
                    WebElement direccionCell = buscar(driver, "//*[@id='datos:ficha_direccion']")
                    String direccion =  direccionCell.getText()
                    personaCommand.domicilioAutonomo.add(direccion)
                    WebElement telefonosCell 
                    try{
                        telefonosCell = buscar(driver, "//*[@id='datos:ficha_telefonos']")
                    }
                    catch(Exception e){
                    }
                    if (telefonosCell){
                        def telefono = telefonosCell.getText().replace('|', '').replace(" ", "")
                        telefono = eliminarCodigoArea(telefono)
                        personaCommand.telefonosEmpresa.add(telefono.trim())
                    }
                }
            }
        } catch(Exception e){
            Auxiliar.printearError(e)
        }
        return personaCommand
    }

    def regexDomicilioAutonomo(String direccion){
        DomicilioCommand domicilioCommand = new DomicilioCommand() 
        def pattern = /^([^0-9]+)(\d+).*\((\d+)\)\s*([^-\s]+)\s*-\s*(.+)$/
        def matcher = (direccion =~ pattern)
        if (matcher.matches()) {
            def calle = matcher[0][1]
            def codigoPostal = matcher[0][2]
            def poblacion = matcher[0][3]
            def provincia = matcher[0][4]
        
            domicilioCommand.calle = calle
            domicilio.codigoPostal = codigoPostal

            if ((!Provincia.findByNombre(provincia))){
                domicilioCommand.provincia = Provincia.findByNombre('Sin Provincia')
            } else {
                domicilioCommand.provincia = Provincia.findByNombre(provincia)
            }  

            domicilioCommand.poblacion = Poblacion.findByNombre(poblacion)
            return domicilioCommand
        }
    }

    def DomicilioCommand guardarDomicilioInglobaly(WebDriver driver, Boolean esDomicilioActual, String nombreCompleto, Boolean hayDomicilio){
        String direccionCompleta = ""
        Estado estado = Estado.findByNombre("Sin Confirmar")
        Provincia provinciaDomicilio
        Poblacion poblacionDomicilio
        String codigoPostal
        Sitio sitio = getSitio()

        int toolbarIndex = 7
        if (hayDomicilio){
            toolbarIndex = 9
        }
        if (esDomicilioActual){
            DomicilioCommand domicilio = new DomicilioCommand()
            WebElement provinciaDomicilioCell = buscar(driver, "datos:ficha_provincia_actual")
            provinciaDomicilio = domicilioService.asignarProvincia(provinciaDomicilioCell.getText().toUpperCase(), sitio)    //Obtener provincia actual
            WebElement codPostalDomicilioActualCell = buscar (driver, "//*[@id='datos:ficha_cpostal_actual']")
            codigoPostal = codPostalDomicilioActualCell.getText()    //Obtener codigo postal actual
            WebElement poblacionDomicilioCell = buscar(driver, "datos:ficha_poblacion_actual")   
            poblacionDomicilio = domicilioService.asignarPoblacion(poblacionDomicilioCell.getText().split('-')[1].trim(), provinciaDomicilio)  //Obtener poblacion actual                
            //Cambio pagina
            clickearConTry(driver, '//a[@title="Number of people in the address"]')
            WebElement direccionCompletaCell = buscar(driver, "//*[@id='datos:dt1_data']/tr/td/table/thead/tr/td")
            direccionCompleta = direccionCompletaCell.getText()
            domicilio.provinciaId = provinciaDomicilio.id
            domicilio.poblacionId = poblacionDomicilio.id
            domicilio.codigoPostal = codigoPostal
            domicilio.estadoId = estado.id
            DomicilioCommand domicilioCreado = regexDireccion(direccionCompleta, domicilio)
            WebElement table = buscar(driver, "//*[@id='datos:dt1_data']/tr/td")
            List<WebElement> rows = table.findElements(By.tagName("tr"))
            def found = false
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"))
                for (WebElement cell : cells) {
                    if (cell.getText().contains(nombreCompleto)) {
                        WebElement link = cell.findElement(By.xpath(".//a"))
                        link.click()
                        found = true
                        break
                    }
                }
                if (found) {
                    break
                }
            }
            return domicilioCreado
        }else {
            DomicilioCommand domicilioAnterior = new DomicilioCommand()
            WebElement provinciaDomicilioAnteriorCell = buscar(driver, "//*[@id='datos:ficha_provincia_anterior']")
            provinciaDomicilio = domicilioService.asignarProvincia(provinciaDomicilioAnteriorCell.getText().toUpperCase(), sitio)
            WebElement codPostalDomicilioAnteriorCell = buscar (driver, "//*[@id='datos:ficha_cpostal_anterior']")
            codigoPostal = codPostalDomicilioAnteriorCell.getText()
            WebElement poblacionAnteriorCell = buscar(driver, "//*[@id='datos:ficha_poblacion_anterior']")
            poblacionDomicilio = domicilioService.asignarPoblacion(poblacionAnteriorCell.getText().split('-')[1].trim(), provinciaDomicilio)
            WebElement trElement = driver.findElement(By.xpath("//*[@id='datos']/div[${toolbarIndex}]/div/table/tbody/tr"))
            List<WebElement> tdElements = trElement.findElements(By.tagName("td"))
            int tdIndex = tdElements.size()
            if (tdElements.size()==1){
                clickearConTry(driver, "//*[@id='datos']/div[${toolbarIndex}]/div/table/tbody/tr/td/a")
            } else{
                clickearConTry(driver, "//*[@id='datos']/div[${toolbarIndex}]/div/table/tbody/tr/td[${tdIndex}]/a")
            }
            WebElement direccionAnteriorCompletaCell = buscar(driver, "//*[@id='datos:dt1_data']/tr[1]/td/table/thead/tr/td")
            direccionCompleta = direccionAnteriorCompletaCell.getText()
            domicilioAnterior.provinciaId = provinciaDomicilio.id
            domicilioAnterior.poblacionId = poblacionDomicilio.id
            domicilioAnterior.codigoPostal = codigoPostal
            domicilioAnterior.estadoId = estado.id
            domicilioAnterior.domicilioAnterior = true
            DomicilioCommand domicilioAnteriorCreado = regexDireccion(direccionCompleta, domicilioAnterior)
            WebElement table = buscar(driver, "//*[@id='datos:dt1_data']/tr/td")
            List<WebElement> rows = table.findElements(By.tagName("tr"))
            def found = false
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"))
                for (WebElement cell : cells) {
                    if (cell.getText().contains(nombreCompleto)) {
                        WebElement link = cell.findElement(By.xpath(".//a"))
                        link.click()
                        found = true
                        break
                    }
                }
                if (found) {
                    break
                }
            }
            return domicilioAnteriorCreado
        }
    }

    def regexDireccion(String direccionCompleta, DomicilioCommand domicilio){
        def regexDomicilioCalle = /(.+?)(\d+),/
        def matcherDomicilioCalle = (direccionCompleta =~ regexDomicilioCalle)
        String calleDomicilio
        Integer numeroDomicilio
        if (matcherDomicilioCalle.find()) {
            calleDomicilio = matcherDomicilioCalle.group(1).trim()
            numeroDomicilio = matcherDomicilioCalle.group(2) as Integer
            domicilio.calle = calleDomicilio
            domicilio.numero = numeroDomicilio
        } else {
            def splitDireccion = direccionCompleta.split(',')
            calleDomicilio = splitDireccion[0].trim()
            domicilio.calle = calleDomicilio
        }
        def regexAtributosDomicilio = /, (?:esc (\S+) )?(?:piso (\S+) )?(?:puerta (\S+))?(?: bloque (\S+) portal (\S+))?/
        def matcherAtributosDomicilio = (direccionCompleta =~ regexAtributosDomicilio)
        if (matcherAtributosDomicilio.find()) {
            if (matcherAtributosDomicilio.group(1) != null) {
                def escaleraDomicilio = matcherAtributosDomicilio.group(1)
                domicilio.escalera = escaleraDomicilio
            }
            if (matcherAtributosDomicilio.group(2) != null) {
                def pisoDomicilio = matcherAtributosDomicilio.group(2)
                domicilio.piso = pisoDomicilio
            }
            if (matcherAtributosDomicilio.group(3) != null) {
                def puertaDomicilio = matcherAtributosDomicilio.group(3)
                domicilio.puerta = puertaDomicilio
            }
            if (matcherAtributosDomicilio.group(4) != null) {
                def bloqueDomicilio = matcherAtributosDomicilio.group(4)
                domicilio.bloque = bloqueDomicilio
            }
            if (matcherAtributosDomicilio.group(5) != null) {
                def portalDomicilio = matcherAtributosDomicilio.group(5)
                println("Portal: $portalDomicilio") //Probar, no encontre domicilios con Portales
                domicilio.portal = portalDomicilio
            }
        }
        return domicilio
    }

    def guardarPersonasRelacionadasDomicilio(WebDriver driver, PersonaCommand personaCommand) {
        WebElement tablaDomicilioActual
        try{
            tablaDomicilioActual = buscar(driver, '(//div[@class="search"])[2]')
        }
        catch(e){
            driver.navigate().back()
            tablaDomicilioActual = buscar(driver, '(//div[@class="search"])[2]')
        }
        List<WebElement> tdsRelaciones = tablaDomicilioActual.findElements(By.tagName("td"))
        if (tdsRelaciones.size() > 0) {
            for (WebElement elemento : tdsRelaciones) {
                try {
                    WebElement checkbox = elemento.findElement(By.tagName("input"));
                    if (checkbox.getAttribute("type").equals("checkbox")) {
                        if (checkbox.isDisplayed() && checkbox.isEnabled()) {
                            checkbox.click();
                        }
                    }
                } catch (NoSuchElementException e) {
                }
            }
        }

        clickearConTry(driver, '//a[@title="Number of people in the address"]')
        def totalDePaginas
        try{
            def paginator = buscar(driver, '//span[@class="ui-paginator-pages"]')
            totalDePaginas = paginator.findElements(By.xpath('.//a')).size()
        }
        catch(e){
            totalDePaginas = 1
        }
        for (int p; p < totalDePaginas; p++){
            int pagina = 5 * p
            if(totalDePaginas > 1){
            clickearConTry(driver, "//a[@aria-label='Page ${p + 1}']")            
            esperarElemento(driver, "//a[contains(@class, 'ui-state-active') and @aria-label='Page ${p + 1}']")
            }
            WebElement totalDeDomicilios = buscar(driver, '//div[@id="datos:dt1"]');
            List<WebElement> domiciliosPorPagina = totalDeDomicilios.findElements(By.xpath('.//th[@class="centerText e location"]'));
            for (int domicilioNumero = 0; domicilioNumero < domiciliosPorPagina.size(); domicilioNumero++) {
                if(totalDePaginas > 1){
                clickearConTry(driver, "//a[@aria-label='Page ${p + 1}']")
                }
                def nombreDomicilio = buscar(driver, "(//td[@style='padding-left: 20px; font-weight: bold;'])[${domicilioNumero + 1}]").getText()
                DomicilioCommand domicilio = new DomicilioCommand()
                domicilio.provinciaId = personaCommand.domicilios.first().provinciaId
                domicilio.poblacionId = personaCommand.domicilios.first().poblacionId
                domicilio.codigoPostal = personaCommand.domicilios.first().codigoPostal
                domicilio.estadoId = Estado.findByNombre("Sin Confirmar").id
                
                try {
                    domicilio = regexDireccion(nombreDomicilio, domicilio)
                } catch(e){
                    domicilio = null
                }
                WebElement tablaRelaciones = buscar(driver,"//tbody[@id='datos:dt1:${pagina + domicilioNumero}:dt2_data']")
                List<WebElement> rowsRelaciones = tablaRelaciones.findElements(By.tagName("tr"))
                for (int i = 0; i < rowsRelaciones.size(); i++) {
                    if(totalDePaginas > 1){
                        clickearConTry(driver, "//a[@aria-label='Page ${p + 1}']")
                    }
                    try{
                        PersonaCommand personaRelacionadaCommand = new PersonaCommand()
                        def nombreCompletoRelacionado = buscar(driver, "(//a[contains(@id, 'datos:dt1:${pagina + domicilioNumero}:dt2:${i}')])[2]").getText()
                        if(nombreCompletoRelacionado.replace(" ", "") != (personaCommand.apellidoPadre + (personaCommand?.apellidoMadre ?: "") + "," + personaCommand.primerNombre + (personaCommand?.segundoNombre ?: ""))){
                            clickearConTry(driver, "(//a[contains(@id, 'datos:dt1:${pagina + domicilioNumero}:dt2:${i}')])[2]")
                            personaRelacionadaCommand.sitioId = personaCommand.sitioId
                            try{
                                WebElement poblacionCell = buscar(driver, "//*[@id='datos:ficha_poblnac']")
                                personaRelacionadaCommand.poblacionNacimientoId = Poblacion.findByNombre(poblacionCell.getText()).id
                            }
                            catch (Exception e){
                                personaRelacionadaCommand.poblacionNacimientoId = null
                            }

                            personaRelacionadaCommand = obtenerNombreyApellidos(personaRelacionadaCommand, nombreCompletoRelacionado)
                            WebElement documentoCell = buscar(driver, "//*[@id='datos:ficha_nif']")
                            String documento = "DESCONOCIDO".equals(documentoCell.getText()) ? null : documentoCell.getText()
                            personaRelacionadaCommand.documento = documento
                            personaRelacionadaCommand.oficinaId = personaCommand.oficinaId

                            WebElement anoNacimientoCell = buscar(driver, "//td[@class='year']/span")
                            Integer anoNacimiento = anoNacimientoCell ? anoNacimientoCell.getText().split("/").last() as Integer : null
                            personaRelacionadaCommand.anoNacimiento = anoNacimiento
                            def hayDomicilioAnterior
                            try {
                                hayDomicilioAnterior = driver.findElement(By.xpath("//*[@id='datos:ficha_direccion_anterior']")).isDisplayed()
                            } catch(Exception e) {
                                hayDomicilioAnterior = false
                            }
                            DomicilioCommand domicilioAnteriorCommand = new DomicilioCommand()
                            if (hayDomicilioAnterior){
                                def poblacionAnterior = buscar(driver, "//span[@id='datos:ficha_poblacion_anterior']").getText().replace(" ", "").replace("-", "")
                                def provinciaAnterior = buscar(driver, "//span[@id='datos:ficha_provincia_anterior']").getText()
                                def provinciaAnteriorDomicilio = domicilioService.asignarProvincia(provinciaAnterior, Sitio.findByNombre("Inglobaly"))
                                domicilioAnteriorCommand.provinciaId = provinciaAnteriorDomicilio.id
                                domicilioAnteriorCommand.poblacionId = domicilioService.asignarPoblacion(poblacionAnterior, provinciaAnteriorDomicilio).id
                                domicilioAnteriorCommand.codigoPostal = buscar(driver, "//*[@id='datos:ficha_cpostal_actual']").getText()
                                domicilioAnteriorCommand.domicilioAnterior = true
                                domicilioAnteriorCommand.estadoId = Estado.findByNombre("Sin Confirmar").id
                                clickearConTry(driver, "(//a[@title='Number of people in the address'])[last()]")
                                domicilioAnteriorCommand = regexDireccion(buscar(driver,"//td[@style='padding-left: 20px; font-weight: bold;']").getText(), domicilioAnteriorCommand)
                                driver.navigate().back()
                            }
                            if(domicilio.calle == domicilioAnteriorCommand.calle && domicilio.numero == domicilioAnteriorCommand.numero && domicilio.piso == domicilioAnteriorCommand.piso && domicilio.puerta == domicilioAnteriorCommand.puerta){
                                personaRelacionadaCommand.domicilios.add(domicilio)
                            }
                            else if(domicilioAnteriorCommand.calle != null){
                                personaRelacionadaCommand.domicilios.add(domicilio)
                                personaRelacionadaCommand.domicilios.add(domicilioAnteriorCommand)
                            }
                            else{
                                personaRelacionadaCommand.domicilios.add(domicilio)
                            }
                            boolean personaRelacionadaExistente
                            if (personaRelacionadaCommand.documento != null && personaRelacionadaCommand.documento != "DESCONOCIDO"){
                                personaRelacionadaExistente = personaCommand.personasRelacionadas.any { it.documento == personaRelacionadaCommand.documento }
                            }
                            if (!personaRelacionadaExistente) {
                                personaCommand.personasRelacionadas.add(personaRelacionadaCommand)
                            }
                            int casaCorrectaNumero = 0
                            def esDomicilioPrincipalCorrecto
                            try{
                                esDomicilioPrincipalCorrecto = buscar(driver, "//span[@id='datos:ficha_direccion_actual' and contains(text(), '${personaCommand?.domicilios.first().calle}')]")
                            } catch(e){}
                            if (esDomicilioPrincipalCorrecto){
                                casaCorrectaNumero = 2
                            } else{
                                def esDomicilioAnteriorCorrecto
                                try{
                                    esDomicilioAnteriorCorrecto = buscar(driver, "//span[@id='datos:ficha_direccion_anterior' and contains(text(), '${personaCommand?.domicilios.first().calle}')]")
                                } catch(e){}
                                if (esDomicilioAnteriorCorrecto){
                                    casaCorrectaNumero = 3
                                } else{
                                    clickear(driver, '//input[@id="domicilios:buscar"]')
                                    List<WebElement> resultados = driver.findElements(By.xpath("//*[@id='datos:dt1_data']/tr/td[2]/a"))
                                    resultados[0].click()
                                    casaCorrectaNumero = 2
                                }
                            }
                            if (casaCorrectaNumero != 0) {
                                WebElement tablaDomicilioActualPersonaRelacionada = buscar(driver, "(//div[@class='search'])[${casaCorrectaNumero}]")
                                List<WebElement> tdsRelacionesRelacionada = tablaDomicilioActualPersonaRelacionada.findElements(By.tagName("td"))
                                if (tdsRelacionesRelacionada.size() > 0) {
                                    for (WebElement elementoRelacionada : tdsRelacionesRelacionada) {
                                        try {
                                            WebElement checkboxRelacionada = elementoRelacionada.findElement(By.tagName("input"));
                                            if (checkboxRelacionada.getAttribute("type").equals("checkbox")) {
                                                if (checkboxRelacionada.isDisplayed() && checkboxRelacionada.isEnabled()) {
                                                    checkboxRelacionada.click();
                                                }
                                            }
                                        } catch (NoSuchElementException e) {
                                        }
                                    }
                                }
                                clickearConTry(driver, "(//a[@title='Number of people in the address'])[${casaCorrectaNumero - 1}]")
                            }
                        }
                    }
                    catch(Exception e){
                        Auxiliar.printearError(e)
                    }
                }            
            }
        }
        driver.navigate().back()
        return personaCommand
    }

    String normalizeName(String name) {
        return name.trim().toUpperCase().replaceAll(/\s+/, " ").replaceAll(/ ,/, ",")
    }

    def guardarPersonasRelacionadasApellido(WebDriver driver, PersonaCommand personaCommand, Boolean hayPersonaPrincipal = false) {
        clickearConTry(driver, '//a[@class="ui-link ui-widget selected"]')
        clickearConTry(driver, '//input[@id="domicilios:tipo_busqueda:1"]')
        clickearConTry(driver, '//input[@id="domicilios:tipo_busqueda:0"]')
        clickearConTry(driver, '//img[@id="domicilios:j_id_3r"]')
        escribir(driver, '//input[@id="domicilios:primer_apellido"]', personaCommand.apellidoPadre, true)
        escribir(driver, '//input[@id="domicilios:segundo_apellido"]', personaCommand.apellidoMadre, true)
        clickear(driver, "//label[@id='domicilios:provincia_label']//following-sibling::div")
        Integer intentos = 0
        Integer maxIntentos = 5
        WebElement searchProvincia = buscar(driver, '//*[@id="domicilios:provincia_filter"]')
        escribir(driver, '//*[@id="domicilios:provincia_filter"]', domicilioService.getProvincia(personaCommand.domicilios.first().provinciaId).nombre)
        searchProvincia.sendKeys(Keys.ENTER)
        try{
            if(buscarCommand.domicilio?.poblacionId) {
                Integer intentosPoblacion = 0
                Integer maxIntentosPoblacion = 10
                while((buscar(driver, '//*[@id="domicilios:poblacion_label"]').getText() != domicilioService.getPoblacion(personaCommand.domicilios.first().poblacionId).nombre) && intentosPoblacion < maxIntentosPoblacion){
                    try{
                        clickear(driver, "//label[@id='domicilios:poblacion_label']//following-sibling::div")
                        clickear(driver, "//li[text()='${domicilioService.getPoblacion(personaCommand.domicilios.first().poblacionId).nombre}' and contains(@id,'poblacion')]")
                    } catch(e){
                        try{
                            clickear(driver, "//label[@id='domicilios:poblacion_label']")
                            WebElement searchPoblacion = buscar(driver, '//*[@id="domicilios:poblacion_filter"]')
                            limpiarInput(driver, '//*[@id="domicilios:poblacion_filter"]')
                            escribir(driver, '//*[@id="domicilios:poblacion_filter"]', domicilioService.getPoblacion(personaCommand.domicilios.first().poblacionId).nombre)
                            Thread.sleep(1000)
                            searchPoblacion.sendKeys(Keys.ENTER)
                        } catch(e2){}
                    }
                    intentosPoblacion++
                }
            }
        }catch(e){}

        clickearConTry(driver, '//input[@name="domicilios:buscar"]')
        def resultados
        try{
            WebElement tablaRelaciones = buscar(driver,"//tbody[@id='datos:dt1_data']")
            List<WebElement> rowsRelaciones = tablaRelaciones.findElements(By.tagName("tr"))
            if(rowsRelaciones.size() > 60){
                assert true: "Se han encontrado muchos resultadosfinerror"
            }else if(rowsRelaciones.size() < 1){
                assert true: "No se han encontrado resultadosfinerror"
            }
            resultados = rowsRelaciones.size()
        }
        catch(Exception e){
            Auxiliar.printearError(e, "Error buscando personas relacionadas por apellido")
            resultados = 0
            return personaCommand
        }
        if (resultados > 1) {
            for (int i = 0; i < resultados; i++) {
                try{
                    def nombreCompletoRelacionado = quitarArticulos(buscar(driver, "//a[@id='datos:dt1:${i}:j_id_53']").getText())
                    if(nombreCompletoRelacionado.replace(" ", "") != (personaCommand.apellidoPadre + (personaCommand?.apellidoMadre ?: "") + "," + personaCommand.primerNombre + (personaCommand?.segundoNombre ?: "")).replace(" ", "")){
                        if(comprobarPersonaCorrectaApellido(personaCommand, nombreCompletoRelacionado)){
                            clickearConTry(driver, "//a[@id='datos:dt1:${i}:j_id_53']")
                            PersonaCommand personaRelacionadaCommand = new PersonaCommand()
                            personaRelacionadaCommand.sitioId = personaCommand.sitioId
                            def poblacionNacimiento
                            try{
                                WebElement poblacionCell = buscar(driver, "//*[@id='datos:ficha_poblnac']")
                                poblacionNacimiento = Poblacion.findByNombre(poblacionCell.getText()).id
                            }
                            catch (Exception e){
                                poblacionNacimiento = null
                            }                     
                            if (hayPersonaPrincipal){
                                personaRelacionadaCommand = obtenerNombreyApellidos(personaRelacionadaCommand, nombreCompletoRelacionado)
                                personaRelacionadaCommand.poblacionNacimientoId = poblacionNacimiento
                                personaRelacionadaCommand.documento = buscar(driver, "//span[@id='datos:ficha_nif']").getText() != "DESCONOCIDO" ? null : buscar(driver, "//span[@id='datos:ficha_nif']").getText()
                                personaRelacionadaCommand.oficinaId = personaCommand.oficinaId
                                WebElement anoNacimientoCell = buscar(driver, "//td[@class='year']/span")
                                Integer anoNacimiento = anoNacimientoCell ? anoNacimientoCell.getText().split("/").last() as Integer : null                            
                                personaRelacionadaCommand.anoNacimiento = anoNacimiento
                            }
                            else {
                                personaCommand = obtenerNombreyApellidos(personaCommand, nombreCompletoRelacionado)
                                personaCommand.poblacionNacimientoId = poblacionNacimiento
                                personaRelacionadaCommand.documento = buscar(driver, "//span[@id='datos:ficha_nif']").getText() != "DESCONOCIDO" ? null : buscar(driver, "//span[@id='datos:ficha_nif']").getText()
                                WebElement anoNacimientoCell = buscar(driver, "//td[@class='year']/span")
                                Integer anoNacimiento = anoNacimientoCell ? anoNacimientoCell.getText().split("/").last() as Integer : null                            
                                personaRelacionadaCommand.anoNacimiento = anoNacimiento
                            }
                            DomicilioCommand domicilio = new DomicilioCommand()
                            String direccionActualCompleta = ""
                            String direccionCompletaAnterior = ""
                            def hayDomicilioAnterior
                            try {
                                hayDomicilioAnterior = driver.findElement(By.xpath("//*[@id='datos:ficha_direccion_anterior']")).isDisplayed()
                            } catch(Exception e) {
                                hayDomicilioAnterior = false
                            }
                            def hayDomicilio
                            try {
                                hayDomicilio = driver.findElement(By.xpath("//*[@id='datos:ficha_direccion_actual']")).isDisplayed()
                            }
                            catch(Exception e){
                                hayDomicilio = false
                            }
                            if (hayDomicilio || hayDomicilioAnterior){
                                if (hayDomicilio){
                                    WebElement domicilioActualCell = buscar(driver, "//*[@id='datos:ficha_direccion_actual']")
                                    direccionActualCompleta = domicilioActualCell.getText()
                                }
                                if (hayDomicilioAnterior){
                                    WebElement domicilioAnteriorCell = buscar(driver, "//*[@id='datos:ficha_direccion_anterior']")
                                    direccionCompletaAnterior = domicilioAnteriorCell.getText()
                                }
                                if (hayDomicilio){
                                    try{
                                        domicilio = guardarDomicilioInglobaly(driver, true, nombreCompletoRelacionado, hayDomicilio)
                                    } catch(e){
                                        domicilio = null
                                    }                                
                                    if (hayPersonaPrincipal) {
                                        personaRelacionadaCommand.domicilios.add(domicilio)
                                    }
                                    else {
                                        personaCommand.domicilios.add(domicilio)
                                    }
                                }
                                if (hayDomicilioAnterior) {
                                    DomicilioCommand domicilioAnteriorCommand
                                    if (hayDomicilio){
                                        if (direccionCompletaAnterior != direccionActualCompleta) {
                                            try{
                                                domicilioAnteriorCommand = guardarDomicilioInglobaly(driver, false, nombreCompletoRelacionado, hayDomicilio)                                        
                                            } catch(e){
                                                domicilioAnteriorCommand = null
                                            }   
                                            if(hayPersonaPrincipal){
                                                personaRelacionadaCommand.domicilios.add(domicilioAnteriorCommand)
                                            }
                                            else {
                                                personaCommand.domicilios.add(domicilioAnteriorCommand)
                                            }
                                        }
                                    }else{
                                        try {
                                            domicilioAnteriorCommand = guardarDomicilioInglobaly(driver, false, nombreCompletoRelacionado, hayDomicilio)
                                        } catch(e){
                                            domicilioAnteriorCommand = null
                                        }   
                                        if (hayPersonaPrincipal) {
                                            personaRelacionadaCommand.domicilios.add(domicilioAnteriorCommand)
                                        }
                                        else {
                                            personaCommand.domicilios.add(domicilioAnteriorCommand)
                                        }
                                    }
                                }
                            }else{
                                println("No se encontraron domicilios.")
                                return
                            }
                            if (hayPersonaPrincipal){
                                boolean personaRelacionadaExistente = personaCommand.personasRelacionadas.any {
                                    it.primerNombre == personaRelacionadaCommand.primerNombre &&
                                    it.segundoNombre == personaRelacionadaCommand.segundoNombre &&
                                    it.apellidoPadre == personaRelacionadaCommand.apellidoPadre &&
                                    it.apellidoMadre == personaRelacionadaCommand.apellidoMadre
                                }
                                boolean personaRelacionadaDocumento = false
                                if (personaRelacionadaCommand.documento != null && personaRelacionadaCommand.documento != "DESCONOCIDO"){
                                    personaRelacionadaDocumento = personaCommand.personasRelacionadas.any { it.documento == personaRelacionadaCommand.documento }                
                                }
                                if (!personaRelacionadaExistente && !personaRelacionadaDocumento) {
                                    personaCommand.personasRelacionadas.add(personaRelacionadaCommand)
                                }
                            }
                            if (i == 0){
                                hayPersonaPrincipal = true
                            }
                            clickearConTry(driver, '//input[@name="domicilios:buscar"]')
                        }
                    }
                }
                catch(Exception e){
                    Auxiliar.printearError(e)
                }
            }
        }
        return personaCommand
    }

    def comprobarPersonaCorrectaApellido(PersonaCommand personaCommand, String nombreCompletoRelacionado){
        def apellidos = nombreCompletoRelacionado.split(",")[0]
        if((nombreCompletoRelacionado.replace(" ", "") != (personaCommand.apellidoPadre + (personaCommand?.apellidoMadre ?: "") + "," + personaCommand.primerNombre + (personaCommand?.segundoNombre ?: ""))) && ((apellidos.replace(" ", "")) == (personaCommand.apellidoPadre + (personaCommand?.apellidoMadre ?: "")))){
            return true
        } else{
            return false
        }
    }

    public Map guardarInformacionIngloblalyDireccion(BuscarCommand buscarCommand) {
        def driver
        def personaCommand = new PersonaCommand()
        List<PersonaCommand> resultados = []
        String mensajeError = null
        Sitio sitio = getSitio()

        try{
            def currentUser = accessRulesService.getCurrentUser()
            Oficina oficina = currentUser.oficina
            assert oficina : "El usuario actual no pertenece a ninguna oficinafinerror"
            driver = inicializarDriver()
            def url = sitio.url
            Credencial credencial = Credencial.findBySitioAndOficina(Sitio.findByNombre("Inglobaly"), oficina)
            assert credencial : "No se encontraron las credenciales necesarias para acceder al sitiofinerror"
            def usuarioLogin = credencial.usuario
            def password = credencial.password
            driver.get(url)
            escribir(driver, "j_id_j:login", usuarioLogin)
            escribir(driver, "j_id_j:pass", password)
            clickearConTry(driver, "//*[@id='j_id_j']/ul/li[3]/a")

            assert driver.getCurrentUrl() != 'https://www.inglobaly.com/utils/unlockUser.xhtml' : "El usuario de Inglobaly está en usofinerror"
            driver.get("https://www.inglobaly.com/privado/domicilios/avanzada.xhtml") //Ir a domicilios
            clickearConTry(driver, "//*[@id='domicilios:tipo_busqueda:2']")
            if(buscarCommand.domicilio?.provinciaId) {
                clickear(driver, "//label[@id='domicilios:provincia_label']//following-sibling::div")
                WebElement searchProvincia = buscar(driver, '//*[@id="domicilios:provincia_filter"]')
                escribir(driver, '//*[@id="domicilios:provincia_filter"]', domicilioService.getProvincia(buscarCommand.domicilio.provinciaId).nombre)
                searchProvincia.sendKeys(Keys.ENTER)
                try{
                    esperarElemento(driver, '//*[@id="domicilios:poblacion_label"]', '-All-')
                } catch(e){}
                if(buscarCommand.domicilio?.poblacionId) {                    
                    Integer intentosPoblacion = 0
                    Integer maxIntentosPoblacion = 10
                    while((buscar(driver, '//*[@id="domicilios:poblacion_label"]').getText() != domicilioService.getPoblacion(buscarCommand.domicilio.poblacionId).nombre) && intentosPoblacion < maxIntentosPoblacion){
                        try{
                            clickear(driver, "//label[@id='domicilios:poblacion_label']//following-sibling::div")
                            clickear(driver, "//li[text()='${domicilioService.getPoblacion(buscarCommand.domicilio.poblacionId).nombre}' and contains(@id,'poblacion')]")
                        } catch(e){
                            try{
                                clickear(driver, "//label[@id='domicilios:poblacion_label']")
                                WebElement searchPoblacion = buscar(driver, '//*[@id="domicilios:poblacion_filter"]')
                                limpiarInput(driver, '//*[@id="domicilios:poblacion_filter"]')
                                escribir(driver, '//*[@id="domicilios:poblacion_filter"]', domicilioService.getPoblacion(buscarCommand.domicilio.poblacionId).nombre)
                                Thread.sleep(1000)
                                searchPoblacion.sendKeys(Keys.ENTER)
                            } catch(e2){}
                        }
                        intentosPoblacion++
                    }
                }
            }

            escribir(driver, "//*[@id='domicilios:num']", buscarCommand.domicilio.numero.toString())
            if(buscarCommand.domicilio.piso){
                escribir(driver, "//*[@id='domicilios:piso']", buscarCommand.domicilio.piso)
            }
            if(buscarCommand.domicilio.puerta){
                escribir(driver, "//*[@id='domicilios:puerta']", buscarCommand.domicilio.puerta)
            }
            if(buscarCommand.domicilio.escalera){
                escribir(driver, "//*[@id='domicilios:escalera']", buscarCommand.domicilio.escalera)
            }
            if(buscarCommand.domicilio.bloque){
                escribir(driver, "//*[@id='domicilios:bloque']", buscarCommand.domicilio.bloque)
            }
            if(buscarCommand.domicilio.portal){
                escribir(driver, "//*[@id='domicilios:portal']", buscarCommand.domicilio.portal)
            }
            Boolean calleEncontrada = false
            List<String> auxiliares = ['y', 'del', 'la', 'las', 'los', 'de', 'en', 'al', 'a', 'el', 'la', 'calle', 'rambla', 'avenida']
            for(String palabra :buscarCommand.domicilio.calle.trim().split(" ")) {
                if (auxiliares.contains(palabra.toLowerCase())) continue
                
                escribir(driver, "//*[@id='domicilios:calle_input']", palabra, true, javascript:true, modoHumano:true)
                try{
                    clickearConTry(driver, "//li[contains(@data-item-value, '${buscarCommand.domicilio.calle.trim().toUpperCase()}')]", 4)
                    calleEncontrada = true
                }
                catch(Exception e){
                    continue
                }
            }
            assert calleEncontrada : "No se encontró la calle ${buscarCommand.domicilio.calle}finerror"
            clickear(driver, "//input[@id='domicilios:buscar']")
            def totalDePaginas
            try{
                def paginator = buscar(driver, '//span[@class="ui-paginator-pages"]')
                totalDePaginas = paginator.findElements(By.xpath('.//a')).size()
            }
            catch(e){
                totalDePaginas = 1
            }
            for(int pagina = 0; pagina < totalDePaginas; pagina++){
                resultados = obtenerResultadosPagina(driver, resultados, buscarCommand, pagina)
            }
            resultados.each { persona ->
                List<PersonaCommand> personasRelacionadas = new ArrayList<>(resultados)
                personasRelacionadas.remove(persona)
                personasRelacionadas.each { 
                    persona.personasRelacionadas.add(it)
                }
            }
        }
        catch(Exception e){
            Auxiliar.printearError(e)
            sacarScreen(driver, ("error_save_direccion_inglobaly" + new LocalDateTime().toString('yyyyMMddHHmmss')))
            resultados = null
            mensajeError = "Inglobaly: Error buscando la dirección"
        }
        catch(AssertionError e){
            println e.message.split("finerror")[0]
            resultados = null
            mensajeError = "Inglobaly: " + e.message.split("finerror")[0]
        }
        finally{
            try{
                driver.get("https://www.inglobaly.com/salir")
            } catch(Exception ex){
                Auxiliar.printearError(ex)
            }
            cerrar(driver)
            return [resultado: resultados, mensajeError: mensajeError]
        }
    }

    private obtenerResultadosPagina(WebDriver driver, List<PersonaCommand> resultados, BuscarCommand buscarCommand, Integer pagina) {
        if(pagina > 0){
            clickearConTry(driver, "//a[@aria-label='Page ${pagina + 1}']")
        }
        esperarElemento(driver, "//a[@id='datos:dt1:${5 * pagina}:dom_conviven']")
        Long resultadosPagina = driver.findElements(By.xpath("//a[@title='Number of people in the address']")).size()
        for(int i = 0; i < resultadosPagina; i++){
            def resultado = buscar(driver, "//a[@id='datos:dt1:${i + 5 * pagina}:dom_conviven']")
            DomicilioCommand domicilioCommand = new DomicilioCommand()
            domicilioCommand = regexDireccion(resultado.getText(), domicilioCommand)
            domicilioCommand.provinciaId = buscarCommand.domicilio.provinciaId
            domicilioCommand.poblacionId = buscarCommand.domicilio.poblacionId 
            domicilioCommand.estadoId = Estado.findByNombre("Sin Confirmar").id
            resultado.click()
            def totalDePersonas = buscar(driver, '//div[@class="ui-datatable-footer ui-widget-header ui-corner-bottom"]').getText().replace("Found ", "").replace(" records", "").toInteger()
            for (int persona = 0; persona < totalDePersonas; persona++){
                def nombreCompleto = buscar(driver, "//a[@id='datos:dt1:${persona}:nombre']").getText()
                clickear(driver, "//a[@id='datos:dt1:${persona}:nombre']")
                PersonaCommand personaRow = new PersonaCommand()
                try{
                    domicilioCommand.codigoPostal = buscar(driver, '//span[@id="datos:ficha_poblacion_actual"]').getText().split("-")[0].trim()
                } catch(e){
                    domicilioCommand.codigoPostal null
                }
                try{
                    WebElement poblacionCell = buscar(driver, "//*[@id='datos:ficha_poblnac']")
                    personaRow.poblacionNacimientoId = Poblacion.findByNombre(poblacionCell.getText()).id
                }
                catch (Exception e){
                    personaRow.poblacionNacimientoId = null
                }
                personaRow = obtenerNombreyApellidos(personaRow, nombreCompleto)
                WebElement documentoCell = buscar(driver, "//*[@id='datos:ficha_nif']")
                String documento = "DESCONOCIDO".equals(documentoCell.getText()) ? null : documentoCell.getText()
                WebElement anoNacimientoCell = buscar(driver, "//td[@class='year']/span")
                Integer anoNacimiento = anoNacimientoCell ? anoNacimientoCell.getText().split("/").last() as Integer : null
                personaRow.anoNacimiento = anoNacimiento
                def hayDomicilioAnterior
                try {
                    hayDomicilioAnterior = driver.findElement(By.xpath("//*[@id='datos:ficha_direccion_anterior']")).isDisplayed()
                } catch(e) {
                    hayDomicilioAnterior = false
                }
                DomicilioCommand domicilioAnteriorCommand = new DomicilioCommand()
                if (hayDomicilioAnterior){
                    String poblacionAnterior
                    try{
                        poblacionAnterior = buscar(driver, "//span[@id='datos:ficha_cpostal_anterior']").getText().replace(" ", "").split("-")[1]

                    } catch(e) {}
                    String provinciaAnterior
                    try{
                        provinciaAnterior = buscar(driver, "//span[@id='datos:ficha_provincia_anterior']").getText()
                    } catch(e) {}
                    def provinciaAnteriorDomicilio = domicilioService.asignarProvincia(provinciaAnterior, Sitio.findByNombre("Inglobaly"))
                    if (provinciaAnteriorDomicilio){
                        domicilioAnteriorCommand.provinciaId = provinciaAnteriorDomicilio.id
                    }
                    def poblacionAnteriorDomicilio = domicilioService.asignarPoblacion(poblacionAnterior, provinciaAnteriorDomicilio)
                    if (poblacionAnterior){
                        domicilioAnteriorCommand.poblacionId = poblacionAnteriorDomicilio.id
                    }
                    domicilioAnteriorCommand.codigoPostal = buscar(driver, '//span[@id="datos:ficha_cpostal_anterior"]').getText().split("-")[0].trim()
                    domicilioAnteriorCommand.domicilioAnterior = true
                    domicilioAnteriorCommand.estadoId = Estado.findByNombre("Sin Confirmar").id
                    clickearConTry(driver, "(//a[@title='Number of people in the address'])[last()]")
                    domicilioAnteriorCommand = regexDireccion(buscar(driver,"//td[@style='padding-left: 20px; font-weight: bold;']").getText(), domicilioAnteriorCommand)
                    driver.navigate().back()
                }
                if(domicilioCommand.calle == domicilioAnteriorCommand.calle && domicilioCommand.numero == domicilioAnteriorCommand.numero && domicilioCommand.piso == domicilioAnteriorCommand.piso && domicilioCommand.puerta == domicilioAnteriorCommand.puerta){
                    personaRow.domicilios.add(domicilioCommand)
                }
                else if(domicilioAnteriorCommand.calle != null){
                    personaRow.domicilios.add(domicilioCommand)
                    personaRow.domicilios.add(domicilioAnteriorCommand)
                }
                else{
                    personaRow.domicilios.add(domicilioCommand)
                }
                personaRow.documento = documento
                personaRow.oficinaId = accessRulesService.getCurrentUser().oficina.id
                personaRow.sitioId = getSitio().id
                resultados.add(personaRow)
                clickear(driver, "//a[@href='javascript:atras()']")
            }
            clickear(driver, "//a[@href='javascript:atras()']")
            if(pagina > 0){
                clickearConTry(driver, "//a[@aria-label='Page ${pagina + 1}']")
            }
        }
        return resultados
    }
}
