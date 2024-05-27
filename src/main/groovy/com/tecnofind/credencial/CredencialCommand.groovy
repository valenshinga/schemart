package com.tecnofind.credencial

import grails.validation.Validateable

class CredencialCommand implements Validateable{

    Long id
    Long version
    
    String usuario
    String password

    Long oficinaId
    Long sitioId

    static constraints = {
        id nullable: true
        version nullable: true
        
        usuario nullable: false
        password nullable: false
        
        oficinaId nullable: false
        sitioId nullable: false
    }

}