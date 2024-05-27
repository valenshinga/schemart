package com.tecnofind.persona

import grails.validation.Validateable

class TipoRelacionCommand implements Validateable{
    
    Long id
    Long version
    
    String nombre

    static constraints = {
        id nullable: true
        version nullable: true
        
        nombre nullable: false
    }
} 