package com.schemart.clase

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate
import groovy.json.JsonSlurper
import org.joda.time.LocalTime
import com.schemart.alumno.Alumno
import com.schemart.empleado.Empleado
import com.schemart.Auxiliar
import org.joda.time.format.DateTimeFormat
import com.schemart.idioma.Idioma
import org.hibernate.SessionFactory
import org.hibernate.transform.Transformers

@Transactional
class ClaseService {
	def sessionFactory
	def listClases() {
		return Clase.list()
	}

	def getClaseCommand(Long id){
		Clase claseInstance = Clase.get(id)
		ClaseCommand command = new ClaseCommand()
		command.id = claseInstance.id
		command.fecha = claseInstance.fecha.toString("dd-MM-yyyy")
		command.inicio = claseInstance.inicio.toString("HH:mm")
		command.fin = claseInstance.fin.toString("HH:mm")
		command.descripcion = claseInstance.descripcion
		command.estadoId = claseInstance.estado.id
		command.docenteId = claseInstance.docente.id
		command.idiomaId = claseInstance.idioma.id
		command.alumnos = []
		claseInstance?.alumno?.each{ alumno ->
			command.alumnos.push(alumno?.id)
		}

		return command
	}

	def saveClase(ClaseCommand command) {
		Clase claseInstance = new Clase()

		def formato = DateTimeFormat.forPattern("dd-MM-yyyy")
		def timeDesde = Auxiliar.separarTiempo(command.inicio)
		def timeHasta = Auxiliar.separarTiempo(command.fin)
		claseInstance.fecha = LocalDate.parse(command.fecha, formato)
		claseInstance.inicio = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		claseInstance.fin = new LocalTime(timeHasta.hora.toInteger(), timeHasta.minuto.toInteger())
		claseInstance.estado = Estado.findByNombre("Programada")
		claseInstance.docente = Empleado.get(command.docenteId)
		claseInstance.idioma = Idioma.get(command.idiomaId)
		command.alumnos.each{ alumnoId ->
			def alumno = Alumno.get(alumnoId)
			claseInstance.addToAlumno(alumno)
		}
		return claseInstance.save(flush: true, failOnError: true)
	}

	def updateClase(ClaseCommand command) {
		Clase claseInstance = Clase.get(command.id)

		def formato = DateTimeFormat.forPattern("dd-MM-yyyy")
		def timeDesde = Auxiliar.separarTiempo(command.inicio)
		def timeHasta = Auxiliar.separarTiempo(command.fin)
		claseInstance.fecha = LocalDate.parse(command.fecha, formato)
		claseInstance.inicio = new LocalTime(timeDesde.hora.toInteger(), timeDesde.minuto.toInteger())
		claseInstance.fin = new LocalTime(timeHasta.hora.toInteger(), timeHasta.minuto.toInteger())
		claseInstance.estado = Estado.findByNombre("Programada")
		claseInstance.docente = Empleado.get(command.docenteId)
		claseInstance.idioma = Idioma.get(command.idiomaId)
		command.alumnos.each{ alumnoId ->
			def alumno = Alumno.get(alumnoId)
			claseInstance.addToAlumno(alumno)
		}
		return claseInstance.save(flush: true, failOnError: true)
	}

	def listClasesByDocente() {
		def query = """
			SELECT 
				clase.id AS "claseId",
				clase.fecha,
				concat(
					(clase.fecha || 'T' || clase.inicio)
				) AS "fechaInicio",
				concat(
					(clase.fecha || 'T' || clase.fin)
				) AS "fechaFin",
				clase.inicio,
				clase.fin,
				estado.nombre AS estado,
				idioma.id AS "idiomaId",
				idioma.nombre AS "idiomaNombre",
				idioma.nivel,
				docente.id AS "docenteId",
				alumno.id AS "alumnoId",
				alumno.apellido,
				alumno.nombre
			FROM clase 
			LEFT JOIN estado ON clase.estado_id = estado.id
			LEFT JOIN idioma ON clase.idioma_id = idioma.id
			LEFT JOIN empleado docente ON clase.docente_id = docente.id
			LEFT JOIN clase_alumno ON clase.id = clase_alumno.clase_alumno_id
			LEFT JOIN alumno ON clase_alumno.alumno_id = alumno.id
			GROUP BY clase.id, clase.fecha, clase.inicio, clase.fin, estado.nombre, idioma.id, idioma.nombre, idioma.nivel, docente.id, alumno.id, alumno.apellido, alumno.nombre;
		"""
		def clases = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
		return clases
	}

	def getAlumnosByClase(Long claseId){
		Clase clase = Clase.get(claseId)
		def alumnos = clase.alumno
		return alumnos
	}


	// def updateIdioma(IdiomaCommand command) {
	// 	Idioma idiomaInstance = Idioma.get(command.id)

	// 	idiomaInstance.nombre = command.nombre
	// 	idiomaInstance.nivel = command.nivel
		
	// 	return idiomaInstance.save(flush: true, failOnError: true)
	// }

	def deleteClase(Long id){
		Clase claseInstance = Clase.get(id)
		return claseInstance.delete()
	}
}