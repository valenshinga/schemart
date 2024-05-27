package com.tecnofind.busqueda

import com.tecnofind.User
import com.tecnofind.sitio.Sitio
import com.tecnofind.persona.Persona
import org.joda.time.LocalDateTime

class Busqueda{

    User responsable
    LocalDateTime fechaHora
    Persona personaPrincipal

    static hasMany = [sitiosVisitados: Sitio, personas: PersonaBusqueda, inputs: String, logError: String]
    static constraints  = {
        inputs nullable: false
        responsable nullable: false
        fechaHora nullable: false
        personaPrincipal nullable:true
    }

    def getResultados(){
        return this.personas.findAll{it.busqueda == this}.collect{it.persona}
    }
}