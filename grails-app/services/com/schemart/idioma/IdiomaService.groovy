package com.schemart.idioma

import grails.transaction.Transactional
import com.schemart.Estado
import org.joda.time.LocalDate
import groovy.json.JsonSlurper

@Transactional
class IdiomaService {
	def listIdiomas() {
		return Idioma.list()
	}

	def getIdioma(Long id){
		return Idioma.get(id)
	}

	def saveIdioma(IdiomaCommand command) {
		Idioma idiomaInstance = new Idioma()

		idiomaInstance.nombre = command.nombre
		idiomaInstance.nivel = command.nivel
		
		return idiomaInstance.save(flush: true, failOnError: true)
	}

	def updateIdioma(IdiomaCommand command) {
		Idioma idiomaInstance = Idioma.get(command.id)

		idiomaInstance.nombre = command.nombre
		idiomaInstance.nivel = command.nivel
		
		return idiomaInstance.save(flush: true, failOnError: true)
	}

	def deleteIdioma(Long id){
		Idioma idiomaInstance = Idioma.get(id)
		return idiomaInstance.delete()
	}
}