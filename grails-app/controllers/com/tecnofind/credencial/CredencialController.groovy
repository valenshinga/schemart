package com.tecnofind.credencial

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.AccessRulesService
import com.tecnofind.Auxiliar


@Secured(['ROLE_SUPER_ADMIN','ROLE_ADMIN'])
class CredencialController {

    def credencialService
    def accessRulesService

    def index() {
        redirect(action: "list")
    }

    def list() {}

    def create() {}

    def save(CredencialCommand credencialCommand) {
        try{
            credencialService.save(credencialCommand)
            flash.message = "Credencial guardada correctamente"
            redirect(action: "list")
        }
        catch(AssertionError e) {
            flash.error = e.message.split("finerror")[0]
            render(view: "create", model: [credencialCommand: credencialCommand])
        }
        catch(Exception e){
            flash.error = "Error al guardar la credencial"
            Auxiliar.printearError e
            render(view: "create", model: [credencialCommand: credencialCommand])
        }
    }

    def edit(Long id) {
        [credencialCommand: credencialService.getCredencialCommand(id)]
    }

    def update(CredencialCommand credencialCommand) {
        try{
            credencialService.update(credencialCommand)
            flash.message = "Credencial guardada correctamente"
            redirect(action: "list")
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            flash.error = e.message.split("finerror")[0]
            render (view: "edit", model: [credencialCommand: credencialCommand])
        }
        catch(Exception e){
            flash.error = "Error al guardar la credencial"
            Auxiliar.printearError e
            render (view: "edit", model: [credencialCommand: credencialCommand])
        }
    
    }

    def delete(Long id) {
        try{
            credencialService.delete(id)
            flash.message = "Credencial borrada correctamente"
            redirect(action: "list")
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            flash.error = e.message.split("finerror")[0]
            render (view: "edit", model: [credencialCommand: credencialCommand])
        }
        catch(Exception e){
            flash.error = "Error al borrar la credencial"
            Auxiliar.printearError e
            render (view: "edit", model: [credencialCommand: credencialCommand])
        }
    }

    def ajaxGetCredenciales() {             
        if (accessRulesService.isSuperAdmin()) {
            def credenciales = credencialService.listCredenciales()
            render credenciales as JSON
        } else {           
            def credenciales = credencialService.listCredencialesbyOficina()
            render credenciales as JSON
        }
        
    }
    
    def ajaxGetSitios() {
        def sitios = credencialService.listSitios()
        render sitios as JSON
    }

}