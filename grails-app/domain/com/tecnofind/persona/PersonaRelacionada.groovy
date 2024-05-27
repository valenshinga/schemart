package com.tecnofind.persona

import com.tecnofind.Estado
import com.tecnofind.oficina.Oficina

class PersonaRelacionada{
    TipoRelacion relacion
    Estado estado

    static belongsTo = [principal: Persona, relacionada: Persona]

    static constraints = {
        principal nullable: false
        relacionada nullable: false
        relacion nullable: false
        estado nullable: false
    }

}
