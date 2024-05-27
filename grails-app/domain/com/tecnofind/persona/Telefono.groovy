package com.tecnofind.persona

import com.tecnofind.Estado
import com.tecnofind.sitio.Sitio

class Telefono {

    Sitio origen
    String numero
    Estado estado
    Boolean trabajo

    static belongsTo = [persona: Persona, empresa: Empresa]

    static constraints = {
        origen nullable: true
        numero nullable: false, blank: false
        estado nullable: false
        persona nullable: true
        empresa nullable: true
        trabajo nullable: true
    }

    def String toString(){
        def resultado = ""

        resultado += origen ? "${origen.nombre}: ${numero}" : ""

        return resultado.trim()
    }

}