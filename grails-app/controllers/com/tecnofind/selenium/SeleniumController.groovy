package com.tecnofind.selenium

import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.AccessRulesService
import com.tecnofind.User
import com.tecnofind.sitio.Sitio
import com.tecnofind.oficina.Oficina
import com.tecnofind.credencial.Credencial
import com.tecnofind.Role
import com.tecnofind.UserRole
import org.joda.time.LocalDateTime
import grails.converters.JSON
import com.tecnofind.persona.Persona

@Secured(['ROLE_SUPER_ADMIN','ROLE_ADMIN'])
class SeleniumController{
    def inglobalyService
    def seleniumService
    def bittoolsService
    AccessRulesService accessRulesService
    def index() {
    }

    def obtenerListaPoblacionPorProvincia(){
        inglobalyService.guardarProvinciaYPoblacion()
        redirect  (controller: "oficina" , action:"list")        
    }

    def obtenerPoblacionesBittools(){
        bittoolsService.importarPoblacionesBittools()
        redirect (controller: "oficina", action:"list")
    }

}