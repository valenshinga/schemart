package com.schemart

import grails.validation.Validateable

class UserCommand implements Validateable{
    
    Long id
    Long version

	String username
	String nombre
	boolean accountLocked

    Long rolId

    static constraints = {
        id nullable: true
        version nullable: true
        
        username nullable: false
        nombre nullable: false
        accountLocked nullable: false

        rolId nullable: false
    }
} 
