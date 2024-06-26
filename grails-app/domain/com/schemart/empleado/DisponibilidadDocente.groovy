package com.schemart.empleado

import com.schemart.Auxiliar

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class DisponibilidadDocente {

    String dia
    String desde 
    String hasta

    static belongsTo = [docente: Empleado]

	static constraints = {
        dia nullable: false
        desde nullable: false 
        hasta nullable: false
        docente nullable: false
	}
}