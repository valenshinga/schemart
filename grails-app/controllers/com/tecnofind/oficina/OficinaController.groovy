package com.tecnofind.oficina

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.Auxiliar

@Secured(['ROLE_SUPER_ADMIN'])
class OficinaController {

    def oficinaService

    def index() {
        redirect (action: 'list')
    }

    def list() {}

    def create() {}

    def save(OficinaCommand oficinaCommand) {
        try{
            oficinaService.save(oficinaCommand)
            flash.message = "Oficina guardada correctamente"
            redirect(action: "list")
        }
        catch(AssertionError e) {
            flash.message = e.message.split("finerror")[0]
            render(view: "create", model: [oficinaCommand: oficinaCommand])
        }
        catch(Exception e){
            flash.message = "Error al guardar la oficina"
            Auxiliar.printearError e
            render(view: "create", model: [oficinaCommand: oficinaCommand])
        }
    }

    def edit(Long id) {
        [oficinaCommand: oficinaService.getOficinaCommand(id)]
    }

    def update(OficinaCommand oficinaCommand) {
        try{
            oficinaService.update(oficinaCommand)
            redirect(action: "list")
            flash.message = "Oficina guardada correctamente"
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            flash.error = e.message.split("finerror")[0]
            render (view: "edit", model: [oficinaCommand: oficinaCommand])
        }
        catch(Exception e){
            flash.error = "Error al guardar la oficina"
            Auxiliar.printearError e
            render (view: "edit", model: [oficinaCommand: oficinaCommand])
        }
    
    }

    def delete(Long id) {
        try{
            oficinaService.delete(id)
            redirect(action: "list")
            flash.message = "Oficina borrada correctamente"
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            flash.error = e.message.split("finerror")[0]
            redirect (action: "edit", params: [id:id])
        }
        catch(Exception e){
            flash.error = "Error al borrar la oficina"
            Auxiliar.printearError e
            redirect (action: "edit", params: [id:id])
        }
    }

    def ajaxGetOficinas() {
        def oficinas = oficinaService.listOficinas()
        render oficinas as JSON
    }

}