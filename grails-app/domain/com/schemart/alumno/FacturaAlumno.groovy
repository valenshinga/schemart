package com.schemart.alumno

import com.schemart.Auxiliar
import com.schemart.Estado

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class FacturaAlumno {

    Integer horasTotales
    Float monto
    Boolean facturado
    
    static belongsTo = [alumno: Alumno]

	static constraints = {
        horasTotales nullable: false
        monto nullable: false 
        facturado nullable: false
	}
}