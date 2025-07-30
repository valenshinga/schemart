package com.schemart.inicializacion

import com.schemart.ItemMenu
import com.schemart.Role
import com.schemart.User
import com.schemart.Estado
import com.schemart.UserRole
import com.schemart.alumno.TipoCurso
import com.schemart.idioma.Idioma

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
		inicializarTiposCursos()
		inicializarIdiomas()
	}

	private static void inicializarEstados() {
		println "Inicializando estados"
		["Activo", "Inactivo", "Confirmado", "Pendiente", "Programada", "Dictada", "Suspendida"].each { estado ->
			if (! Estado.findByNombre(estado)){
				new Estado(nombre:estado).save(flush:true)
				println "    Estado $estado creado"
			}
		}
	}

	private static void inicializarIdiomas() {
		println "Inicializando idiomas"
		def idiomas = [
			["nombre": "Español", "nivel": "A1"], ["nombre": "Español", "nivel": "A2"],
			["nombre": "Español", "nivel": "B1"], ["nombre": "Español", "nivel": "B2"],
			["nombre": "Español", "nivel": "C1"], ["nombre": "Español", "nivel": "C2"],
		]
		idiomas.each { idioma ->
			if (! Idioma.findByNombreAndNivel(idioma.nombre, idioma.nivel)){
				new Idioma(nombre:idioma.nombre, nivel:idioma.nivel).save(flush:true)
				println "    Idioma $idioma.nombre $idioma.nivel creado"
			}
		}
	}

	private static void inicializarTiposCursos() {
		println "Inicializando tipos de cursos"
		["Grupal", "Individual", "Empresa"].each { tipo ->
			if (! TipoCurso.findByNombre(tipo)){
				new TipoCurso(nombre:tipo).save(flush:true)
				println "    TipoCurso $tipo creado"
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
			["username": "valentinshingaki@gmail.com", "password": "schemart123", "nombre": "Programadores","enabled": true, "accountExpired": false,
			 "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_SUPER_ADMIN')]],
			["username": "testeradmin@gmail.com", "password": "schemart123", "nombre": "Programadores","enabled": true, "accountExpired": false,
			 "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_ADMIN')]],
			["username": "testeruser@gmail.com", "password": "schemart123", "nombre": "Programadores","enabled": true, "accountExpired": false,
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
						controller: 'alumno',
						action: 'list',
						roles: [admin, superAdmin, docente, administracion]
					],
					[
						nombre: 'Idiomas',
						controller: 'idioma',
						action: 'list',
						roles: [admin, superAdmin, docente, administracion]
					],
					// [
					// 	nombre: 'Usuarios',
					// 	// controller: '',
					// 	// action: '',
					// 	roles: [admin, superAdmin]
					// ]
				]
			],
			[   nombre: 'Clases',
				icono: 'icofont icofont-book',
				roles: [admin, superAdmin, docente],
				hijos: [
					[
						nombre: 'Clases',
						controller: 'clase',
						action: 'list',
						roles: [admin, superAdmin, docente]
					],
				]
			],]
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