package com.tecnofind.persona

import com.tecnofind.AccessRulesService
import com.tecnofind.Auxiliar
import com.tecnofind.busqueda.BusquedaService
import com.tecnofind.domicilio.DomicilioService
import java.util.List 
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.Poblacion
import groovy.json.JsonSlurper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_USER'])
class PersonaController {
    
    def personaService
    def domicilioService
    def accessRulesService
    def busquedaService

    def index() {
        redirect (action: 'list')
    }

    def list() {} 

    def edit(Long id) {
        PersonaCommand personaCommand = new PersonaCommand()
        personaCommand = personaService.getPersonaCommand(id)
        [personaCommand: personaCommand, personaPoblacionNacimiento: domicilioService.getPoblacion(personaCommand.poblacionNacimientoId)]
    }

    def update(PersonaCommand personaCommand, String datosForm) {
        try{
            personaCommand.poblacionNacimientoId = params.poblacionNacimientoId?.toInteger()
            def datosFormulario = new JsonSlurper().parseText(datosForm) 
            def telefonosList = datosFormulario.telefonos
            def domicilioList = datosFormulario.domicilios
            def personasRelacionadasList = datosFormulario.personasRelacionadas
            personaService.updatePersona(personaCommand)
            personaService.updateTelefonos(personaCommand, telefonosList)
            personaService.updatePersonasRelacionadas(personaCommand, personasRelacionadasList)
            domicilioService.updateDomiciliosPersona(personaCommand, domicilioList)
            flash.message = "Persona guardada correctamente"
            redirect (action: "show", params: [id: personaCommand.id])
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            flash.error = e.message.split("finerror")[0]
            render (view: "edit", model: [personaCommand: personaCommand, personaPoblacionNacimiento: domicilioService.getPoblacion(personaCommand.poblacionNacimientoId)?.nombre])
        }
        catch(Exception e){
            flash.error = "Error al guardar la persona"
            Auxiliar.printearError e
            render (view: "edit", model: [personaCommand: personaCommand, personaPoblacionNacimiento: domicilioService.getPoblacion(personaCommand.poblacionNacimientoId)?.nombre])
        }
    }

    def show(long id){
        [persona: personaService.getPersona(id),
        domicilioList: domicilioService.getDomiciliosByPersona(id),
        telefonosList: personaService.listTelefonosByPersona(id)]
    }

    def ajaxGetPersonas() {
        def userOficina = accessRulesService.isSuperAdmin() ? null : accessRulesService.currentUser.oficina?.id 
        def personas = personaService.listPersonas(userOficina)
        render personas as JSON
    }

    def ajaxGetPersonasPorBusqueda(Long id) {
        def busquedas = personaService.listPersonasPorBusqueda(id)
        render busquedas as JSON
    }

    def ajaxGetTelefonosByPersona(Long id) {
        def telefonos = personaService.listTelefonosByPersona(id)
        render telefonos as JSON
    }

    def ajaxGetPersonasRelacionadas(Long id) {   
        def personas = personaService.listPersonaRelacionadas(id)
        render personas as JSON
    }

    def ajaxGetTelefono(Long id) {   
        def telefono = personaService.getTelefono(id)
        render telefono as JSON
    }

    def ajaxGetPersonaRelacionada(Long id) {   
        def personaRelacionada = personaService.getPersonaRelacionada(id)
        render personaRelacionada as JSON
    }

    def ajaxDescargarExcelBusqueda(Long id){
        try{
            def excel = busquedaService.escribirExcelBusqueda(personaService.getPersona(id))
            def fileInputStream = new FileInputStream(excel)

			response.setContentType("APPLICATION/OCTET-STREAM")
			response.setHeader("Content-Disposition", "Attachment;Filename=\"${excel.getName()}\"")
			
			def outputStream = response.getOutputStream()
			byte[] buffer = new byte[4096];
			int len;
			while ((len = fileInputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}

			outputStream.flush()
			outputStream.close()
			fileInputStream.close()
            excel.delete()
        }
        catch(Exception e){
            flash.message = e.message
            Auxiliar.printearError(e)
        }
    }

    def delete(Long id){ 
        try{
            personaService.delete(id)
            flash.message = "Persona borrada correctamente"
        }
        catch(Exception e){
            flash.error = "Error al borrar a la persona"
            Auxiliar.printearError e
        } 
        finally {
            redirect (action: 'list')
        }
        
    }

}