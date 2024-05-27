package com.tecnofind.persona

import com.tecnofind.domicilio.DomicilioCommand
import grails.validation.Validateable

class PersonaCommand implements Validateable{
    
    Long id
    Long version

    String primerNombre
    String segundoNombre
    String apellidoPadre
    String apellidoMadre
    String tipoSociedad
    Integer anoNacimiento
    String documento
    Boolean autonomo
    Long poblacionNacimientoId
    Long oficinaId
    Long sitioId 
    String documentoEmpresa
    String razonSocialEmpresa
    String urlEmpresa
    List<String> telefonos = []
    List<String> telefonosEmpresa = []
    List<DomicilioCommand> domicilios = []
    List<String> domicilioAutonomo = []
    List<PersonaCommand> personasRelacionadas = []

    static constraints = {
        id nullable: true
        version nullable: true 
        primerNombre nullable: false
        segundoNombre nullable: true
        apellidoPadre nullable: false
        apellidoMadre nullable: true
        tipoSociedad nullable: true
        anoNacimiento nullable: true
        documento nullable: true
        autonomo nullable: true
        poblacionNacimientoId nullable: true
        oficinaId nullable: false
        sitioId nullable: true
        telefonos nullable: true
        telefonosEmpresa nullable: true
        documentoEmpresa nullable: true
        razonSocialEmpresa nullable: true
        urlEmpresa nullable: true
        domicilios nullable: true
        domicilioAutonomo nullable: true
    }    

    public String getNombreCompleto() {
        return [primerNombre, segundoNombre, apellidoPadre, apellidoMadre].findAll{it}.join(" ")
    }

}
