package com.schemart.disponibilidad

import org.joda.time.LocalTime
import com.schemart.empleado.Empleado
import com.schemart.alumno.Alumno

class Disponibilidad {

    String dia
    LocalTime desde 
    LocalTime hasta

    static belongsTo = [docente: Empleado, alumno: Alumno]

	static constraints = {
        dia nullable: false
        desde nullable: false 
        hasta nullable: false
        docente nullable: true
        alumno nullable: true
	}
}