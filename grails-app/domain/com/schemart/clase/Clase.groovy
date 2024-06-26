package com.schemart.clase

import org.joda.time.LocalDateTime
import com.schemart.Estado
import com.schemart.empleado.Empleado
import com.schemart.idioma.Idioma
import com.schemart.alumno.Alumno

class Clase {

    LocalDateTime fecha
    Idioma idioma
    Float duracion
    String descripcion
    String observaciones
    Estado estado

    static belongsTo = [docente: Empleado]
    static hasMany = [alumno: Alumno]

    static constraints = {
        fecha nullable: false
        idioma nullable: false
        duracion nullable: false
        descripcion nullable: true
        observaciones nullable: true
        estado nullable: false
        docente nullable: false
    }
} 