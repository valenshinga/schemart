package com.schemart.disponibilidad

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.Auxiliar


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class DisponibilidadController {
	def accessRulesService
	def disponibilidadService

	def ajaxGetDisponibilidad(Boolean esAlumno, Long id){
		def salida 
		if (esAlumno){
			salida = disponibilidadService.listDisponibilidadesAlumno(id)
		} 
		else
			salida = disponibilidadService.listDisponibilidadesDocente(id)
		render salida as JSON
	}

	def ajaxSaveDisponibilidad(String desde, String hasta, String dia, Long personaId, Boolean esDocente){
		render disponibilidadService.saveDisponibilidad(desde,hasta,dia,personaId,esDocente) as JSON
	}

	def ajaxUpdateDisponibilidad(Long id, String desde, String hasta, String dia, Boolean esDocente){
		render disponibilidadService.updateDisponibilidad(id,desde,hasta,dia,esDocente) as JSON
	}

	def ajaxDeleteDisponibilidad(Long id){
		disponibilidadService.deleteDisponibilidad(id)
		def salida = [:]
		salida.mensaje = 'ok'
		render salida as JSON
	}
}
