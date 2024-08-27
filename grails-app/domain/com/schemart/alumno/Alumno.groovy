package com.schemart.alumno

import com.schemart.Auxiliar
import com.schemart.Estado
import com.schemart.idioma.Idioma
import com.schemart.disponibilidad.Disponibilidad

import org.joda.time.LocalDate

class Alumno {

	String nombre
	String apellido 
	String dni
	String domicilio
	String email
	String telefono
	LocalDate fechaNacimiento
	Estado estado
	TipoCurso tipoCurso

	static hasMany = [idiomas: Idioma, disponibilidad: Disponibilidad]
	static belongsTo = [empresa: Empresa]

	static constraints = {
		nombre nullable: false
		apellido nullable: false 
		domicilio nullable: true
		dni nullable: false
		tipoCurso nullable: false
		email nullable: true, unique: true
		telefono nullable: true, unique: true
		fechaNacimiento nullable: false
		idiomas nullable: false
		disponibilidad nullable: false
		empresa nullable: true
		estado nullable: false
	}
}