package com.schemart.alumno

import com.schemart.Auxiliar
import com.schemart.Estado

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