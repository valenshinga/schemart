package com.schemart.clase

import com.schemart.User
import org.joda.time.LocalDateTime
import com.schemart.alumno.Alumno
import com.schemart.Estado

class Asistencia {

    Estado estado

    static belongsTo = [clase: Clase, alumno: Alumno]

    static constraints = {
        estado nullable: false
        clase nullable: false
        alumno nullable: false
    }
} 