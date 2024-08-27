package com.schemart.idioma

import com.schemart.Auxiliar


import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class Idioma {

    String nombre
    String nivel 

	static constraints = {
        nombre nullable: false, blank: false
        nivel nullable: false, blank: false
	}
}