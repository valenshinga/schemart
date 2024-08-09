package com.schemart.empleado

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode
import com.schemart.Auxiliar


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class EmpleadoController {
	def accessRulesService
	def empleadoService

	def index() {
		redirect (action: 'list')
	}

	def list(){

	}

	def save(EmpleadoCommand command){
		try{
			empleadoService.saveEmpleado(command)
			flash.message = "Guardado exitoso"
			redirect(action: 'list')
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error guardando empleado"
			render(view: 'create', model:[command:command])
		}
	}

	def create() {  
	}
	
	def edit(Long id) {
		def empleado = empleadoService.getEmpleadoCommand(id)
		render(view: "edit", model:[command: empleado])  
	}
	
	def update(EmpleadoCommand command) {  
		try{
			empleadoService.updateEmpleado(command)
			flash.message = "Guardado exitoso"
			redirect(action: 'list')
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error guardando empleado"
			render(view: 'edit', model:[command:command])
		}
	}

	def delete(Long id){
		try{
			empleadoService.deleteEmpleado(id)
			flash.message = "Borrado exitoso"
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error borrando empleado"
		}
		redirect(action: 'list')
	}

	def ajaxGetEmpleados(){
		render empleadoService.listEmpleados() as JSON
	}
}
