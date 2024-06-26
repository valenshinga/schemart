package com.schemart.alumno

import com.schemart.Auxiliar

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class DisponibilidadAlumno {

    String dia
    String desde 
    String hasta

    static belongsTo = [alumno: Alumno]

	static constraints = {
        dia nullable: false
        desde nullable: false 
        hasta nullable: false
        alumno nullable: false
	}
}