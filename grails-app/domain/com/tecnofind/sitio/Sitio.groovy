package com.tecnofind.sitio

import com.tecnofind.credencial.Credencial

class Sitio {
    String nombre
    String url

    static hasMany = [credenciales: Credencial]

    static constraints = {
        nombre nullable: false, blank: false, unique: true
        url nullable: false, blank: false, unique: true
        credenciales nullable: true
    }

    public String toString(){
        return this.nombre
    }
}