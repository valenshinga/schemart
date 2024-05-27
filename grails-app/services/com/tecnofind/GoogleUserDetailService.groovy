

package com.tecnofind

import grails.plugin.springsecurity.rest.oauth.OauthUserDetailsService
import grails.plugin.springsecurity.rest.oauth.OauthUser

import org.pac4j.core.profile.CommonProfile
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.GrantedAuthority

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

@Slf4j
@CompileStatic
class GoogleUserDetailService implements OauthUserDetailsService {

    @Delegate
    UserDetailsService userDetailsService

    UserDetailsChecker preAuthenticationChecks


    OauthUser loadUserByUserProfile(CommonProfile userProfile, Collection<GrantedAuthority> defaultRoles){

        // println "+++++++++++++++++++++++++++++++++++++++++++++++"
        // println userProfile.email
        // println "+++++++++++++++++++++++++++++++++++++++++++++++"
        //Falta preguntar si ya esta creado el usuario,si ya esta debe ingresar al sistema
     

        return new OauthUser(userProfile.id, 'N/A', defaultRoles, userProfile)

    }
}

