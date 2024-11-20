package com.schemart.empleado

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
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

	def save(EmpleadoCommand command, String disponibilidades){
		try{
			empleadoService.saveEmpleado(command, disponibilidades)
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
		def empleadoIdiomas = empleado?.idiomas?.join(',')
		render(view: "edit", model:[command: empleado, idiomas:empleadoIdiomas])
	}
	
	def update(EmpleadoCommand command, String disponibilidades) {  
		try{
			empleadoService.updateEmpleado(command, disponibilidades)
			flash.message = "Guardado exitoso"
			redirect(action: 'list')
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error guardando empleado"
			def empleadoIdiomas = command?.idiomas?.join(',')
			render(view: "edit", model:[command: command, idiomas:empleadoIdiomas])
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
	
	def ajaxGetDocentes(){
		def docentes = empleadoService.listDocentes()
		docentes.add([
			id: 0,
			nombreApellido: 'Todos'
		])
		render docentes as JSON
	}

	def ajaxGetDocentesDisponibles(String fecha, String inicio, String fin, Long idiomaId, Long claseId){
		def docentes = empleadoService.listDocentesDisponibles(fecha, inicio, fin, idiomaId, claseId)
		render docentes as JSON
	}
}
