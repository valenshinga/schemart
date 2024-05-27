package com.tecnofind.busqueda

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.Auxiliar
import com.tecnofind.persona.Persona
import com.tecnofind.persona.PersonaCommand
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.Telefono
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.Poblacion
import com.tecnofind.domicilio.Provincia
import com.tecnofind.Estado
import com.tecnofind.AccessRulesService
import com.tecnofind.busqueda.BuscarCommand
import com.tecnofind.notificacion.NotificacionService
import org.springframework.web.multipart.MultipartFile

@Secured(['ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUPER_ADMIN'])
class BusquedaController {
    def accessRulesService
    def busquedaService
    def notificacionService

    def index() {
        redirect (action: 'create')
    }

    def list() {

    }

    def create() {}

    def show(Long id) {
        [busqueda: busquedaService.getBusqueda(id)]
    }

    def buscar(BuscarCommand buscarCommand){
        Map resultado = [:]
        try{
            def validacion = busquedaService.validarBusqueda(buscarCommand)

            if (validacion.error) {
                resultado.mensaje = validacion.message
                resultado.invalido = true
                render resultado as JSON
            }
            render resultado as JSON

            if (!validacion.error){
                Map resultadosBusqueda = busquedaService.nuevaBusqueda(buscarCommand) 
                def busquedaInstanciada = busquedaService.generarInstanciaBusqueda(buscarCommand) 
                busquedaService.busquedaSave(busquedaInstanciada, resultadosBusqueda)
            }
        }
        catch(Exception e){
            Auxiliar.printearError e
            try{
                def user = accessRulesService.getCurrentUser()
                def inputs = buscarCommand.inputs
                notificacionService.enviarMailBusquedaFallida(user.username, inputs)
            }catch(Exception p){
                Auxiliar.printearError(p)
            }
        }
    }

    def ajaxGetTiposDatos() {
        def tiposDatos = busquedaService.listTiposDatos()
        render tiposDatos as JSON
    }
    
    def ajaxGetBusquedas() {
       def busquedas = busquedaService.listBusqueda()
       render busquedas as JSON        
    }

    def ajaxDescargarExcelBusqueda(Long id){
        try{
            def excel = busquedaService.escribirExcelBusqueda(id)
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

    def ajaxDescargarPlantillaExcel(){
        try{
            def excel = busquedaService.plantillaExcelBusqueda()
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

    def busquedaConExcel() {
        def archivo = request.getFile('files')
        try {
            if (archivo) {
                def buscarCommands = busquedaService.generarBuscarCommandsExcel(archivo)
                buscarCommands.each { buscarCommand -> 
                    buscar(buscarCommand)
                }
            } else {
                render "No se subio ningun archivo"
            }
        } catch (Exception e) {
            flash.message = e.message
            Auxiliar.printearError(e)
        }
    }

}
