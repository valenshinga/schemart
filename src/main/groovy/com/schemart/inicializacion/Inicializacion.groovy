package com.schemart.inicializacion

import com.schemart.ItemMenu
import com.schemart.Role
import com.schemart.User
import com.schemart.Estado
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
		inicializarEstados()
        inicializarMenues()
	}

	private static void inicializarEstados() {
        println "Inicializando estados"
        ["Activo", "Inactivo", "Confirmado", "Pendiente"].each { estado ->
            if (! Estado.findByNombre(estado)){
				new Estado(nombre:estado).save(flush:true)
				println "    Estado $estado creado"
			}
        }
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
		Role administracion = Role.findByAuthority('ADMINISTRACION')
		Role docente = Role.findByAuthority('ROLE_DOCENTE')
        def nuevos = [
			[
				nombre: 'Dashboard',
                icono: 'icofont icofont-ui-home',
                roles: [admin, superAdmin, docente, administracion]
			],
			[   nombre: 'Gestión',
                icono: 'icofont icofont-options',
                roles: [admin, superAdmin, docente, administracion],
				hijos: [
					[
						nombre: 'Empleados',
						controller: 'empleado',
						action: 'list',
						roles: [admin, superAdmin, administracion]
					],
					[
						nombre: 'Alumnos',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente, administracion]
					],
					[
						nombre: 'Horarios',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente, administracion]
					],
					[
						nombre: 'Eventos',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente, administracion]
					],
					[
						nombre: 'Usuarios',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin]
					]
				]
            ],
			[   nombre: 'Clases',
                icono: 'icofont icofont-book',
                roles: [admin, superAdmin, docente],
				hijos: [
					[
						nombre: 'Clases',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente]
					],
					[
						nombre: 'Notificaciones',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente]
					],
					[
						nombre: 'Seguimiento',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente]
					]
				]
            ],
			[   nombre: 'Reportes',
                icono: 'icofont icofont-chart-bar-graph',
                roles: [admin, superAdmin, docente, administracion],
				hijos: [
					[
						nombre: 'General',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente, administracion]
					],
					[
						nombre: 'Asistencia',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, docente]
					],
					[
						nombre: 'Facturación',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, administracion]
					],
					[
						nombre: 'Liquidación',
						// controller: '',
						// action: '',
						roles: [admin, superAdmin, administracion]
					]
				]
            ]]
        nuevos.each{ menu ->
			int orden = 10
			println "nombre $menu.nombre, icono $menu.icono, controller $menu.controller, action $menu.action"
			def nodo = ItemMenu.findByPadreAndNombreAndIconoAndControllerAndAction(null, menu.nombre, menu.icono, menu.controller, menu.action)
			println "Este es el nodo encontrado $nodo"
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
			menu.hijos.each{ hijo ->
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