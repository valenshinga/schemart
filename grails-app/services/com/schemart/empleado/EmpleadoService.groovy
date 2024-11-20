package com.schemart.empleado

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import groovy.json.JsonSlurper
import com.schemart.idioma.Idioma
import com.schemart.disponibilidad.Disponibilidad
import com.schemart.disponibilidad.DisponibilidadService

import org.hibernate.transform.Transformers
import org.joda.time.format.DateTimeFormat
import org.hibernate.SessionFactory;

@Transactional
class EmpleadoService {
	def disponibilidadService
	SessionFactory sessionFactory;

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

		empleadoInstance.idiomas = []
		empleadoInstance = empleadoInstance.save(flush: true, failOnError: true)

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

		command.idiomas.each{ id ->
			def idioma = Idioma.get(id)
			if (!empleadoInstance.idiomas.contains(idioma)){
				empleadoInstance.addToIdiomas(idioma)
			}
		}
		empleadoInstance.refresh()
		empleadoInstance.save(flush:true, failOnError:true)
	}

	def deleteEmpleado(Long id){
		Empleado empleadoInstance = Empleado.get(id)
		return empleadoInstance.delete()
	}

	def listDocentes(){
		return Empleado.findAllByCargo("Docente")
	}

	def listDocentesDisponibles(String fecha, String inicio, String fin, Long idiomaId, Long claseId = null){
		String desde = new LocalTime(inicio)
		String hasta = new LocalTime(fin)

		def formato = DateTimeFormat.forPattern("dd-MM-yyyy")
		def fechaFormateada = LocalDate.parse(fecha, formato) 
		String dia = fechaFormateada.dayOfWeek().getAsText(new Locale("es")).capitalize()
		Idioma idioma = Idioma.get(idiomaId)
		def query = """
				SELECT empleado.id, 
					CONCAT(empleado.apellido, ' ', empleado.nombre) as "nombreCompleto"
				FROM empleado 
				JOIN empleado_idioma 
				ON empleado_idioma.empleado_idiomas_id = empleado.id 
				JOIN idioma 
				ON empleado_idioma.idioma_id = idioma.id
				JOIN disponibilidad 
				ON disponibilidad.docente_id = empleado.id
				LEFT JOIN clase 
				ON clase.docente_id = empleado.id 
					AND clase.fecha = '${fechaFormateada}' 
					AND (
						(clase.inicio < '${hasta}' AND clase.fin > '${desde}')
					)
				WHERE empleado.cargo = 'Docente'
				AND disponibilidad.desde <= '${desde}'
				AND disponibilidad.hasta >= '${hasta}'
				AND disponibilidad.dia = '${dia}'
				AND idioma.nombre = '${idioma.nombre}'
				AND idioma.nivel = '${idioma.nivel}'
				AND (clase.id IS NULL or clase.id = ${claseId}) 
				GROUP BY empleado.id;
			"""
		def docentes = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
		return docentes
	}
}