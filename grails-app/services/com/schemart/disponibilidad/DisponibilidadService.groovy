package com.schemart.disponibilidad

import grails.transaction.Transactional
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import com.schemart.Auxiliar

@Transactional
class DisponibilidadService {

	def listDisponibilidadesAlumno(Long id) {
		def disponibilidades = Disponibilidad.createCriteria().list {
			alumno {
				eq("id", id)
			}
		}
		return disponibilidades
	}

	def listDisponibilidadesDocente(Long id){
		def disponibilidades = Disponibilidad.createCriteria().list {
			docente {
				eq("id", id)
			}
		}
		return disponibilidades  
	}

	def saveDisponibilidad(disponibilidad) {
		Disponibilidad disponibilidadInstance = new Disponibilidad()
		def timeDesde = Auxiliar.separarTiempo(disponibilidad.desde)
		def timehasta = Auxiliar.separarTiempo(disponibilidad.hasta)
		disponibilidadInstance.dia = disponibilidad.dia
		disponibilidadInstance.desde = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		disponibilidadInstance.hasta = new LocalTime(timehasta.hora.toInteger(), timehasta.minuto.toInteger())
		return disponibilidadInstance.save(flush:true, failOnError:true)
	}
	
	def updateDisponibilidad(disponibilidad){
		if (disponibilidad.id.getClass() == String)
			disponibilidad.id = disponibilidad.id.toInteger()
		Disponibilidad disponibilidadInstance = Disponibilidad.get(disponibilidad.id.longValue())
		def timeDesde = Auxiliar.separarTiempo(disponibilidad.desde)
		def timeHasta = Auxiliar.separarTiempo(disponibilidad.hasta)
		disponibilidadInstance?.dia = disponibilidad?.dia
		disponibilidadInstance?.desde = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		disponibilidadInstance?.hasta = new LocalTime(timeHasta.hora.toInteger(), timeHasta.minuto.toInteger())
		return disponibilidadInstance.save(flush:true,failOnError:true)
	}

	def deleteDisponibilidad(Long id){
		Disponibilidad disponibilidadInstance = Disponibilidad.get(id)
		return disponibilidadInstance.delete()
	}
}