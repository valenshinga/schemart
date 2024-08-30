package com.schemart.empleado

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate
import groovy.json.JsonSlurper
import com.schemart.idioma.Idioma
import com.schemart.disponibilidad.DisponibilidadService


@Transactional
class EmpleadoService {
	def disponibilidadService

	def listEmpleados(){
		return Empleado.list()
	}

	def getEmpleadoCommand(Long id){
		def empleadoInstance = Empleado.get(id)
		def command = new EmpleadoCommand()
		
		def calle = ''
		def numero = ''
		if(empleadoInstance.domicilio){
			def pattern = ~/^(.*?)(\d+)$/
			def matcher = pattern.matcher(empleadoInstance.domicilio)
			if (matcher.matches()) {
				calle = matcher.group(1).trim()
				numero = matcher.group(2).trim()
			}
		}
		command.id = empleadoInstance.id
		command.nombre = empleadoInstance.nombre
		command.apellido = empleadoInstance.apellido
		command.dni = empleadoInstance.dni
		command.cuit = empleadoInstance.cuit
		command.calle = calle
		command.numero = numero
		command.email = empleadoInstance.email
		command.telefono = empleadoInstance.telefono
		command.fechaNacimiento = empleadoInstance.fechaNacimiento
		command.cargo = empleadoInstance.cargo
		command.estadoId = empleadoInstance.estado.id
		command.idiomas = empleadoInstance.idiomas.id as List

		return command
	}

	def saveEmpleado(EmpleadoCommand command, String disponibilidades = null) {
		Empleado empleadoInstance = new Empleado()
		empleadoInstance.nombre = command.nombre
		empleadoInstance.apellido = command.apellido
		empleadoInstance.dni = command.dni
		empleadoInstance.cuit = command.cuit
		empleadoInstance.domicilio = "$command.calle $command.numero"
		empleadoInstance.email = command.email
		empleadoInstance.telefono = command.telefono
		empleadoInstance.fechaNacimiento = LocalDate.parse(command.fechaNacimiento)
		empleadoInstance.cargo = command.cargo
		empleadoInstance.estado = Estado.findByNombre('Activo')

		empleadoInstance.disponibilidad = []
		empleadoInstance.idiomas = []
		empleadoInstance = empleadoInstance.save(flush: true, failOnError: true)

		def disponibilidadesList = disponibilidades ? new JsonSlurper().parseText(disponibilidades) : []
		disponibilidadesList.each { disponibilidad ->
			def disponibilidadGuardada = disponibilidadService.saveDisponibilidad(disponibilidad)
			empleadoInstance.addToDisponibilidad(disponibilidadGuardada)
		}

		command.idiomas.each { id ->
			empleadoInstance.addToIdiomas(Idioma.get(id))
		}

		empleadoInstance.save(flush:true, failOnError:true)
	}

	def updateEmpleado(EmpleadoCommand command, String disponibilidades = null) {
		Empleado empleadoInstance = Empleado.get(command.id)
		empleadoInstance.nombre = command.nombre
		empleadoInstance.apellido = command.apellido
		empleadoInstance.dni = command.dni
		empleadoInstance.cuit = command.cuit
		empleadoInstance.domicilio = "$command.calle $command.numero"
		empleadoInstance.email = command.email
		empleadoInstance.telefono = command.telefono
		empleadoInstance.fechaNacimiento = LocalDate.parse(command.fechaNacimiento)
		empleadoInstance.cargo = command.cargo
		empleadoInstance.estado = Estado.get(command.estadoId)
      	def disponibilidadesList = new JsonSlurper().parseText(disponibilidades)
		if (disponibilidadesList != []){
			def disponibilidadesRecibidasConId = disponibilidadesList.findAll { it.id != null }*.id
			def disponibilidadesAEliminar = []
			empleadoInstance.disponibilidad.each { disponibilidad ->
				if (!disponibilidadesRecibidasConId.contains(disponibilidad.id.toInteger())){
					disponibilidadesAEliminar << disponibilidad
				}
			}

			if (disponibilidadesAEliminar) {
				disponibilidadesAEliminar.each { disponibilidad ->
					empleadoInstance.removeFromDisponibilidad(disponibilidad)
					disponibilidadService.deleteDisponibilidad(disponibilidad.id)
				}
			}

			disponibilidadesList.each { disponibilidadData ->
				if (disponibilidadData.id) {
					def disponibilidadExistente = disponibilidadService.updateDisponibilidad(disponibilidadData)
					if (!empleadoInstance.disponibilidad.contains(disponibilidadExistente)) {
						empleadoInstance.addToDisponibilidad(disponibilidadExistente)
					}
				} else {
					def disponibilidadGuardada = disponibilidadService.saveDisponibilidad(disponibilidadData)
					empleadoInstance.addToDisponibilidad(disponibilidadGuardada)
				}
			}
		}else{
			empleadoInstance.disponibilidad.each { disponibilidad ->
				empleadoInstance.removeFromDisponibilidad(disponibilidad)
				disponibilidadService.deleteDisponibilidad(disponibilidad.id)
			}
		}


		command.idiomas.each{ id ->
			def idioma = Idioma.get(id)
			if (!empleadoInstance.idiomas.contains(idioma)){
				empleadoInstance.addToIdiomas(idioma)
			}
		}

		empleadoInstance.save(flush:true, failOnError:true)
	}

	def deleteEmpleado(Long id){
		Empleado empleadoInstance = Empleado.get(id)
		return empleadoInstance.delete()
	}
}