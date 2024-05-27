package com.tecnofind.domicilio

class Poblacion{
    
    String nombre

    static belongsTo = [provincia: Provincia]
    
    static constraints = {
        nombre nullable: false, blank: false
    }
}