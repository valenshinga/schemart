package com.schemart.clase

import org.joda.time.LocalDate
import org.joda.time.LocalTime
import com.schemart.Estado
import com.schemart.empleado.Empleado
import com.schemart.idioma.Idioma
import com.schemart.alumno.Alumno

class Clase {

    LocalDate fecha
    LocalTime inicio
    LocalTime fin
    Idioma idioma
    String descripcion
    String observaciones
    Estado estado

    static belongsTo = [docente: Empleado]
    static hasMany = [alumno: Alumno]

    static constraints = {
        fecha nullable: false
        idioma nullable: false
        descripcion nullable: true
        observaciones nullable: true
        estado nullable: false
        docente nullable: false
    }
} 