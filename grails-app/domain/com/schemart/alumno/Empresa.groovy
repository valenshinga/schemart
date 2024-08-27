package com.schemart.alumno

import com.schemart.Auxiliar
import com.schemart.Estado
import com.schemart.idioma.Idioma

class Empresa {

    String razonSocial
    String nombre
    String email
    String telefono
    String direccion
    String sede

    static hasMany = [alumno: Alumno]

	static constraints = {
        nombre nullable: false
        razonSocial nullable: false 
        email nullable: false, unique: true
        telefono nullable: false
        direccion nullable: false
        sede nullable: true
	}
}