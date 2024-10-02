package com.schemart.clase

import grails.validation.Validateable

class ClaseCommand implements Validateable{
	
	Long id
	Long version

	String fecha
    String inicio
    String fin
	Float duracion
	String descripcion
	String  observaciones
    Long estadoId
	Long docenteId
	List<Long> alumnos 
    Long idiomaId

	static constraints = {
		id nullable: true
		version nullable: true
		fecha nullable: false
		inicio nullable: false
		fin nullable: false
		duracion nullable: true
		descripcion nullable: true
		observaciones nullable: true
		estadoId nullable: false
		docenteId nullable: false
		alumnos nullable: false
		idiomaId nullable: false
		estadoId nullable: false
	}
} 
