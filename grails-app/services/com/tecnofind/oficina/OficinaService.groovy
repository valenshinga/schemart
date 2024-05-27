package com.tecnofind.oficina

import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import com.tecnofind.busqueda.Busqueda
import com.tecnofind.User
@Transactional
class OficinaService {
    def sessionFactory

    public List<Oficina> listOficinas() {
        return Oficina.list()    
    } 

    public Oficina save(OficinaCommand command) { 

        Oficina oficina = new Oficina()

        oficina.nombre = command.nombre
        oficina.ip = command.ip
        
        return oficina.save(flush:true, failOnError:true)
    }

    public Oficina getOficina(Long id) {
        return Oficina.get(id)
    }

    public Oficina update(OficinaCommand command) { 

        Oficina oficina = getOficina(command.id)
        oficina.nombre = command.nombre 
        oficina.ip = command.ip

        return oficina.save(flush:true, failOnError:true)
    }

    @Transactional
    public delete(Long id) {
        Oficina oficina = getOficina(id)
        oficina.personasBuscadas.each { persona ->
            persona.busqueda?.removeFromResultados(persona)
            persona.busqueda?.delete(flush:true, failOnError: true)
            persona.personasRelacionadas.each { personaRelacionada ->
                persona.removeFromPersonasRelacionadas(personaRelacionada)
                personaRelacionada.delete(flush:true, failOnError: true)
                persona.save(flush: true, failOnError: true)
            }          
        }
        def users = [] 
        oficina.usuarios.each { usuario ->
            if (!usuario.hasRole("ROLE_SUPER_ADMIN")) {
                users << usuario.id
            }
            def busquedas = Busqueda.findAllByResponsable(usuario)
            busquedas?.each { busqueda ->       
                busqueda.delete(flush:true, failOnError: true)
            }
            usuario.oficina = null
            usuario.save(flush: true, failOnError: true)
        }

        oficina.usuarios.clear()
        oficina.save(flush: true, failOnError: true)
        users.each { userId ->
            def usuario = User.get(userId)
            usuario.oficina = null
            usuario.save(flush: true, failOnError: true)
            usuario.delete(flush: true, failOnError: true)
        }
        oficina.delete(flush:true, failOnError:true)
    }


    def getOficinaCommand(Long id) {
        def oficina = getOficina(id)
        def oficinaCommand = new OficinaCommand()

        oficinaCommand.id = oficina.id
        oficinaCommand.version = oficina.version
        oficinaCommand.nombre = oficina.nombre 
        oficinaCommand.ip = oficina.ip
        return oficinaCommand
    }


}