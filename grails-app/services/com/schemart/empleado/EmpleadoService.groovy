package com.schemart.empleado

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate

@Transactional
class EmpleadoService {

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
		// command.estado = command.estado
		command.estadoId = empleadoInstance.estado.id

		return command
	}

	def saveEmpleado(EmpleadoCommand command) {
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
		empleadoInstance.save(flush:true, failOnError:true)
	}

	def updateEmpleado(EmpleadoCommand command) {
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
		empleadoInstance.save(flush:true, failOnError:true)
	}

	def deleteEmpleado(Long id){
		Empleado empleadoInstance = Empleado.get(id)
		return empleadoInstance.delete()
	}
}