package com.schemart

import com.schemart.Role
import com.schemart.UserRole

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User {
	
	private static final long serialVersionUID = 1

	String username
	public void setUsername(String username){
		this.username = username.toLowerCase()
	}
	String password
	String nombre
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
		username blank: false, unique: true, email:true
		password blank: false
		nombre blank: false
	}
	
	static mapping = {
		table 'users'
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		(UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
	}
	
	public boolean hasRole(String roleBuscado){
		def role = Role.findByAuthority(roleBuscado)
		def userRole = UserRole.findByUserAndRole(this, role)
		return userRole ? true : false
	}

	public boolean hasAnyRole(String... roles) {
		roles.any { role ->
			hasRole(role as String)
		}
	}

}
