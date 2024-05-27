package com.tecnofind.persona

class Nombre {
    String castellano
    String catalan

    static constraints = {
        castellano nullable: false
        catalan nullable: false
    }
}