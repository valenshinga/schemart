package com.schemart.inicializacion

import com.schemart.ItemMenu
import com.schemart.Role
import com.schemart.User
import com.schemart.UserRole

import java.nio.charset.Charset
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class Inicializacion {
	static def comienzo(){
		println "######################################################################"
		println "Verificando datos del sistema generales..."
        inicializarRoles()
		inicializarUsuarios()
        inicializarMenues()
	}

    private static void inicializarRoles() {
        println "Inicializando roles"
        def roles = [
            ["authority": "ROLE_SUPER_ADMIN"],
            ["authority": "ROLE_ADMIN"],
            ["authority": "ROLE_DOCENTE"],
			["authority": "ROLE_DIRECCION"],
			["authority": "ROLE_ADMINISTRACION"]
        ]
        roles.each { rol ->
            if(Role.findByAuthority(rol.authority)) return
            new Role(rol).save(flush: true, failOnError: true)
        }
    }

	private static void inicializarUsuarios() {
        println "Inicializando usuarios"
        def usuarios = [
            ["username": "valentinshingaki@gmail.com", "password": "schemart123", "nombre": "Programadores Huergo","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_SUPER_ADMIN')]],
            ["username": "testeradmin@gmail.com", "password": "schemart123", "nombre": "Programadores Huergo","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_ADMIN')]],
            ["username": "testeruser@gmail.com", "password": "schemart123", "nombre": "Programadores Huergo","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_USER')]]
        ]
        usuarios.each { usuario ->
            if(User.findByUsername(usuario.username)) return
            def user = new User(usuario).save(flush: true, failOnError: true)
            usuario.roles.each { rol ->
                UserRole.create(user, rol, true)
            }
        }
    }

    private static void inicializarMenues(){
        println "Inicializando menues"
        Role superAdmin = Role.findByAuthority('ROLE_SUPER_ADMIN')
        Role admin = Role.findByAuthority('ROLE_ADMIN')
		Role user = Role.findByAuthority('ROLE_USER')
		Role docente = Role.findByAuthority('ROLE_DOCENTE')
        def nuevos = [
            [   nombre: 'Buscar',
                icono: 'icofont icofont-search',
                controller: 'busqueda',
                action: 'buscar',
                roles: [admin, user, superAdmin, docente],
            ]]
        nuevos.each{ menu ->
			int orden = 10
			def nodo = ItemMenu.findByPadreAndNombreAndIconoAndControllerAndAction(null, menu.nombre, menu.icono, menu.controller, menu.action)
			if (!nodo){
				nodo = new ItemMenu(tipo: 'PRINCIPAL', nombre: menu.nombre, controller: menu.controller, action: menu.action, icono: menu.icono, orden: orden, padre: null, roles: menu.roles).save(flush:true, failOnError:true)
				println "	Menú $nodo creado"
			}else {
				if (nodo.orden != orden){
					nodo.orden = orden
					nodo.save(flush:true)
					println "	Actualizado orden para Menú $nodo"
				}
				if (nodo.roles as Set != menu.roles as Set){
					nodo.roles = menu.roles
					nodo.save(flush:true)
					println "	Actualizado roles para Menú $nodo"
				}
			}
			int ordenHijos = 10
			def menuesHijos = ItemMenu.findAllByPadre(nodo)
			menu.hijos.findAll{ it.tenants.contains(tenant)}.each{ hijo ->
				def imhijo = menuesHijos.find{it.nombre == hijo.nombre && it.controller == hijo.controller && it.action == hijo.action}
				if (!imhijo){
					imhijo = new ItemMenu(tipo: 'PRINCIPAL', nombre: hijo.nombre, controller: hijo.controller, action: hijo.action, orden: ordenHijos, padre: nodo, roles: hijo.roles).save(flush:true, failOnError:true)
					println "		Menú $imhijo creado"
					menuesHijos.removeElement(imhijo)
				}else{
					if (imhijo.orden != ordenHijos){
						imhijo.orden = ordenHijos
						imhijo.save(flush:true)
						println "		Actualizado orden para Menú $imhijo"
					}
					if (imhijo.roles as Set != hijo.roles as Set){
						imhijo.roles = hijo.roles
						imhijo.save(flush:true)
						println "		Actualizado roles para Menú $imhijo"
					}
					menuesHijos.removeElement(imhijo)
				}
				ordenHijos+=10
			}
			menuesHijos.each{
				it.delete(flush:true)
				println "		ELIMINANDO viejo Menú $it"
			}
			orden+=10
		}
    }
}