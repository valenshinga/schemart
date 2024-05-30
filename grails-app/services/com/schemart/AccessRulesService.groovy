package com.schemart

import grails.plugin.springsecurity.SpringSecurityService
import com.schemart.User

class AccessRulesService {

    def springSecurityService
	
	def getCurrentUser(){
		def user = springSecurityService.currentUser
		if(user!=null)
			user = User.get(user.id)
		/*
		if(!isAdminRole(user)){
			user = User.get(user.id)
		}else{
			user = Admin.get(user.id)
		}
		*/
		user
	}
	
	def isAdmin(){
		def user = getCurrentUser()
		if(!isAdminRole(user)){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean isAdminRole(user){
		return user.hasRole('ROLE_ADMIN')
	}

	def isSuperAdmin(){
		def user = getCurrentUser()
		if(!isSuperAdminRole(user)){
			return false;
		}else{
			return true;
		}
	}

	private boolean isSuperAdminRole(user){
		return user.hasRole('ROLE_SUPER_ADMIN')
	}
}
