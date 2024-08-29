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
}
