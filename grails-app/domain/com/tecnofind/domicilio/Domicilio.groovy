package com.tecnofind.domicilio

import com.tecnofind.Estado
import com.tecnofind.persona.Persona

class Domicilio{
    Provincia provincia
    Poblacion poblacion
    String calle
    Integer numero
    String bloque
    String portal
    String escalera
    String piso
    String puerta
    String codigoPostal
    Estado estado
    Boolean domicilioAnterior = false

    static belongsTo = [persona: Persona]
    
    static constraints ={
        provincia nullable: false
        poblacion nullable: true
        calle nullable: false, blank: false
        numero nullable: true
        bloque nullable: true
        portal nullable: true
        escalera nullable: true
        piso nullable: true
        puerta nullable: true
        codigoPostal nullable: true
        estado nullable: false, blank: false
        domicilioAnterior nullable: true
    }

    def String toString(){
        def propiedades = ['calle', 'numero', 'bloque', 'portal', 'escalera', 'piso', 'puerta', 'codigoPostal', 'poblacion', 'provincia']
        def resultado = propiedades.collect { propiedad ->
            def valorPropiedad
            if (propiedad.toString() == 'numero'){
                valorPropiedad = this."$propiedad".toString()
                if (valorPropiedad == 'null')
                    valorPropiedad = null
            } else{
                valorPropiedad = this."$propiedad"
            }
            def valorFormateado = valorPropiedad ? (propiedad in ['poblacion', 'provincia'] ? valorPropiedad.nombre : "${valorPropiedad}") : null
            valorFormateado ? (propiedad == 'calle' ? "${valorFormateado} " : (propiedad == 'numero' ? "${valorFormateado}, " : "${valorFormateado}, ")) : null
        }.findAll { it != null }.join('').trim()

        return resultado
    }
}