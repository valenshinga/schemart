package com.tecnofind.persona

import com.tecnofind.domicilio.Domicilio

class Empresa {

    String razonSocial
    String documento
    String url

    static hasMany = [telefonos: Telefono, domicilios: String, empleados: Persona]

    static constraints = {
        razonSocial nullable: false, blank: false
        documento nullable: true, unique: true
        url nullable: true
        telefonos nullable: true
        domicilios nullable: true
        empleados nullable: true
    }


     String toString() {
        def result = """
            ${razonSocial}
            Teléfono: ${unirTelefonos() ?: 'No disponible'}
            ${domicilios}
            Sitio web: ${url ?: 'No disponible'}
            
        """
        return result
    }

    def unirTelefonos(){
        def telefonosJuntos = []
        telefonos.eachWithIndex { telefono, i ->
        telefonosJuntos << "-TEL ${i + 1}: ${telefono}"
        }
        return telefonosJuntos.join("\n")
    }

   

}