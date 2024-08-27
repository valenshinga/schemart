package com.schemart.start

import com.schemart.AccessRulesService
import com.schemart.Role
import com.schemart.User
import com.schemart.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class StartController {
	AccessRulesService accessRulesService

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def index() {
		User userInstance = accessRulesService.getCurrentUser()

		redirect(controller:"busqueda", action:'index')
	}
}