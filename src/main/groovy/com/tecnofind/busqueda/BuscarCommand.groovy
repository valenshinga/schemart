package com.tecnofind.busqueda

import grails.validation.Validateable
import com.tecnofind.domicilio.DomicilioCommand
import com.tecnofind.domicilio.Provincia
import com.tecnofind.domicilio.Poblacion

class BuscarCommand implements Validateable, Cloneable{

    Long id
    Long version
    
    String primerNombre
    String segundoNombre 
    String apellidoPadre
    String apellidoMadre
    String documento
    String telefono
    DomicilioCommand domicilio 
    
    static constraints = {
        id nullable: true
        version nullable: true
        primerNombre nullable: true
        segundoNombre nullable: true
        apellidoPadre nullable: true
        apellidoMadre nullable: true
        documento nullable: true
        telefono nullable: true
        domicilio nullable: true
    }

    public String getParametros() {
        return "primerNombre: ${primerNombre}, segundoNombre: ${segundoNombre}, apellidoPadre: ${apellidoPadre}, apellidoMadre: ${apellidoMadre}, \n" +
                 "documento: ${documento}, telefono: ${telefono}, \n" +
                 "provinciaId: ${domicilio.provinciaId}, poblacionId: ${domicilio.poblacionId}, calle: ${domicilio.calle}, numero: ${domicilio.numero}, bloque: ${domicilio.bloque}, \n" +
                 "portal: ${domicilio.portal}, escalera: ${domicilio.escalera}, puerta: ${domicilio.puerta}, piso: ${domicilio.piso}, codigoPostal: ${domicilio.codigoPostal} "
    }

    public String getInputs() {
    def parametros = getParametros()
    parametros = parametros.replaceAll("null", "-")                           
                           .replace("primerNombre", "Primer nombre")
                           .replace("segundoNombre", "Segundo nombre")
                           .replace("apellidoPadre", "Apellido paterno")
                           .replace("apellidoMadre", "Apellido materno")
                           .replace("documento", "Documento")
                           .replace("telefono", "Teléfono")
                           .replace("calle", "Calle")
                           .replace("numero", "Número")
                           .replace("bloque", "Bloque")
                           .replace("portal", "Portal")
                           .replace("escalera", "Escalera")
                           .replace("puerto", "Puerto")
                           .replace("piso", "Piso")
                           .replace("codigoPostal", "Código postal")

    if (domicilio.provinciaId) {
        parametros = parametros.replace("provinciaId: ${domicilio.provinciaId}", "Provincia: ${Provincia.get(domicilio.provinciaId).nombre}")
    }else {
        parametros = parametros.replace("provinciaId", "Provincia")
    }
    if (domicilio.poblacionId){
        parametros = parametros.replace("poblacionId: ${domicilio.poblacionId}", "Población: ${Poblacion.get(domicilio.poblacionId).nombre}")
    }else {
        parametros = parametros.replace("poblacionId", "Población")
    }
    return parametros
    }

    @Override
    BuscarCommand clone() {
        try {
            return (BuscarCommand) super.clone()
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e)
        }
    }

    public String getNombreCompleto() {
        return [primerNombre, segundoNombre, apellidoPadre, apellidoMadre].findAll{it}.join(" ")
    }

    public Boolean getTieneNombre() {
        return [primerNombre, apellidoPadre].findAll{it}.size() > 1
    }

    public Boolean getTieneDireccion() {
        return domicilio.provinciaId && domicilio.poblacionId && domicilio.numero && domicilio.calle
    }
}
