package com.tecnofind

import grails.plugin.springsecurity.SpringSecurityUtils
import org.joda.time.LocalDateTime
import com.tecnofind.UserTrack

class FirstInterceptor {
	def springSecurityService
	def usuarioService

	public FirstInterceptor() {
		matchAll()
	}

	boolean before() {
		String ipAddress = ""
		ipAddress = request.getHeader("Client-IP")
		String email = "no-user"

		def urlsNoLogueables = ['/notificacion/mailgunDeliveredEvent','/notificacion/mailgunOpensEvent','/notificacion/mailgunClicksEvent']
		def actionsSinVerificar = ['pasosRegistro','pagarMovimientosMail', 'confirmarCUIT','accesoDashboard','appGetDatosCuit','appGetImportes','guardarTokenPush', 'ajaxGetObrasSociales', 'ajaxGetNacionalidades', 'ajaxGetApps', 'ajaxGetProvincias']

		if (ipAddress == null)
			ipAddress = request.getHeader("X-Forwarded-For")

		if (ipAddress == null)
			ipAddress = request.getRemoteAddr()

		String userAgent = request.getHeader("User-Agent")

		def tieneAjax = request.forwardURI.toString().contains('ajax')
		def tieneAssets = request.forwardURI.toString().contains('/tecnofind/assets')

		if(springSecurityService.principal.getClass() == grails.plugin.springsecurity.rest.oauth.OauthUser ){
			def mail = springSecurityService.principal.userProfile.getEmail()
			if(usuarioService.existeUsuario(mail))
				springSecurityService.reauthenticate(mail)
		}
		Boolean printear = Auxiliar.testingEnviroment || ! (tieneAjax || tieneAssets)
		def ahora = new LocalDateTime()
		def parametros = "["
		params.each{
			if((it.key != 'password')&&(it.key != 'password2')){
				parametros += it.key + ":" + it.value + ","
			}
		}
		//Le quita la ultima "," luego de la impresion del mapa al string
		parametros = parametros.getAt(0..parametros.length()-2)
		parametros += "]"

		if(springSecurityService.currentUser!=null){
			def userInstance = springSecurityService.currentUser
			email = springSecurityService.currentUser.username
			
			def userTrack = new UserTrack()
			userTrack.fechaHora = ahora
			userTrack.controller = params.controller
			userTrack.action = params.action
			userTrack.params = parametros.take(2047)
			userTrack.ip = ipAddress
			userTrack.user = userInstance
			userTrack.url = request.forwardURI
			userTrack.ajax = !printear 
			userTrack.save(flush:true)
			if(params.controller!='logout'){
				if(!userInstance.hasAnyRole("ROLE_SUPER_ADMIN")){
					if (ipAddress){
						if (userInstance.oficina.ip){
							if (!ipAddress.contains(userInstance.oficina.ip)){
								println "El usuario ${userInstance.username} ha sido bloqueado. Intentó ingresar desde la IP: ${ipAddress}"
								if(params.controller!='login' && params.action!='auth'){
									redirect(controller:"login", action:"ipBlocked", params: [ip:ipAddress])
									return false
								}
							}
						}
					}
				}
			}
		}
		if(printear && !urlsNoLogueables.contains(request.forwardURI) ){
			log.info(ahora.toString("dd/MM/YYYY HH:mm:ss") + " user:" + email + " request:" + request.forwardURI + " ip:" + ipAddress + " params: $parametros")
			println  ahora.toString("\ndd/MM/YYYY HH:mm:ss") + " user:" + email + " request:" + request.forwardURI + " ip:" + ipAddress + " params: $parametros"
		}
		
		return true
	}

	boolean after() { true }

	void afterView() {
		// no-op
	}
}