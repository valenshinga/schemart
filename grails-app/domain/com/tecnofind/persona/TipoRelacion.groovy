package com.tecnofind.persona

class TipoRelacion{
    String nombre

    static constraints = {
        nombre nullable: false, blank: false
    }
}