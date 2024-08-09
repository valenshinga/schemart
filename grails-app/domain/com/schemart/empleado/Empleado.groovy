package com.schemart.empleado

import com.schemart.Auxiliar
import com.schemart.Estado
import com.schemart.idioma.Idioma

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class Empleado {

    String nombre
    String apellido
    String dni 
    String cuit
    String domicilio
    String email
    String telefono
    LocalDate fechaNacimiento
    String cargo 
    Estado estado

    static hasMany = [idiomas: Idioma, disponibilidad: DisponibilidadDocente]

	static constraints = {
        nombre nullable: false
        apellido nullable: false 
        dni nullable: false
        cuit nullable: true
        domicilio nullable: true
        email nullable: true, unique: true
        telefono nullable: true, unique: true
        fechaNacimiento nullable: false
        cargo nullable: false
        estado nullable: false
	}
}