package com.tecnofind.domicilio

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.AccessRulesService
import com.tecnofind.Auxiliar


@Secured(['ROLE_SUPER_ADMIN','ROLE_ADMIN', 'ROLE_USER'])
class DomicilioController {

    def domicilioService
    def accessRulesService

    def ajaxGetProvincias() {
        def provincias = domicilioService.listProvincias()
        render provincias as JSON
    }

    def ajaxGetPoblaciones(Long id) {
        def poblaciones = domicilioService.listPoblacionesByProvincia(id)
        render poblaciones as JSON
    }

    def ajaxGetAllPoblaciones() {
        def poblaciones = domicilioService.listPoblaciones()
        render poblaciones as JSON
    }

    def ajaxGetDomiciliosByPersona(Long id) {
        def domicilios = domicilioService.listDomiciliosByPersona(id)
        render domicilios as JSON
    }

    def ajaxGetDomicilio(Long id) {
        def domicilio = domicilioService.getDomicilio(id)
        render domicilio as JSON
    }
}