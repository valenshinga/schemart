package com.tecnofind.oficina

import com.tecnofind.credencial.Credencial
import com.tecnofind.User
import com.tecnofind.persona.Persona

class Oficina {

	String nombre
	String ip

	static hasMany = [ credenciales: Credencial, usuarios: User, personasBuscadas: Persona ]

	static constraints = {
        nombre nullable: false
		ip nullable: true
		usuarios nullable: true
		credenciales nullable: true	
		personasBuscadas nullable: true
	}
}
