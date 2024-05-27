package com.tecnofind

import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import java.util.LinkedHashMap
import grails.plugin.springsecurity.SpringSecurityUtils

import com.tecnofind.oficina.Oficina
import com.tecnofind.notificacion.NotificacionService
import com.tecnofind.AccessRulesService
import com.tecnofind.UserRole
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.core.GrailsApplication


@Transactional
class UsuarioService {

    def sessionFactory
    def accessRulesService

    NotificacionService notificacionService

    public List<User> listUsers() {
        return User.list()
    } 

    public List<User> listUsersbyOficina() {
        def id = accessRulesService.getCurrentUser().oficina.id
        return User.createCriteria().list {
            eq("oficina.id", id)
        }
    }

    public List<Role> listRoles() {
        return Role.list()
    }
    
    public User save(UserCommand command, String token) {
        assert command.username != null && command.username != "": "El usuario no puede estar vacíofinerror"
        assert command.nombre != null && command.nombre != "", "El nombre no puede estar vacíafinerror"
        assert command.rolId != null && command.rolId != "", "Debe de seleccionar un rolfinerror"
        User user = new User()
        Role role = Role.get(command.rolId)

        if (accessRulesService.isSuperAdmin()) {
            if (command.oficinaId != null) {
                Oficina oficina = Oficina.get(command.oficinaId)
                user.oficina = oficina
            }
            else {
                user.oficina = null
            }
        }
        else {            
            user.oficina = accessRulesService.getCurrentUser().oficina
        }

        user.username = command.username
        user.password = "calim"
        user.accountExpired = false
        user.passwordExpired = false
        user.enabled = true
        user.userTenantId = 1
        user.nombre = command.nombre
        user.accountLocked = command.accountLocked
        
        user.save(flush:true, failOnError:true)
        UserRole.create(user, role, true)
        def registrationCode = new RegistrationCode(username: user.username)
        registrationCode.save(flush: true)
        notificacionService.enviarMailReseteoPassword(user.username, registrationCode.token)
        return user

        /* Tenemos que mandar un mail al nuevo usuario para que cree su contraseña
        por ahora esta hardcodeada. */
    }

    public User getUser(Long id) {
        return User.get(id)
    }

    public User update(UserCommand command) {
        assert command.username != null && command.username != "": "El usuario no puede estar vacíofinerror"
        assert command.nombre != null && command.nombre != "", "El nombre no puede estar vacíofinerror"
        User user = User.get(command.id)
        Role role = Role.get(command.rolId)

        if (accessRulesService.isSuperAdmin()) {
            if (command.oficinaId != null) {
                Oficina oficina = Oficina.get(command.oficinaId)
                user.oficina = oficina
            }
            else {
                user.oficina = null
            }
        }
        else {            
            user.oficina = accessRulesService.getCurrentUser().oficina
        }

        user.username = command.username   
        user.nombre = command.nombre
        user.accountLocked = command.accountLocked
        user.save(flush:true, failOnError:true)

        UserRole oldUserRole = UserRole.get(user.id, role.id)
        if (oldUserRole.role.id != role.id){
            UserRole.updateRole(user, role, true)
        } 
        return user
    }

    public User updatePassword(String username, String password1, String password2) {
        if (password1 != password2) {
            assert false: "Las contraseñas no coincidenfinerror"
        }

        User user = User.findByUsername(username)
        user.password = password1

        return user.save(flush:true, failOnError:true)
    }

    public User delete(Long id) {
        User user = User.get(id)
        user.delete(flush:true)
        return user
    }

    def UserCommand getUserCommand(Long id) {
        def user = User.get(id)
        def userCommand = new UserCommand()

        userCommand.id = user.id
        userCommand.version = user.version
        userCommand.username = user.username 
        userCommand.nombre = user.nombre
        userCommand.accountLocked = user.accountLocked
        userCommand.oficinaId = user.oficina?.id
        userCommand.rolId = user.getAuthorities().collect{it.id}.join(', ').toLong()

        return userCommand
    }
}
