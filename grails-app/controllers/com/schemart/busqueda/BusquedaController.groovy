package com.schemart.busqueda

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_DOCENTE'])
class BusquedaController {

    def index(){
        render 'hola'
    }

    def usuarioService
    def accessRulesService
    def notificacionService
    def springSecurityService

    // def index() {
    //     redirect (action: 'buscar')
    // }

    // def buscar(){

    // }

    // def create() {  
    // }
}
