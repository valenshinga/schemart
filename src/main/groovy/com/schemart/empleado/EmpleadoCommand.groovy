package com.schemart.empleado

import grails.validation.Validateable
import com.schemart.Auxiliar
import com.schemart.Estado
import com.schemart.idioma.Idioma

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class EmpleadoCommand implements Validateable{
    
    Long id
    Long version

    String nombre
    String apellido
    String dni 
    String cuit
    String calle
    String numero
    String email
    String telefono
    String fechaNacimiento
    String cargo 
    Long estadoId

	static constraints = {
        id nullable: true
        version nullable: true
        nombre nullable: false
        apellido nullable: false
        dni nullable: false
        cuit nullable: true
        calle nullable: true
        numero nullable: true
        email nullable: true
        telefono nullable: true
        fechaNacimiento nullable: false
        cargo nullable: false
        estadoId nullable: true
	}
} 
