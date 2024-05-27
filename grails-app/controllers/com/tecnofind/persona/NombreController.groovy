package com.tecnofind.persona

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.Auxiliar

@Secured(['ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUPER_ADMIN'])
class NombreController{
    def nombreService

    def index() {
        redirect(action: "list")
    }

    def list() {}

    def create() {}

    def save(NombreCommand nombreCommand){
        nombreService.save(nombreCommand)
        flash.message = "Sinónimo guardado correctamente"
        redirect(action: "list")
    }

    def edit(Long id) {
        [nombreCommand: nombreService.getNombreCommand(id)]
    }

    def update(NombreCommand nombreCommand) {
        nombreService.update(nombreCommand)
        flash.message = "Sinónimo guardado correctamente"
        redirect(action: "list")
    }

    def delete(Long id) {
        nombreService.delete(id)
        flash.message = "Sinónimo eliminado correctamente"
        redirect(action: "list")
    }

    def ajaxGetNombres(){
        def nombres = nombreService.listNombres()
        render nombres as JSON
    }
}
