package com.schemart.alumno

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate
import com.schemart.idioma.Idioma
import groovy.json.JsonSlurper
import com.schemart.disponibilidad.DisponibilidadService
import org.joda.time.format.DateTimeFormat
import org.joda.time.LocalTime
import org.hibernate.SessionFactory
import org.hibernate.transform.Transformers
import com.schemart.clase.Clase

@Transactional
class AlumnoService {
	def disponibilidadService
	SessionFactory sessionFactory

	def listAlumnos(){
		return Alumno.list()
	}

	def getAlumnoCommand(Long id){
		def alumnoInstance = Alumno.get(id)
		def command = new AlumnoCommand()
		
		def calle = ''
		def numero = ''
		if(alumnoInstance.domicilio){
			def pattern = ~/^(.*?)(\d+)$/
			def matcher = pattern.matcher(alumnoInstance.domicilio)
			if (matcher.matches()) {
				calle = matcher.group(1).trim()
				numero = matcher.group(2).trim()
			}
		}
		command.id = alumnoInstance.id
		command.nombre = alumnoInstance.nombre
		command.apellido = alumnoInstance.apellido
		command.dni = alumnoInstance.dni
		command.calle = calle
		command.numero = numero
		command.email = alumnoInstance.email
		command.telefono = alumnoInstance.telefono
		command.fechaNacimiento = alumnoInstance.fechaNacimiento
		command.tipoCursoId = alumnoInstance.tipoCurso?.id
		command.estadoId = alumnoInstance.estado?.id
		command.idiomas = alumnoInstance.idiomas.id as List

		return command
	}

	def saveAlumno(AlumnoCommand command, String disponibilidades = null) {
		Alumno alumnoInstance = new Alumno()

		alumnoInstance.nombre = command.nombre
		alumnoInstance.apellido = command.apellido
		alumnoInstance.dni = command.dni
		alumnoInstance.domicilio = "$command.calle $command.numero"
		alumnoInstance.email = command.email
		alumnoInstance.telefono = command.telefono
		alumnoInstance.fechaNacimiento = LocalDate.parse(command.fechaNacimiento)
		alumnoInstance.tipoCurso = TipoCurso.get(command.tipoCursoId)
		alumnoInstance.estado = Estado.findByNombre('Activo')
		alumnoInstance.disponibilidad = []
		alumnoInstance.idiomas = []
		
		alumnoInstance = alumnoInstance.save(flush: true, failOnError: true)


		def disponibilidadesList = disponibilidades ? new JsonSlurper().parseText(disponibilidades) : []
		disponibilidadesList.each { disponibilidad ->
			def disponibilidadGuardada = disponibilidadService.saveDisponibilidad(disponibilidad)
			alumnoInstance.addToDisponibilidad(disponibilidadGuardada)
		}

		command.idiomas.each { id ->
			alumnoInstance.addToIdiomas(Idioma.get(id))
		}

		alumnoInstance.save(flush: true, failOnError: true)
	}


	def updateAlumno(AlumnoCommand command, String disponibilidades = null) {
		Alumno alumnoInstance = Alumno.get(command.id)
        def disponibilidadesList = new JsonSlurper().parseText(disponibilidades)
		def disponibilidadesRecibidasConId = disponibilidadesList.findAll { it.id != null }*.id

		def disponibilidadesAEliminar = []
		alumnoInstance.disponibilidad.each { disponibilidad ->
			if (!disponibilidadesRecibidasConId.contains(disponibilidad.id.toInteger())){
				disponibilidadesAEliminar << disponibilidad
			}
		}

		if (disponibilidadesAEliminar) {
			disponibilidadesAEliminar.each { disponibilidad ->
				alumnoInstance.removeFromDisponibilidad(disponibilidad)
				disponibilidadService.deleteDisponibilidad(disponibilidad.id)
			}
		}

		disponibilidadesList.each { disponibilidadData ->
			if (disponibilidadData.id) {
				def disponibilidadExistente = disponibilidadService.updateDisponibilidad(disponibilidadData)
				if (!alumnoInstance.disponibilidad.contains(disponibilidadExistente)) {
					alumnoInstance.addToDisponibilidad(disponibilidadExistente)
				}
			} else {
				def disponibilidadGuardada = disponibilidadService.saveDisponibilidad(disponibilidadData)
				alumnoInstance.addToDisponibilidad(disponibilidadGuardada)
			}
		}

		command.idiomas.each{ id ->
			def idioma = Idioma.get(id)
			if (!alumnoInstance.idiomas.contains(idioma)){
				alumnoInstance.addToIdiomas(idioma)
			}
		}

		alumnoInstance.nombre = command.nombre
		alumnoInstance.apellido = command.apellido
		alumnoInstance.dni = command.dni
		alumnoInstance.domicilio = "$command.calle $command.numero"
		alumnoInstance.email = command.email
		alumnoInstance.telefono = command.telefono
		alumnoInstance.fechaNacimiento = LocalDate.parse(command.fechaNacimiento)
		alumnoInstance.tipoCurso = TipoCurso.get(command.tipoCursoId)
		alumnoInstance.estado = Estado.get(command.estadoId)
		alumnoInstance.save(flush:true, failOnError:true)
	}

	def deleteAlumno(Long id){
		Alumno alumnoInstance = Alumno.get(id)
		return alumnoInstance.delete()
	}

	def listTiposCursos() {
		return TipoCurso.list()
	}

	def listIdiomas() {
		return Idioma.list()
	}

	// def listAlumnosByClase(Long claseId){
	// 	return Alumno.findAllByClase(Clase.get(claseId))
	// }

	def listAlumnosDisponibles(String fecha, String inicio, String fin, Long idiomaId){
		String desde = new LocalTime(inicio)
		String hasta = new LocalTime(fin)

		def formato = DateTimeFormat.forPattern("dd-MM-yyyy")
		String dia = LocalDate.parse(fecha, formato).dayOfWeek().getAsText(new Locale("es")).capitalize()
		Idioma idioma = Idioma.get(idiomaId)
		def query = """
			SELECT alumno.id, 
				CONCAT(alumno.apellido, ' ', alumno.nombre) as "nombreCompleto",
				alumno.email,
				alumno.telefono
				FROM alumno 
				JOIN alumno_idioma 
				ON alumno_idioma.alumno_idiomas_id = alumno.id 
				JOIN idioma
				ON alumno_idioma.idioma_id = idioma.id
				JOIN disponibilidad 
				ON disponibilidad.alumno_id = alumno.id
				WHERE disponibilidad.desde <= '${desde}'
					AND disponibilidad.hasta >= '${hasta}'
					AND disponibilidad.dia = '${dia}'
					AND idioma.nombre = '${idioma.nombre}'
					AND idioma.nivel = '${idioma.nivel}'
				GROUP BY alumno.id
				;
		"""
		def alumnos = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
		return alumnos
	}
}