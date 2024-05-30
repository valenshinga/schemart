package com.schemart

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.schemart.AccessRulesService
import com.schemart.notificacion.NotificacionService
import grails.plugin.springsecurity.ui.RegistrationCode


@Secured(['ROLE_SUPER_ADMIN', 'ROLE_ADMIN'])
class UsuarioController {

        def index(){
            render 'hola'
        }

//     def usuarioService
//     def accessRulesService
//     def notificacionService
//     def springSecurityService

//     def index() {
//         redirect (action: 'list')
//     }

//     def list(){

//     }

//     def create() {  
//     }

//     def save(UserCommand command) {
//         try{
//             String token = params.t
//             usuarioService.save(command, token)
//             flash.message = "Se envió un email para completar la contraseña"
//             redirect(action: "list")
//         }       
//         catch(AssertionError e) {
//             Auxiliar.printearError e
//             flash.error = e.message.split("finerror")[0]
//             render (view: "create", model: [UserCommand: command])
//         }
//         catch(Exception e){
//             flash.error = "Error al guardar el usuario"
//             Auxiliar.printearError e
//             render (view: "create", model: [UserCommand: command])
//         }
//     }

    def edit(Long id) {
        [userCommand: usuarioService.getUserCommand(id)]
    }

//     def update(UserCommand command) {
//         try{
//             if (command.hasErrors()){
//                 flash.error = "Error al modificar el usuario, verifique que haya completado todos los campos correctamente."
//                 render(view: "edit")
//                 return
//             }
//             usuarioService.update(command)
//             flash.message = "Usuario modificado correctamente!"
//             redirect(action: "list")
//         }
//         catch(AssertionError e) {
//             Auxiliar.printearError e
//             flash.error = e.message.split("finerror")[0]
//             render (view: "edit", model: [userCommand: command])
//         }
//         catch(Exception e){
//             flash.error = "Error al modificar el usuario."
//             Auxiliar.printearError e
//             render (view: "edit", model: [userCommand: command])
//         }
    
//     }

//     def delete(Long id) {
//         try{
//             usuarioService.delete(id)
//             flash.message = "Usuario borrado!"
//             redirect(action: "list")
//         }
//         catch(AssertionError e) {
//             Auxiliar.printearError e
//             flash.error = e.message.split("finerror")[0]
//             render (view: "edit", model: [userCommand: command])
//         }
//         catch(Exception e){
//             flash.error = "Error al borrar el usuario."
//             Auxiliar.printearError e
//             render (view: "edit", model: [userCommand: command])
//         }
//     }

//     def ajaxGetUsuarios() {
//         if (accessRulesService.isSuperAdmin()) {
//             def users = usuarioService.listUsers()
//             render users as JSON
//         } else {          
//             def users = usuarioService.listUsersbyOficina()
//             render users as JSON
//         }
//     }

//     @Secured('permitAll')
//     def resetPassword() {
//         render (view: "resetPassword", model: [username: params.username ])
//     }

//     @Secured('permitAll')
//     def editPassword() {
//         try{
//             springSecurityService.reauthenticate(params.username)
//             def user = User.findByUsername(params.username)
//             if (!user.enabled) {
// 			flash.error = "Usuario bloqueado"
// 			redirect (action: "list")
// 			return
// 		    }
//             usuarioService.updatePassword( params.username ,params.password1, params.password2)
//             flash.message = "Contraseña modificada correctamente!"
//             redirect(action: "list")
//         }
//         catch(AssertionError e) {
//             Auxiliar.printearError e
//             flash.error = e.message.split("finerror")[0]
//             render (view: "resetPassword", model: [username: params.username, password1: params.password1, password2: params.password2])
//         }
//         catch(Exception e){
//             flash.error = "Error al modificar la contraseña."
//             Auxiliar.printearError e
//             render (view: "resetPassword", model: [username: params.username, password1: params.password1, password2: params.password2])
//         }
//     }

// //Metodo para cuando se edite la contraseña en edit usuario.
// /*     def forgotPassword() {
// 		def registrationCode = RegistrationCode.findByUsername(params.userCommand.username) : null
// 		String salt = saltSource instanceof NullSaltSource ? null : registrationCode.username?.toLowerCase()
// 		RegistrationCode.withTransaction { status ->
// 			def user = User.findByUsername(registrationCode.username?.toLowerCase())
// 			user.passwordExpired = false
// 			user.password = command.password//springSecurityService.encodePassword(command.password, registrationCode.username)
// 			user.save()
// 			registrationCode.delete()
// 		}
// 	} */

//     def ajaxGetRoles() {
//         if (accessRulesService.isSuperAdmin()) {
//             def roles = usuarioService.listRoles()
//             render roles as JSON
//         } else {          
//             def roles = usuarioService.listRoles().findAll { it.authority != "ROLE_SUPER_ADMIN" }
//             render roles as JSON
//         }
//     }
}
