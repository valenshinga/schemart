package com.schemart.notificacion

import grails.transaction.Transactional

import grails.plugins.mail.MailService
import com.schemart.email.Email
import org.joda.time.LocalDateTime
import com.schemart.User
import com.schemart.Auxiliar
import grails.gsp.PageRenderer


@Transactional
class NotificacionService{

    // def mailService
	// def groovyPageRenderer

    // private boolean realizarEnvio(Email email, archivo = null, contenido){
	// 	boolean huboError = false;
	// 	// if (! contenido)
	// 	// 	contenido = email.html
	// 	if(Auxiliar.testingEnviromentAlternativo){
	// 		println "Enviando email a: " + email.emailReceptor
	// 		println "Asunto: " + email.asunto
	// 		println "Contenido: " + contenido
	// 		return false
	// 	}
	// 	try {
	// 		def resp = mailService.sendMail {
	// 			to email.emailReceptor
	// 			from '"Tecnofind" <notificaciones@tecnofind.es>'
	// 			subject email.asunto
	// 			html contenido
	// 			multipart true
	// 			if(archivo){
	// 				attachBytes archivo.name, "application/x-compressed", archivo.bytes
	// 			}
	// 		}
	// 		email.save(flush:true, failOnError:true)
	// 	}
	// 	catch(Exception e) {
	// 		Auxiliar.printearError(e, "Error enviando email")
	// 		huboError = true
	// 	}
	// 	return huboError
	// }

    // def enviarMailReseteoPassword(String destino, archivo=null, String token){
	// 	def email = new Email()
	// 	email.emailReceptor = destino
	// 	email.asunto = "Reestablecer contraseña"
	// 	email.receptor = User.findByUsername(destino)
	// 	email.view = "/email/setPassword"
    // 	email.parametros = [token: token, username: email.emailReceptor]
	// 	def contenido = groovyPageRenderer.render(view: "/email/setPassword", model: [token: token, username: email.emailReceptor])
	// 	email.save(flush:true, failOnError:true) // Hago el save antes del envío y actualizo dentro del mismo
	// 	realizarEnvio(email, archivo, contenido)
	// }

	// def enviarMailReportPostOrquestador(String destino, Map resultadosBusqueda, Busqueda busquedaGuardada) {
    //     def email = new Email()
	// 	email.emailReceptor = destino
	// 	email.asunto = "Reporte de Busqueda"
	// 	email.receptor = User.findByUsername(destino)
	// 	email.view = "/email/reportPostOrquestador"
	// 	String resultadosBusquedaStr = resultadosBusqueda.toString()
	// 	String mensajeError
	// 	def cantidad = 0
	// 	if(resultadosBusqueda.persona instanceof List)
	// 		cantidad += resultadosBusqueda.persona.size()
	// 	else
	// 		cantidad += 1 + (resultadosBusqueda.persona?.personasRelacionadas?.size() ?: 0)

	// 	if (resultadosBusqueda.mensajes.any { mensaje -> mensaje.contains("Ya hay resultados para la persona buscada") }) {
	// 		mensajeError = "Ya hay resultados para la persona buscada"
	// 	}
	// 	def idDePersona = resultadosBusqueda?.persona?.first()?.id
	// 	String busqueda = resultadosBusquedaStr.length() > 250 ? resultadosBusquedaStr[0..250] : resultadosBusquedaStr
	// 	email.parametros =  [busqueda: busqueda]
	// 	def contenido = groovyPageRenderer.render(view: "/email/reportPostOrquestador", model: [busquedaResultados: resultadosBusqueda,
	// 																							cantResultados: cantidad,
	// 																							busqueda: busquedaGuardada,
	// 																							mensajeError: mensajeError,
	// 																							idDePersona: idDePersona ])
	// 	email.save(flush:true, failOnError:true) // Hago el save antes del envío y actualizo dentro del mismo
	// 	realizarEnvio(email, null, contenido)
    // }

	// def enviarMailBusquedaFallida(String destino, String inputs){
	// 	def email = new Email()
	// 	email.emailReceptor = destino
	// 	email.asunto = "Búsqueda fallida"
	// 	email.receptor = User.findByUsername(destino)
	// 	email.view = "/email/busquedaFallida"
	// 	email.parametros =  [inputs: inputs]
	// 	def contenido = groovyPageRenderer.render(view: "/email/busquedaFallida", model: [inputs: inputs ])
	// 	email.save(flush:true, failOnError:true) // Hago el save antes del envío y actualizo dentro del mismo
	// 	realizarEnvio(email, null, contenido)
	// }
}