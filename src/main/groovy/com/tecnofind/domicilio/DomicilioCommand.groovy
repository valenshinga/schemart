package com.tecnofind.domicilio

import grails.validation.Validateable

class DomicilioCommand implements Validateable{
    
    Long id
    Long version
    Long provinciaId
    Long poblacionId
    String calle
    Integer numero
    String bloque
    String portal
    String escalera
    String piso
    String puerta
    String codigoPostal
    Long estadoId
    Long personaId
    Boolean domicilioAnterior 

    static constraints ={
        provinciaId nullable: false
        poblacionId nullable: false
        calle nullable: false, blank: false
        numero nullable: true
        bloque nullable: true
        portal nullable: true
        escalera nullable: true
        piso nullable: true
        puerta nullable: true
        codigoPostal nullable: true
        estadoId nullable: false, blank: false
        personaId nullable: false
        domicilioAnterior nullable: true
    }
}