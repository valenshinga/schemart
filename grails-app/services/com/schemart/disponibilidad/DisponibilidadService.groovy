package com.schemart.disponibilidad

import grails.transaction.Transactional
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import com.schemart.Auxiliar
import org.hibernate.SessionFactory;

import com.schemart.alumno.Alumno
import com.schemart.empleado.Empleado

@Transactional
class DisponibilidadService {
	SessionFactory sessionFactory

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

	def saveDisponibilidad(String desde, String hasta, String dia, Long personaId, Boolean esDocente) {
		Disponibilidad disponibilidadInstance = new Disponibilidad()
		def timeDesde = Auxiliar.separarTiempo(desde)
		def timehasta = Auxiliar.separarTiempo(hasta)
		disponibilidadInstance.dia = dia
		disponibilidadInstance.desde = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		disponibilidadInstance.hasta = new LocalTime(timehasta.hora.toInteger(), timehasta.minuto.toInteger())
		disponibilidadInstance.save(flush:true, failOnError:true)
		if (esDocente){
			Empleado empleadoInstance = Empleado.get(personaId)
			empleadoInstance.addToDisponibilidad(disponibilidadInstance)
			empleadoInstance.save(flush:true, failOnError:true)
		} else {
			Alumno alumnoInstance = Alumno.get(personaId)
			alumnoInstance.addToDisponibilidad(disponibilidadInstance)
			alumnoInstance.save(flush:true, failOnError:true)	
		}

		return disponibilidadInstance
	}

	def updateDisponibilidad(Long id, String desde, String hasta, String dia, Boolean esDocente){
		Disponibilidad disponibilidadInstance = Disponibilidad.get(id)
		def timeDesde = Auxiliar.separarTiempo(desde)
		def timeHasta = Auxiliar.separarTiempo(hasta)
		disponibilidadInstance?.dia = dia
		disponibilidadInstance?.desde = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		disponibilidadInstance?.hasta = new LocalTime(timeHasta.hora.toInteger(), timeHasta.minuto.toInteger())
		return disponibilidadInstance.save(flush:true,failOnError:true)
	}
	
	def deleteDisponibilidad(Long id){
		Disponibilidad disponibilidadInstance = Disponibilidad.get(id)
		return disponibilidadInstance.delete()
	}
}