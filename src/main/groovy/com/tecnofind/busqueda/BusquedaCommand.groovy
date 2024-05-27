package com.tecnofind.busqueda

import grails.validation.Validateable
import org.joda.time.LocalDateTime

class BusquedaCommand implements Validateable{

    Long id
    Long version

    Long responsableId
    LocalDateTime fechaHora 
    List<String> sitiosVisitados
    List<String> resultados
    String sitios
    List<String> inputs

    static constraints = {
        id nullable: true
        version nullable: true
        inputs nullable: false
        responsableId nullable: false
        fechaHora nullable: false
        sitiosVisitados nullable: true
        sitios nullable: true
        resultados nullable: true
    }

}