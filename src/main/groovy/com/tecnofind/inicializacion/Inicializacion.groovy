package com.tecnofind.inicializacion

import com.tecnofind.Estado
import com.tecnofind.ProxyTecnofind
import com.tecnofind.ItemMenu
import com.tecnofind.Role
import com.tecnofind.User
import com.tecnofind.UserRole
import com.tecnofind.busqueda.Busqueda
import com.tecnofind.busqueda.TipoDato
import com.tecnofind.credencial.Credencial
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.Poblacion
import com.tecnofind.domicilio.Provincia
import com.tecnofind.oficina.Oficina
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.Nombre
import com.tecnofind.persona.Persona
import com.tecnofind.persona.PersonaRelacionada
import com.tecnofind.persona.Telefono
import com.tecnofind.persona.TipoRelacion
import com.tecnofind.sitio.Sitio

import grails.gorm.multitenancy.Tenant
import java.nio.charset.Charset
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import static grails.gorm.multitenancy.Tenants.*

class Inicializacion {
	static def comienzo(){
		println "######################################################################"
		println "Verificando datos del sistema generales..."
        inicializarRoles()
        inicializarOficinas()
		inicializarUsuarios()
        inicializarSitios()
        inicializarTipoDato()
        inicializarEstado()
        inicializarTipoRelaciones()
        inicializarMenues()
        inicializarCredenciales()
        inicializarProxys()
	}

    private static void inicializarRoles() {
        println "Inicializando roles"
        def roles = [
            ["authority": "ROLE_SUPER_ADMIN"],
            ["authority": "ROLE_ADMIN"],
            ["authority": "ROLE_USER"]
        ]
        roles.each { rol ->
            if(Role.findByAuthority(rol.authority)) return
            new Role(rol).save(flush: true, failOnError: true)
        }
    }

	private static void inicializarUsuarios() {
        println "Inicializando usuarios"
        def usuarios = [
            ["username": "lcarera04@gmail.com", "password": "calim123", "nombre": "Programadores Calim","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_SUPER_ADMIN')]],
            ["username": "programadores@calim.com.ar", "password": "calim123", "nombre": "Programadores Calim","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_SUPER_ADMIN')]],
            ["username": "testeradmin@calim.com.ar", "password": "calim123", "nombre": "Programadores Calim","enabled": true, "accountExpired": false,
             "accountLocked": false, "passwordExpired": false, "roles": [Role.findByAuthority('ROLE_ADMIN')]],
            ["username": "testeruser@calim.com.ar", "password": "calim123", "nombre": "Programadores Calim","enabled": true, "accountExpired": false,
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

    private static void inicializarSitios(){
        println "Inicializando sitios"
        def sitios = [
        ["nombre": "Inglobaly", "url": "https://www.inglobaly.com/index.jsf"],
        ["nombre": "Bittools", "url": "https://www.bittools.es/"],
        ["nombre": "Abcteléfonos", "url": "https://www.abctelefonos.com/?country=espana"],
        ["nombre": "Telexplorer", "url": "https://www.telexplorer.com.es/"],
        ["nombre": "Infoempresa", "url": "https://www.infoempresa.com/es-es/es/?gclid=EAIaIQobChMIgoy03czBgwMVnEVIAB1XowCyEAAYASAAEgJ9CfD_BwE"],

        ]
        sitios.each { sitio ->
            if(Sitio.findByNombre(sitio.nombre)) return
            new Sitio(sitio).save(flush: true, failOnError: true)
        }
    }

    private static void inicializarMenues(){
        println "Inicializando menues"
        Role superAdmin = Role.findByAuthority('ROLE_SUPER_ADMIN')
        Role admin = Role.findByAuthority('ROLE_ADMIN')
		Role user = Role.findByAuthority('ROLE_USER')
        def nuevos = [
            [   nombre: 'Buscar',
                icono: 'icofont icofont-search',
                controller: 'busqueda',
                action: 'create',
                roles: [admin, user, superAdmin],
                tenants: [1]
            ],[ nombre: 'Personas',
                icono: 'icofont icofont-user-alt-4',
                controller: 'persona',
                action: 'list',
                roles: [superAdmin, admin, user],
                tenants: [1]
            ],[ nombre: 'Historial',
                icono: 'icofont icofont-clock-time',
                controller: 'busqueda',
                action: 'list',
                roles: [superAdmin, admin, user],
                tenants: [1]
            ],
            [ nombre: 'Oficinas',
                icono: 'icofont icofont-building-alt',
                controller: 'oficina',
                action: 'list',
                roles: [superAdmin],
                tenants: [1]
            ],[ nombre: 'Usuarios',
                icono: 'icofont icofont-users-alt-3',
                controller: 'usuario',
                action: 'list',
                roles: [superAdmin, admin],
                tenants: [1]
            ],[ nombre: 'Tipos de relación',
                icono: 'icofont icofont-ui-social-link',
                controller: 'tipoRelacion',
                action: 'list',
                roles: [superAdmin, admin],
                tenants: [1]
            ],[ nombre: 'Credenciales',
                icono: 'icofont icofont-key',
                controller: 'credencial',
                action: 'list',
                roles: [superAdmin, admin],
                tenants: [1]
            ],[ nombre: 'Sinónimos',
                icono: 'icofont icofont-book-alt',
                controller: 'nombre',
                action: 'list',
                roles: [superAdmin, admin, user],
                tenants: [1]
            ]]
        nuevos.each{ menu ->
			int orden = 10
			menu.tenants.each{tenant ->
				grails.gorm.multitenancy.Tenants.withId(tenant){
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
				}
			}
			orden+=10
		}
    }

    private static void inicializarTipoDato(){
        println "Inicializando tipo de datos"
        def tipos = [
        ["nombre": "Dirección"],
        ["nombre": "Nombre"],
        ["nombre": "Teléfono"],
        ["nombre": "Documento"]
        ]
        tipos.each { tipo ->
            if(TipoDato.findByNombre(tipo.nombre)) return
            new TipoDato(tipo).save(flush: true, failOnError: true)
        }
    }

    private static void inicializarEstado(){
        println "Inicializando estados"
        def estados = [
        ["nombre": "Sin Confirmar"],
        ["nombre": "Confirmado"],
        ["nombre": "Erróneo"]
        ]
        estados.each { estado ->
            if(Estado.findByNombre(estado.nombre)) return
            new Estado(estado).save(flush: true, failOnError: true)
        }
    }

    private static void inicializarOficinas() {
        println "Inicializando oficinas"
        def oficinas = [
            ["nombre": "Oficina Central", "ip": 2020],
            ["nombre": "Oficina Secundaria", "ip": 2021]
        ]

        oficinas.each { oficina ->
            if (Oficina.findByNombre(oficina.nombre)) return
            def oficinaInstance = new Oficina(oficina).save(flush: true, failOnError: true)
        }
    }

    private static void inicializarTipoRelaciones() {
        println "Inicializando tipos de relaciones"
        def tiposRelaciones = [
            ["nombre": "Vecino"],
            ["nombre": "Anterior Vecino"],
            ["nombre": "Conviviente"],
            ["nombre": "Compañero de Trabajo"],
            ["nombre": "Familiar"]
        ]
        tiposRelaciones.each { relacion ->
            if (TipoRelacion.findByNombre(relacion.nombre)) return
            def relacionInstance = new TipoRelacion(relacion).save(flush: true, failOnError: true)

        }
    }

    private static void inicializarCredenciales() {
        println "Inicializando credenciales"

        def credenciales = [
            ["usuario":"luisdemarco@hotmail.es", "password": "Luis3478", "sitio": Sitio.findByNombre("Inglobaly"), "oficina": Oficina.findByIp("2020")],
            ["usuario":"baadh", "password": "1234", "sitio": Sitio.findByNombre("Bittools"), "oficina": Oficina.findByIp("2020")]
        ]
        credenciales.each { credencial ->
            if (Credencial.findByUsuario(credencial.usuario)) return
            new Credencial(credencial).save(flush: true, failOnError: true)
        }
    }

    private static void inicializarProxys() {
        println "Inicializando proxys"

        def proxys = [
            ["ip": "71.4.186.38", "puerto": 21282, "habilitado": true],
            ["ip": "208.53.20.52", "puerto": 21286, "habilitado": true],
            ["ip": "162.251.71.173", "puerto": 21312, "habilitado": true],
            ["ip": "64.44.138.78", "puerto": 21311, "habilitado": true],
            ["ip": "23.27.185.237", "puerto": 21241, "habilitado": false]
        ]
        proxys.each { proxy ->
            if (ProxyTecnofind.findByIp(proxy.ip)) return
            new ProxyTecnofind(proxy).save(flush: true, failOnError: true)
        }
    }
}