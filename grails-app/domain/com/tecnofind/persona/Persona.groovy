package com.tecnofind.persona

import com.tecnofind.busqueda.Busqueda
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.Poblacion
import com.tecnofind.oficina.Oficina    
import com.tecnofind.busqueda.PersonaBusqueda

class Persona {
    String primerNombre
    String segundoNombre
    String apellidoPadre
    String apellidoMadre
    String tipoSociedad
    Integer anoNacimiento
    String documento
    Poblacion poblacionNacimiento
    Boolean autonomo
    String domicilioAutonomo

    static hasMany = [telefonos: Telefono, domicilios: Domicilio,personasRelacionadas: PersonaRelacionada, busquedas: PersonaBusqueda]
    static belongsTo = [oficina: Oficina, empresa: Empresa]

    static constraints = {
        primerNombre nullable: false, blank: false
        segundoNombre nullable: true
        apellidoPadre nullable: false, blank: false
        apellidoMadre nullable: true
        tipoSociedad nullable: true
        anoNacimiento nullable: true
        documento nullable: true, unique: true
        autonomo nullable: true
        poblacionNacimiento nullable: true
        telefonos nullable: true
        domicilios nullable: true
        personasRelacionadas nullable: true
        oficina nullable: true
        empresa nullable: true
        domicilioAutonomo nullable: true
    }

    def getTipoRelacion(Persona personaPrincipal){
        return this.personasRelacionadas.find{it.principal == personaPrincipal && it.relacionada == this}?.relacion?.nombre
    }
    
    def unirDomicilios(){
        def domiciliosJuntos = []
        domicilios.sort { -it.id }.reverse().eachWithIndex { domicilio, i ->
            def descripcionDomicilio = "DOM ${i+1}: ${domicilio.calle} ${domicilio.numero ?: ''},"
            descripcionDomicilio += domicilio.bloque ? " Bloque ${domicilio.bloque}," : ''
            descripcionDomicilio += domicilio.portal ? " Portal ${domicilio.portal}," : ''
            descripcionDomicilio += domicilio.escalera ? " Escalera ${domicilio.escalera}," : ''
            descripcionDomicilio += domicilio.piso ? " Piso ${domicilio.piso}," : ''
            descripcionDomicilio += domicilio.puerta ? " Puerta ${domicilio.puerta}," : ''
            descripcionDomicilio += domicilio.codigoPostal ? " C.P. ${domicilio.codigoPostal}," : ''
            descripcionDomicilio += domicilio.poblacion ? " ${domicilio.poblacion.nombre}," : ''
            descripcionDomicilio += domicilio.provincia ? " ${domicilio.provincia.nombre}" : ''
            descripcionDomicilio += domicilio.domicilioAnterior ? " - Domicilio Anterior" : ''
            domiciliosJuntos << descripcionDomicilio
        }
        return domiciliosJuntos.join("\n")
    }

    def unirTelefonos(){
        def telefonosJuntos = []
        telefonos.eachWithIndex { telefono, i ->
        telefonosJuntos << "TEL ${i + 1}: ${telefono}"
        }
        return telefonosJuntos.join("\n")
    }

    public String getNombreCompleto() {
        return [primerNombre, segundoNombre, apellidoPadre, apellidoMadre].findAll{it}.join(" ")
    }
}
