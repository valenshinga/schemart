package com.tecnofind.busqueda

import com.tecnofind.AccessRulesService
import com.tecnofind.Auxiliar
import com.tecnofind.User
import com.tecnofind.UserRole
import com.tecnofind.busqueda.Busqueda
import com.tecnofind.domicilio.Provincia
import com.tecnofind.domicilio.Poblacion
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.Persona
import com.tecnofind.persona.PersonaRelacionada
import com.tecnofind.persona.Telefono
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.persona.PersonaService
import com.tecnofind.persona.NombreService
import com.tecnofind.selenium.InglobalyService
import com.tecnofind.selenium.AbcTelefonosService
import com.tecnofind.selenium.BittoolsService
import com.tecnofind.selenium.InfoEmpresasService
import com.tecnofind.selenium.TelexplorerService
import com.tecnofind.sitio.Sitio
import com.tecnofind.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode

import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import org.joda.time.LocalDateTime
import java.util.LinkedHashMap
import com.tecnofind.selenium.InglobalyService
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.FillPatternType
import com.tecnofind.Auxiliar
import com.tecnofind.domicilio.DomicilioCommand
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.util.CellRangeAddressList
import org.apache.poi.xssf.usermodel.XSSFDataValidation
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFName

@Transactional
class BusquedaService {
    def sessionFactory
    def abcTelefonosService
    def accessRulesService
    def bittoolsService
    def domicilioService
    def infoEmpresasService
    def inglobalyService
    def nombreService
    def notificacionService
    def personaService
    def telexplorerService
    def usuarioService
    def grailsApplication

    public List<TipoDato> listTiposDatos() {
        return TipoDato.list()
    }

    public validarBusqueda(BuscarCommand buscarCommand){
        def personaBuscada = null
        def message = ''
        def error = false

        if ( buscarCommand.documento ) {
            personaBuscada = personaService.getPersonaByDocumento(buscarCommand.documento)
        }

        if (personaBuscada) {
            def user = accessRulesService.getCurrentUser()
            if (personaBuscada.oficina?.id != user.oficina?.id ) {
                def oficinaPersona = Oficina.get(personaBuscada.oficina.id)
                message = "La persona pertenece a la Oficina ${oficinaPersona.nombre}"
                error = true
            }
        } 
        return [message: message, error: error]
    }

    public Busqueda getBusqueda(Long id){
        return Busqueda.get(id)
    }
    
    public BusquedaCommand generarInstanciaBusqueda (BuscarCommand buscarCommand) {
        BusquedaCommand busquedaCommand = new BusquedaCommand()
        busquedaCommand.responsableId = accessRulesService.getCurrentUser().id
        busquedaCommand.fechaHora = LocalDateTime.now()

        busquedaCommand.inputs = [buscarCommand.telefono, buscarCommand.documento, buscarCommand.primerNombre, buscarCommand.segundoNombre, 
            buscarCommand.apellidoPadre, buscarCommand.apellidoMadre, buscarCommand?.domicilio?.codigoPostal, 
            domicilioService.getProvincia(buscarCommand?.domicilio?.provinciaId)?.nombre, 
            domicilioService.getPoblacion(buscarCommand?.domicilio?.poblacionId)?.nombre,
            buscarCommand?.domicilio?.calle, buscarCommand?.domicilio?.numero?.toString(), buscarCommand?.domicilio?.piso, buscarCommand?.domicilio?.puerta]
        return busquedaCommand 
    }

    public Busqueda busquedaSave (BusquedaCommand busquedaInstanciada, Map resultadosBusqueda) {
        Auxiliar.separador()
        println "Persona Encontrada:"
        println "${resultadosBusqueda?.persona?.primerNombre ?: ' '} ${resultadosBusqueda?.persona?.segundoNombre ?: ' '} ${resultadosBusqueda?.persona?.apellidoPadre ?: ' '} ${resultadosBusqueda?.persona?.apellidoMadre ?: ' '}"
        println "- Mensajes: "
        println "${resultadosBusqueda.mensajes?.join('\n')}"
        println "- Sitios: "
        println "${resultadosBusqueda.sitiosVisitados?.join('\n')}"
        Auxiliar.separador()
        Busqueda busqueda = new Busqueda()
        busqueda.responsable = User.get(busquedaInstanciada.responsableId)
        busqueda.fechaHora = busquedaInstanciada.fechaHora
        busqueda.inputs = []
        def map = [:]
        busquedaInstanciada.inputs.each { 
            if(it)
                map[it] = map.containsKey(it) ? map[it] + ',' + it : it 
        }
        busqueda.inputs = map.values().toList()
        
        busqueda.save(flush:true, failOnError:true)
        if(resultadosBusqueda.persona instanceof List) {
                resultadosBusqueda.persona.each { personaEncontrada ->
                guardarPersonaBusqueda(busqueda,personaEncontrada)
            }
        }
        else{
            if(resultadosBusqueda.persona){
                busqueda.personaPrincipal = resultadosBusqueda.persona
                guardarPersonaBusqueda(busqueda,resultadosBusqueda.persona)
                if (resultadosBusqueda.persona.personasRelacionadas) {
                    resultadosBusqueda.persona.personasRelacionadas.each { personaRelacionada ->
                        guardarPersonaBusqueda(busqueda,personaRelacionada.relacionada)
                    }
                }
                
            } 
        }
        if(resultadosBusqueda.sitiosVisitados){
            resultadosBusqueda.sitiosVisitados.each { sitio ->
                busqueda.addToSitiosVisitados(sitio)
            }
        }
        if(resultadosBusqueda.mensajes){
            busqueda.logError = []
            busqueda.logError.addAll(resultadosBusqueda.mensajes)
        }
        busqueda.save(flush:true, failOnError:true)

        def user = accessRulesService.getCurrentUser()
        try{
            notificacionService.enviarMailReportPostOrquestador(user.username, resultadosBusqueda, busqueda)
        }catch(Exception e){
            Auxiliar.printearError(e)
        }
        return busqueda
    }

    public List<Busqueda> listBusqueda(){
        def esAdmin = accessRulesService.isAdmin()
        def esSuperAdmin = accessRulesService.isSuperAdmin()
        def usuario = accessRulesService.getCurrentUser()

        def query = """
            SELECT 
                DISTINCT(BUSQUEDA.ID) AS ID,
                USERS.nombre AS RESPONSABLE,
                TO_CHAR(BUSQUEDA.FECHA_HORA,'dd/MM/yyyy - HH24:mi') AS FECHAHORA,
                (SELECT STRING_AGG(INPUTS_STRING,', ')
                    FROM BUSQUEDA_INPUTS
                    WHERE BUSQUEDA_INPUTS.BUSQUEDA_ID = BUSQUEDA.ID
                    GROUP BY BUSQUEDA_INPUTS.BUSQUEDA_ID) AS INPUTS,
                (SELECT COUNT(*)
                    FROM PERSONA_BUSQUEDA
                    WHERE BUSQUEDA.ID = PERSONA_BUSQUEDA.BUSQUEDA_ID) AS RESULTADOS,
                (SELECT STRING_AGG(nombre,', ')
                    FROM SITIO AS SITIOS
                    JOIN BUSQUEDA_SITIO ON BUSQUEDA_SITIO.sitio_id = SITIOS.ID AND BUSQUEDA_SITIO.busqueda_sitios_visitados_id = BUSQUEDA.ID) AS sitios
            FROM BUSQUEDA
            JOIN BUSQUEDA_INPUTS ON BUSQUEDA_INPUTS.BUSQUEDA_ID = BUSQUEDA.ID
            LEFT JOIN BUSQUEDA_SITIO ON BUSQUEDA_SITIO.busqueda_sitios_visitados_id = BUSQUEDA.ID
            INNER JOIN USERS ON BUSQUEDA.RESPONSABLE_ID = USERS.ID
        """
        if (esSuperAdmin) {
            query += "ORDER BY fechaHora DESC ;"
        } else if (esAdmin) {
            query += "INNER JOIN oficina ON users.oficina_id = oficina.id "
            query += "WHERE oficina.id = ${usuario.oficina.id} ORDER BY fechaHora DESC ;"
        } else {
            query += "WHERE busqueda.responsable_id = ${usuario.id} ORDER BY fechaHora DESC ;"
        }
        def busquedas = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
    }

    public Map nuevaBusqueda(BuscarCommand buscarCommand) {
        Map resultado = [:]
        Persona persona = null
        List<String> mensajes = []
        List<Sitio> sitiosVisitados = []

        try{
            if(buscarCommand.tieneNombre || buscarCommand.documento){
                resultado = buscarInglobaly(buscarCommand, persona, mensajes, sitiosVisitados, true)
                resultado = buscarAbcTelefonos(buscarCommand, resultado.persona, mensajes, sitiosVisitados, true)
                resultado = buscarTelexprorer(buscarCommand, resultado.persona, mensajes, sitiosVisitados, true)
                resultado = buscarInfoEmpresas(buscarCommand, resultado.persona, mensajes, sitiosVisitados, true)
                resultado = buscarBitTools(buscarCommand, resultado.persona, mensajes, sitiosVisitados, true)
            }
            else if(buscarCommand.tieneDireccion) {
                resultado = buscarInglobalyDireccion(buscarCommand, mensajes, sitiosVisitados)
                List<Persona> personasRelacionadas = PersonaRelacionada.findAllByPrincipal(resultado?.persona[0]).collect { it.relacionada }
                if(personasRelacionadas){
                    busquedaRelacionados(resultado?.persona[0])
                }
                return resultado
            }
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            mensajes.add(e.message.split("finerror")[0])
            resultado.mensajes = mensajes
            return resultado
        }
        
        if(resultado.persona){
            def relacionadosResult = busquedaRelacionados(resultado.persona)
            resultado.persona = relacionadosResult.persona
            def sitiosVisitadosRelacionados = relacionadosResult.sitiosVisitados
            sitiosVisitadosRelacionados.each { sitio ->
                if (!resultado.sitiosVisitados.contains(sitio)) {
                    resultado.sitiosVisitados.add(sitio)
                }
            }
        }

        return resultado
    }

    Map buscarInglobaly(BuscarCommand buscarCommand, Persona persona, List<String> mensajes, List<Sitio> sitiosVisitados, Boolean esPrincipal) {
        try {
            println "\nEmpieza Inglobaly\n"
            Map resultadoInglobaly = inglobalyService.guardarInformacionIngloblalyPersonas(buscarCommand, persona)
            if (resultadoInglobaly.resultado) {
                persona = personaService.instanciarPersona(persona, resultadoInglobaly.resultado, esPrincipal)
                persona.save(flush:true, failOnError:true)
                sitiosVisitados.add(Sitio.get(resultadoInglobaly.resultado.sitioId))
            }
            if(resultadoInglobaly.mensajeError){
                mensajes.add(resultadoInglobaly.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("Inglobaly: Error guardando la persona")
        }
        finally {
            println "\nTermina Inglobaly\n"
        }
        return [persona:persona, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
    }

    Map buscarInglobalyDireccion(BuscarCommand buscarCommand, List<String> mensajes, List<Sitio> sitiosVisitados) {
        def personas
        try {
            println "\nEmpieza Inglobaly Dirección\n"
            String mensajePrint = "Buscando con los parámetros:\n" 
            Auxiliar.separador (mensajePrint + buscarCommand.parametros)
            Map resultadoInglobaly = inglobalyService.guardarInformacionIngloblalyDireccion(buscarCommand)
            if (resultadoInglobaly.resultado) {
                personas = personaService.savePersonasDireccion(resultadoInglobaly.resultado)
                sitiosVisitados.add(Sitio.get(resultadoInglobaly.resultado[0].sitioId))
            }
            
            if(resultadoInglobaly.mensajeError){
                mensajes.add(resultadoInglobaly.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("Inglobaly: Error guardando la dirección")
        }
        finally {
            println "\nTermina Inglobaly Dirección\n"
        }
        return [persona:personas, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
    }

    Map buscarAbcTelefonos(BuscarCommand buscarCommand, Persona persona, List<String> mensajes, List<Sitio> sitiosVisitados, Boolean esPrincipal) {
        try {
            println "\nEmpieza ABC Telefono\n"
            Map resultadoAbcTelefonos = abcTelefonosService.busquedaAbcTelefonos(buscarCommand, persona)
            if (resultadoAbcTelefonos.resultado) {
                persona = personaService.instanciarPersona(persona, resultadoAbcTelefonos.resultado, esPrincipal)
                persona.save(flush:true, failOnError:true)
                sitiosVisitados.add(Sitio.get(resultadoAbcTelefonos.resultado.sitioId))
            }
            if(resultadoAbcTelefonos.mensajeError){
                mensajes.add(resultadoAbcTelefonos.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("AbcTeléfonos: Error guardando la persona")
        }
        finally {
            println "\nTermina ABC Telefono\n"
            return [persona:persona, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
        }
    }

    Map buscarTelexprorer(BuscarCommand buscarCommand, Persona persona, List<String> mensajes, List<Sitio> sitiosVisitados, Boolean esPrincipal) {
        try {
            println "\nEmpieza Telexplorer\n"

            Map resultadoTelexprorer = telexplorerService.busquedaTelexplorer(buscarCommand, persona)
            if (resultadoTelexprorer.resultado) {                       
                persona = personaService.instanciarPersona(persona, resultadoTelexprorer.resultado, esPrincipal)
                persona.save(flush:true, failOnError:true)
                sitiosVisitados.add(Sitio.get(resultadoTelexprorer.resultado.sitioId))
            }
            if(resultadoTelexprorer.mensajeError){
                mensajes.add(resultadoTelexprorer.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("Telexprorer: Error guardando la persona")
        }
        finally {
            println "\nTermina Telexplorer\n"
            return [persona:persona, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
        }
    }

    Map buscarInfoEmpresas(BuscarCommand buscarCommand, Persona persona, List<String> mensajes, List<Sitio> sitiosVisitados, Boolean esPrincipal) {
        try {
            println "\nEmpieza InfoEmpresas\n"
            Map resultadoInfoEmpresas = infoEmpresasService.busquedaInfoEmpresas(buscarCommand, persona)
            if (resultadoInfoEmpresas.resultado) {                        
                persona = personaService.instanciarPersona(persona, resultadoInfoEmpresas.resultado, esPrincipal)
                persona.save(flush:true, failOnError:true)
                sitiosVisitados.add(Sitio.get(resultadoInfoEmpresas.resultado.sitioId))
            }
            if(resultadoInfoEmpresas.mensajeError){
                mensajes.add(resultadoInfoEmpresas.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("InfoEmpresas: Error guardando la persona")
        }
        finally {
            println "\nTermina InfoEmpresas\n"
            return [persona:persona, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
        }
    }

    Map buscarBitTools(BuscarCommand buscarCommand, Persona persona, List<String> mensajes, List<Sitio> sitiosVisitados, Boolean esPrincipal) {
        try {
            println "\nEmpieza Bittools\n"
            Map resultadoBittools = bittoolsService.busquedaBittools(buscarCommand, persona)
            if (resultadoBittools.resultado) {                        
                persona = personaService.instanciarPersona(persona, resultadoBittools.resultado, esPrincipal)
                persona.save(flush:true, failOnError:true)
                sitiosVisitados.add(Sitio.get(resultadoBittools.resultado.sitioId))
            }
            if(resultadoBittools.mensajeError){
                mensajes.add(resultadoBittools.mensajeError)
            }
        }
        catch(Exception e) {
            Auxiliar.printearError e
            mensajes.add("Bittools: Error guardando la persona")
        }
        finally {
            println "\nTermina Bittools\n"
            return [persona:persona, mensajes:mensajes, sitiosVisitados:sitiosVisitados]
        }
    }

    public busquedaRelacionados(Persona persona){
        List<Persona> personasRelacionadas = PersonaRelacionada.findAllByPrincipal(persona).collect { it.relacionada }
        List<String> mensajes = []
        List<Sitio> sitiosVisitados = []
        if(personasRelacionadas.size() == 0) return [persona:persona]
        personasRelacionadas.each { personaRelacionada ->
            try{
                personaRelacionada = buscarAbcTelefonos(null, personaRelacionada, mensajes, sitiosVisitados, false).persona
                personaRelacionada = buscarTelexprorer(null, personaRelacionada, mensajes, sitiosVisitados, false).persona
                personaRelacionada = buscarInfoEmpresas(null, personaRelacionada, mensajes, sitiosVisitados, false).persona
                personaRelacionada = buscarBitTools(null, personaRelacionada, mensajes, sitiosVisitados, false).persona
                personaRelacionada.save(flush:true, failOnError:true)
            }
            catch(AssertionError e) {
                Auxiliar.printearError e
                mensajes.add(e.message.split("finerror")[0])
                return
            }
        }
        persona.save(flush:true, failOnError:true)
        return [persona:persona, sitiosVisitados:sitiosVisitados]
    }
    
    public plantillaExcelBusqueda(){
        try{
            Workbook workbook = new XSSFWorkbook()
            CellStyle style = workbook.createCellStyle();
            CellStyle style2 = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            def sheet = workbook.createSheet("busquedaEjemplo")
            def headerRow = sheet.createRow(0)
            def cellIndex = 0
            style2.setWrapText(true);
            headerRow.setRowStyle(style2);
            def propertyNames = ['primerNombre', 'segundoNombre', 'apellidoPadre', 'apellidoMadre', 'documento', 'provincia', 'poblacion', 'calle', 'numero', 'bloque', 'portal', 'escalera', 'puerta', 'piso', 'codigoPostal']
            def customHeaders = [
                'primerNombre': 'Primer Nombre',
                'segundoNombre': 'Segundo Nombre',
                'apellidoPadre': 'Apellido Padre',
                'apellidoMadre': 'Apellido Madre',
                'documento': 'Documento/N.I.F',
                'provincia' : 'Provincia',
                'poblacion' : 'Población',
                'calle' : 'Calle',
                'numero' : 'Número',
                'bloque' : 'Bloque',
                'portal' : 'Portal',
                'escalera' : 'Escalera',
                'puerta' : 'Puerta',
                'piso' : 'Piso',
                'codigoPostal' : 'Código Postal'
            ]
            propertyNames.each { attributeName ->
                def columnHeader = customHeaders[attributeName] ?: formatearCamelcase(attributeName)
                def headerCell = headerRow.createCell(cellIndex++)
                headerCell.setCellValue(columnHeader)
                headerCell.setCellStyle(style)
                sheet.autoSizeColumn(cellIndex - 1)
            }
            sheet.setColumnWidth(5, 30 * 256)
            sheet.setColumnWidth(6, 30 * 256)
            def rowNum = 1 
            def cellNum = 0
            def rowNumPobl = 1 
            def cellNumPobl = 2
            def provincias = domicilioService.listProvincias()
            provincias.sort { it.nombre }
            def hiddenProvinciasSheet = workbook.createSheet("ProvinciasHidden")
            def columnCount = 3
            provincias.eachWithIndex {provincia, index ->
                columnCount += 2              
                def row = hiddenProvinciasSheet.createRow(index)
                def cell = row.createCell(0)
                cell.setCellValue(provincia.nombre)
            }
            //Tengo qe hacer otro each porque si uso el mismo no escribe bien las poblaciones
            provincias.eachWithIndex { provincia, index -> 
                def provinciaColumnIndex = index + 1 
                def firstRow = hiddenProvinciasSheet.getRow(0) ?: hiddenProvinciasSheet.createRow(0)
                    def cellHeader = firstRow.createCell(cellNumPobl + provinciaColumnIndex)
                    cellHeader.setCellValue("Provincia " + (index + 1))
                    def poblaciones = domicilioService.listPoblacionesByProvincia(provincia.id)
                    
                    poblaciones.eachWithIndex { poblacion, poblacionIndex ->
                        def rowPoblacion = hiddenProvinciasSheet.getRow(rowNumPobl + poblacionIndex) ?: hiddenProvinciasSheet.createRow(rowNumPobl + poblacionIndex)
                        def newCell = rowPoblacion.createCell(cellNumPobl + provinciaColumnIndex)
                        newCell.setCellValue(poblacion.nombre)
                    }
            }
            workbook.setSheetHidden(workbook.getSheetIndex("ProvinciasHidden"), Workbook.SHEET_STATE_VERY_HIDDEN)
            XSSFName name1 = workbook.createName();
            name1.setNameName("provincias");
            name1.setRefersToFormula("ProvinciasHidden!\$A\$1:\$A\$53");
            XSSFName name2 = workbook.createName();
            name2.setNameName("poblaciones");
            name2.setRefersToFormula("ProvinciasHidden!\$D\$2:\$BD\$773");

            def validationHelper = new XSSFDataValidationHelper(sheet)
            def provinciaRange = new CellRangeAddressList(1, 200, 5, 5)
            def provinciaConstraint = validationHelper.createFormulaListConstraint("provincias")
            def provinciaValidation = validationHelper.createValidation(provinciaConstraint, provinciaRange)
            provinciaValidation.setShowErrorBox(true)
            sheet.addValidationData(provinciaValidation)
        
            def poblacionRange = new CellRangeAddressList(1, 200, 6, 6)
            def poblacionConstraintFormula = "INDEX(poblaciones,,MATCH(F2,provincias,0))"
            def poblacionConstraint = validationHelper.createFormulaListConstraint(poblacionConstraintFormula)
            def poblacionValidation = validationHelper.createValidation(poblacionConstraint, poblacionRange)
            poblacionValidation.setShowErrorBox(true)
            sheet.addValidationData(poblacionValidation)     
            String pathExcels = System.getProperty("user.home") + "/" + grailsApplication.config.getProperty("carpetaArchivos") + "/busquedaExcel/"
            File carpeta = new File(pathExcels)
            if (!carpeta.exists()) carpeta.mkdirs()
            FileOutputStream outputStream = new FileOutputStream(pathExcels + "plantillaBusqueda" + ".xlsx")
            workbook.write(outputStream)
            outputStream.close()
            return new File(pathExcels + "plantillaBusqueda" + ".xlsx")
            } catch(Exception ex){
            Auxiliar.printearError(ex)
        }
    }


    public escribirExcelBusqueda(Long busquedaId) {escribirExcelBusqueda(Busqueda.get(busquedaId))}
    public escribirExcelBusqueda(Persona persona) {escribirExcelBusqueda(persona.busqueda)}
    public escribirExcelBusqueda(Busqueda busqueda) {
        assert busqueda: "No se encontró la búsqueda."
        try{
            Workbook workbook = new XSSFWorkbook()
            CellStyle style = workbook.createCellStyle();
            CellStyle style2 = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            def sheet = workbook.createSheet("resultadosBusqueda")
            def propertyNames = ['primerNombre', 'segundoNombre', 'apellidoPadre', 'apellidoMadre', 'anoNacimiento', 'documento', 'poblacionNacimiento', 'provinciaNacimiento', 'autonomo', 'empresa', 'domicilios', 'telefonos', 'personasRelacionadas']
            def formatearCamelcase = { attributeName ->
                attributeName.replaceAll(/(?<=[a-z])(?=[A-Z])/, ' ').capitalize()
            }
            def getProperty = { persona, attributeName ->
                switch (attributeName) {
                    case 'poblacionNacimiento':
                        persona.poblacionNacimiento ? persona.poblacionNacimiento.nombre : ''
                        break
                    case 'provinciaNacimiento':
                        if (persona.poblacionNacimiento) {
                            def provinciaNacimiento = Provincia.findByNombre(persona.poblacionNacimiento.provincia.nombre.toUpperCase())
                            provinciaNacimiento ? provinciaNacimiento.nombre : ''
                        } else {
                            ''
                        }
                        break
                    case 'personasRelacionadas':
                        busqueda.personaPrincipal!= persona ? persona.getTipoRelacion(busqueda.personaPrincipal) : ''
                        break
                    case 'domicilios':
                        persona.domicilios.size() != 0 ? persona.unirDomicilios() : ''
                        break
                    case 'telefonos':
                        persona.telefonos.size() != 0 ? persona.unirTelefonos() : ''
                        break
                    case 'autonomo':
                        persona.autonomo ? "SI" : "NO"
                        break
                    default:
                        def attributeValue = persona."$attributeName"
                        attributeValue instanceof Collection || attributeValue instanceof Map ? attributeValue.toString() : attributeValue as String
                }
            }
            def headerRow = sheet.createRow(0)
            def cellIndex = 0
            def customHeaders = [
                'primerNombre': 'Primer Nombre',
                'segundoNombre': 'Segundo Nombre',
                'apellidoPadre': 'Apellido Padre',
                'apellidoMadre': 'Apellido Madre',
                'anoNacimiento': 'Año de Nacimiento',
                'documento': 'Documento/N.I.F',
                'poblacionNacimiento': 'Población Nacimiento',
                'provinciaNacimiento': 'Provincia Nacimiento',
                'autonomo': 'Autónomo',
                'empresa': 'Sociedad',
                'domicilios': 'Domicilios',
                'telefonos': 'Teléfonos',
                'personasRelacionadas': 'Tipo Relación'
            ]
            for (String attributeName : propertyNames) {
                def columnHeader = customHeaders[attributeName] ?: formatearCamelcase(attributeName)
                def headerCell = headerRow.createCell(cellIndex++)
                headerCell.setCellValue(columnHeader)
                headerCell.setCellStyle(style)
                sheet.autoSizeColumn(cellIndex - 1)
            }
            //TODO, ver este sort que onda en el excel --- No estoy seguro si funciona lo que hice pero para mi que tiene sentido :p
            def personas = busqueda.resultados
            def personaPrincipal = busqueda.personaPrincipal
            if (personaPrincipal) {
                // Mueve la persona principal al principio de la lista
                personas.remove(personaPrincipal)
                personas = [personaPrincipal] + personas
            }
            personas.eachWithIndex { persona, i ->
                def dataRow  = sheet.createRow( i + 1 )
                dataRow.setHeightInPoints(50)
                cellIndex = 0
                propertyNames.eachWithIndex { attributeName, columna ->
                    def dataCell = dataRow.createCell(cellIndex++)
                    dataCell.setCellValue(getProperty(persona, attributeName))
                    sheet.autoSizeColumn(columna)
                    style2.setWrapText(true);
                    dataRow.setRowStyle(style2);
                    dataRow.getCell(0).setCellStyle(style2);
                }
            }
            String pathExcels = System.getProperty("user.home") + "/" + grailsApplication.config.getProperty("carpetaArchivos") + "/busquedaExcel/"
            File carpeta = new File(pathExcels)
            if (!carpeta.exists()) carpeta.mkdirs()
            FileOutputStream outputStream = new FileOutputStream(pathExcels + "resultados_" + busqueda.id + ".xlsx")
            workbook.write(outputStream)
            outputStream.close()
            return new File(pathExcels + "resultados_" + busqueda.id + ".xlsx")
        } catch(Exception ex){
            Auxiliar.printearError(ex)
        }
    }

    private obtenerExcel(archivo, Boolean errorEsAssertion){
		InputStream targetStream
		String nombre
		try {
			targetStream = archivo.getInputStream()
			nombre = archivo.getOriginalFilename()
		}
		catch(Exception e) {
			targetStream = new FileInputStream(archivo);
			nombre = archivo.name
		}	
            switch (nombre.substring(nombre.lastIndexOf(".") + 1,
				nombre.length())) {
			case "xls":
				println "Detectamos que es la versión de excel vieja."
				return new HSSFWorkbook(targetStream)
			case "xlsx":
				println "Detectamos que es la versión de excel nueva."
				return new XSSFWorkbook(targetStream)
			default:
				if (errorEsAssertion)
					assert false : "El archivo ingresado no es un Excel.finerror"
				else
					throw new Exception("formato")
		}
	}

    def obtenerValorCelda(fila, celda, Boolean forzarLong = true) { obtenerValorCelda(fila.getCell(celda), forzarLong) }
	def obtenerValorCelda(celda, Boolean forzarLong = true) {
		def valor
		try{
			valor = celda.getStringCellValue()
		}
		catch(IllegalStateException e){
			valor = (Double) celda.getNumericCellValue()
			if(valor % 1 == 0 || forzarLong){
				if(valor instanceof Double)
					valor = Double.valueOf(valor).longValue()
				else
					valor = new Long(valor)
			}
		}
		catch(NullPointerException e){
			valor = ""
		}

		return valor
	}
    def isRowEmpty(row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            if (obtenerValorCelda(row.getCell(i)).trim() != "") {
                return false
            }
        }
        return true
    }

    def generarBuscarCommandsExcel(archivo) {
        def sheet = obtenerExcel(archivo, true).getSheetAt(0)
        def rows = sheet.rowIterator()
        def buscarCommands = []
        def rowIndex = 0
        rows.next()
        while (rows.hasNext()) {
            rowIndex++
            def fila = rows.next()
            if (isRowEmpty(fila)) {            
                    break
            }
            def buscarCommand = new BuscarCommand()
            try {   
                buscarCommand.domicilio = new DomicilioCommand() 
                buscarCommand.primerNombre = obtenerValorCelda(fila.getCell(0)) ?: null
                buscarCommand.segundoNombre = obtenerValorCelda(fila.getCell(1)) ?: null
                buscarCommand.apellidoPadre = obtenerValorCelda(fila.getCell(2)) ?: null
                buscarCommand.apellidoMadre = obtenerValorCelda(fila.getCell(3)) ?: null
                buscarCommand.documento = obtenerValorCelda(fila.getCell(4)) ?: null
                Provincia provincia = Provincia.findByNombre(obtenerValorCelda(fila.getCell(5)).toUpperCase())
                Poblacion poblacion = Poblacion.findByNombre(obtenerValorCelda(fila.getCell(6)).toUpperCase())
                buscarCommand.domicilio.provinciaId = Provincia.findByNombre(obtenerValorCelda(fila.getCell(5)).toUpperCase())?.id
                buscarCommand.domicilio.poblacionId = Poblacion.findByNombre(obtenerValorCelda(fila.getCell(6)).toUpperCase())?.id
                buscarCommand.domicilio.calle = obtenerValorCelda(fila.getCell(7)) ?: null
                def numeroValue = obtenerValorCelda(fila.getCell(8))
                buscarCommand.domicilio.numero = numeroValue ? numeroValue.toInteger() : null
                buscarCommand.domicilio.bloque = obtenerValorCelda(fila.getCell(9)) ?: null
                buscarCommand.domicilio.portal = obtenerValorCelda(fila.getCell(10)) ?: null
                buscarCommand.domicilio.escalera = obtenerValorCelda(fila.getCell(11)) ?: null
                buscarCommand.domicilio.puerta = obtenerValorCelda(fila.getCell(12)) ?: null
                buscarCommand.domicilio.piso = obtenerValorCelda(fila.getCell(13)) ?: null
                buscarCommand.domicilio.codigoPostal = obtenerValorCelda(fila.getCell(14)) ?: null
                buscarCommands.add(buscarCommand)
            } catch (Exception e) {
                Auxiliar.printearError(e, "Error generando BuscarCommand en row: ${buscarCommands.size() + 1}")
            }
        }
        return buscarCommands
    }

    def guardarPersonaBusqueda(Busqueda busqueda, Persona persona){
        if(PersonaBusqueda.findByBusquedaAndPersona(busqueda,persona)) return
        PersonaBusqueda personaBusq = new PersonaBusqueda()
        personaBusq.busqueda = busqueda
        personaBusq.persona = persona
        personaBusq.save(flush:true, failOnError:true)
    }
}
