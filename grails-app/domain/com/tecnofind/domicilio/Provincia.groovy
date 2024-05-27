package com.tecnofind.domicilio

class Provincia {

    String nombre
    String nombreBittools
    String nombreTelexplorer
    String nombreAbctelefonos

    static hasMany = [poblaciones: Poblacion]

    static constraints = {
        nombre nullable: false, blank: false
        nombreBittools nullable: true
        nombreTelexplorer nullable: true
        nombreAbctelefonos nullable: true
    }
}