package com.tecnofind.credencial

import com.tecnofind.oficina.Oficina
import com.tecnofind.sitio.Sitio

class Credencial {

    String usuario
    String password

    static belongsTo = [oficina: Oficina, sitio: Sitio]

    static constraints = {
        usuario nullable: false, blank: false
        password nullable: false, blank: false
        oficina nullable: false
        sitio nullable: false
    }
}
