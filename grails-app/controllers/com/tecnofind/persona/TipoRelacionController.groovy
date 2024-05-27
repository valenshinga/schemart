package com.tecnofind.persona

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.tecnofind.Auxiliar

@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN'])
class TipoRelacionController {

    def tipoRelacionService

    def index() {
        redirect (action: 'list')
    }

    def list() {}

    def create() {}

    def save(TipoRelacionCommand tipoRelacionCommand) {
        def resultado = [ 'error': false ]
        try{
            tipoRelacionService.save(tipoRelacionCommand)
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            resultado.error = true
        }
        catch(Exception e){
            Auxiliar.printearError e
            resultado.error = true
        }
        finally{
            render resultado as JSON
        }
    }

    def edit(Long id) {
        [tipoRelacionCommand: tipoRelacionService.getTipoRelacionCommand(id)]
    }

    def update(TipoRelacionCommand tipoRelacionCommand) {
        def resultado = [ 'error': false ]
        try{
            tipoRelacionService.update(tipoRelacionCommand)
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            resultado.error = true
        }
        catch(Exception e){
            Auxiliar.printearError e
            resultado.error = true
        }
        finally{
            render resultado as JSON
        }    
    }

    def delete(Long id) {
        def resultado = [ 'error': false ]
        try{
            tipoRelacionService.delete(id)
        }
        catch(AssertionError e) {
            Auxiliar.printearError e
            resultado.error = true
        }
        catch(Exception e){
            Auxiliar.printearError e
            resultado.error = true
        }
        finally{
            render resultado as JSON
        }   
    }

    def ajaxGetTiposRelacion() {
        def tiposRelacion = tipoRelacionService.listTiposRelacion()
        render tiposRelacion as JSON
    }

    def asignarTipoRelacion(Persona persona, Persona relacionada){
                tipoRelacionService.asignarTipoRelacion(persona, relacionada)
    }
}