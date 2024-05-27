package com.tecnofind.start

import com.tecnofind.AccessRulesService
import com.tecnofind.Role
import com.tecnofind.User
import com.tecnofind.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.joda.time.LocalDateTime

class StartController {
	AccessRulesService accessRulesService

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def index() {
		User userInstance = accessRulesService.getCurrentUser()

		redirect(controller:"busqueda", action:'index')
	}
}