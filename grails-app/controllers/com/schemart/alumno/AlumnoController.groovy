package com.schemart.alumno

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode
import com.schemart.Auxiliar


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class AlumnoController {
	def accessRulesService
	def alumnoService

	def index() {
		redirect (action: 'list')
	}

	def list(){

	}

	def save(AlumnoCommand command, String disponibilidades){
		try{
			alumnoService.saveAlumno(command, disponibilidades)
			flash.message = "Guardado exitoso"
			redirect(action: 'list')
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error guardando alumno"
			render(view: 'create', model:[command:command])
		}
	}

	def create() {  
	}
	
	def edit(Long id) {
		def alumno = alumnoService.getAlumnoCommand(id)
		def alumnoIdiomas = alumno?.idiomas?.join(',')
		render(view: "edit", model:[command: alumno, idiomas:alumnoIdiomas])  
	}
	
	def update(AlumnoCommand command, String disponibilidades) {  
		try{
			alumnoService.updateAlumno(command, disponibilidades)
			flash.message = "Guardado exitoso"
			redirect(action: 'list')
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error guardando alumno"
			def alumnoIdiomas = command?.idiomas?.join(',')
			render(view: 'edit', model:[command:command, idiomas:alumnoIdiomas])
		}
	}

	def delete(Long id){
		try{
			alumnoService.deleteAlumno(id)
			flash.message = "Borrado exitoso"
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error borrando alumno"
		}
		redirect(action: 'list')
	}

	def ajaxGetAlumnos(){
		render alumnoService.listAlumnos() as JSON
	}

    def ajaxGetTiposCursos(){
		render alumnoService.listTiposCursos() as JSON
	}
}
