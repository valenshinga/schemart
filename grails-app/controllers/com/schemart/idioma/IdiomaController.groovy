package com.schemart.idioma

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode
import com.schemart.Auxiliar


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class IdiomaController {
	def accessRulesService
	def idiomaService

	def index() {
		redirect (action: 'list')
	}

	def list(){

	}

	def ajaxSaveIdioma(IdiomaCommand command){
		def salida = [:]
		try{
			idiomaService.saveIdioma(command)
			salida.mensaje = "Guardado exitoso"
			salida.error = false
		}
		catch(Exception e){
			Auxiliar.printearError e
			salida.error = true 
			salida.mensaje = "Error guardando idioma"
		}
		finally{
			render salida as JSON
		}
	}

	def ajaxUpdateIdioma(IdiomaCommand command) {
		def salida = [:]
		try{
			idiomaService.updateIdioma(command)
			salida.mensaje = "Guardado exitoso"
			salida.error = false
		}
		catch(Exception e){
			Auxiliar.printearError e
			salida.error = true 
			salida.mensaje = "Error guardando idioma"
		}
		finally{
			render salida as JSON
		}
	}

	def ajaxDeleteIdioma(Long id){
		def salida = [:]
		try{
			idiomaService.deleteIdioma(id)
			salida.mensaje = "Borrado exitoso"
			salida.error = false
		}
		catch(Exception e){
			Auxiliar.printearError e
			salida.mensaje = "Error borrando idioma"
			salida.error = false
		}
		finally{
			render salida as JSON
		}
	}

    def ajaxGetIdiomas(){
        render idiomaService.listIdiomas() as JSON
    }
}
