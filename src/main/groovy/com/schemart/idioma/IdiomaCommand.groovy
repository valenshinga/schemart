package com.schemart.idioma

import grails.validation.Validateable
import com.schemart.Auxiliar

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class IdiomaCommand implements Validateable{
	
	Long id
	Long version

	String nombre
	String nivel

	static constraints = {
		id nullable: true
		version nullable: true
		nombre nullable: false
		nivel nullable: false
	}
} 
