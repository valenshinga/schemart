package com.tecnofind.credencial

import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import grails.plugin.springsecurity.SpringSecurityUtils

import com.tecnofind.oficina.Oficina
import com.tecnofind.sitio.Sitio
import com.tecnofind.AccessRulesService

@Transactional
class CredencialService {
    def sessionFactory
    def accessRulesService

    public List<Credencial> listCredenciales() {
        return Credencial.list()
    } 

    public List<Credencial> listCredencialesbyOficina() {
        def id = accessRulesService.getCurrentUser().oficina.id
        return Credencial.createCriteria().list {
            eq("oficina.id", id)
        }
    }

    public Credencial save(CredencialCommand command) { 
        assert command.usuario != null && command.usuario != "": "El usuario no puede estar vacíofinerror"
        assert command.password != null && command.password != "", "La contraseña no puede estar vacíafinerror"
        assert command.sitioId != null && command.sitioId != "", "Debe seleccionar un sitiofinerror"
        Credencial credencial = new Credencial()
        Sitio sitio = Sitio.get(command.sitioId)

        credencial.usuario = command.usuario
        credencial.password = command.password
        credencial.sitio = sitio

        if (accessRulesService.isSuperAdmin()) {
            assert command.oficinaId != null && command.oficinaId != "", "Debe seleccionar una oficinafinerror"
            Oficina oficina = Oficina.get(command.oficinaId)
            credencial.oficina = oficina
        }
        else {            
            credencial.oficina = accessRulesService.getCurrentUser().oficina
        }

        return credencial.save(flush:true, failOnError:true)
    }

    public Credencial getCredencial(Long id) {
        return Credencial.get(id)
    }

    public Credencial update(CredencialCommand command) { 
        assert command.usuario != null && command.usuario != "": "El usuario no puede estar vacíofinerror"
        assert command.password != null && command.password != "", "La contraseña no puede estar vacíafinerror"        
        assert command.sitioId != null && command.sitioId != "", "Debe seleccionar un sitiofinerror"
        Credencial credencial = getCredencial(command.id)
        Sitio sitio = Sitio.get(command.sitioId)

        credencial.usuario = command.usuario
        credencial.password = command.password
        credencial.sitio = sitio

        if (accessRulesService.isSuperAdmin()) {
            assert command.oficinaId != null && command.oficinaId != "", "Debe seleccionar una oficinafinerror"
            Oficina oficina = Oficina.get(command.oficinaId)
            credencial.oficina = oficina
        }
        else {            
            credencial.oficina = accessRulesService.getCurrentUser().oficina
        }
        
        return credencial.save(flush:true, failOnError:true)
    }

    public Credencial delete(Long id) {
        Credencial credencial = getCredencial(id)
        credencial.delete(flush:true, failOnError:true)
    }

    def getCredencialCommand(Long id) {
        def credencial = getCredencial(id)
        def credencialCommand = new CredencialCommand()

        credencialCommand.id = credencial.id
        credencialCommand.usuario = credencial.usuario
        credencialCommand.password = credencial.password
        credencialCommand.oficinaId = credencial.oficina.id 
        credencialCommand.sitioId = credencial.sitio.id

        return credencialCommand
    }

    def List<Sitio> listSitios() {
        return Sitio.list()
    }
}