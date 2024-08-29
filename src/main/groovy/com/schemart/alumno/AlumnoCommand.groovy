package com.schemart.alumno

import grails.validation.Validateable
import com.schemart.Auxiliar
import com.schemart.Estado
import com.schemart.idioma.Idioma

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class AlumnoCommand implements Validateable{
	
	Long id
	Long version

	String nombre
	String apellido
	String dni 
	String calle
	String numero
	String email
	String telefono
	String fechaNacimiento
	Long tipoCursoId
	Long estadoId
    List<Long> idiomas
    Long empresaId

	static constraints = {
        nombre nullable: false
        apellido nullable: false
        dni nullable: false
        calle nullable: true
        numero nullable: true
        email nullable: true, email: true
        telefono nullable: true
        fechaNacimiento nullable: false
        estadoId nullable: false
        tipoCursoId nullable: false
        idiomas nullable: true
        empresaId nullable: true
	}
} 
