package com.schemart

import grails.plugin.springsecurity.SpringSecurityUtils
import org.joda.time.LocalDateTime

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
		def userInstance = springSecurityService.currentUser
		
		if (userInstance){
			email = springSecurityService.currentUser.username
		}

		// def urlsNoLogueables = ['/notificacion/mailgunDeliveredEvent','/notificacion/mailgunOpensEvent','/notificacion/mailgunClicksEvent']
		// def actionsSinVerificar = ['pasosRegistro','pagarMovimientosMail', 'confirmarCUIT','accesoDashboard','appGetDatosCuit','appGetImportes','guardarTokenPush', 'ajaxGetObrasSociales', 'ajaxGetNacionalidades', 'ajaxGetApps', 'ajaxGetProvincias']

		if (ipAddress == null)
			ipAddress = request.getHeader("X-Forwarded-For")

		if (ipAddress == null)
			ipAddress = request.getRemoteAddr()

		String userAgent = request.getHeader("User-Agent")

		def tieneAjax = request.forwardURI.toString().contains('ajax')
		def tieneAssets = request.forwardURI.toString().contains('/schemart/assets')

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

		if(printear){
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