package com.tecnofind.persona

import grails.validation.Validateable

class NombreCommand implements Validateable{
    
    Long id
    Long version
    
    String castellano
    String catalan

    static constraints = {
        id nullable: true
        version nullable: true
        
        castellano nullable: false
        catalan nullable: false
    }
}