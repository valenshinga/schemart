package com.tecnofind

import grails.validation.Validateable

class UserCommand implements Validateable{
    
    Long id
    Long version

	String username
	String nombre
	boolean accountLocked

    Long oficinaId
    Long rolId

    static constraints = {
        id nullable: true
        version nullable: true
        
        username nullable: false
        nombre nullable: false
        accountLocked nullable: false

        oficinaId nullable: true
        rolId nullable: false
    }
} 
