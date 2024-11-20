package com.schemart.clase

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.Auxiliar

@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class ClaseController {
	def accessRulesService
	def claseService

	def index() {
		redirect (action: 'list')
	}

	def list(){
	}

	def create(){
		
	}

	def getClaseCommand(Long id){
		render claseService.getClaseCommand() as JSON
	}

	def save(ClaseCommand command){
		try{
			claseService.saveClase(command)
			flash.message = "Guardado exitoso"
		}
		catch(Exception e){
			Auxiliar.printearError e 
			flash.error = "Error agendando clase"
		}
		finally{
			redirect(action: 'list')
		}
	}

	def ajaxListClases(){
		render claseService.listClases() as JSON
	}

	def ajaxListClasesByDocente(Long docenteId){
		render claseService.listClasesByDocente(docenteId) as JSON
	}

	def edit(Long id){
		def claseCommand = claseService.getClaseCommand(id)
		render(view:"edit", model:[command:claseCommand])
	}

	def update(ClaseCommand command){
		try{
			claseService.updateClase(command)
			flash.message = "Guardado exitoso"
		}
		catch(Exception e){
			Auxiliar.printearError e 
			flash.error = "Error modificando la clase"
		}
		finally{
			redirect(action: 'list')
		}
	}

	def ajaxGetAlumnosByClase(Long claseId){
		def alumnos = claseService.getAlumnosByClase(claseId)
		render alumnos as JSON
	}

	def delete(Long id){
		try{
			claseService.deleteClase(id)
			flash.message = "Borrado exitoso"
		}
		catch(Exception e){
			Auxiliar.printearError e
			flash.error = "Error borrando clase"
		}
		finally{
			redirect(action: 'list')
		}
	}
}
