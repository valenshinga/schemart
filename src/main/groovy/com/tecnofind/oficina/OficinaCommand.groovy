package com.tecnofind.oficina

import grails.validation.Validateable

class OficinaCommand implements Validateable{
    
    Long id
    Long version
    
    String nombre
    String ip

    static constraints = {
        id nullable: true
        version nullable: true
        
        nombre nullable: false
        ip nullable: false
    }
} 