package com.schemart.empleado

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRACION'])
class EmpleadoController {
    def accessRulesService
    def empleadoService

    def index() {
        redirect (action: 'list')
    }

    def list(){

    }

    def create() {  
    }

    def ajaxGetEmpleados(){
        render empleadoService.listEmpleados() as JSON
    }
}
