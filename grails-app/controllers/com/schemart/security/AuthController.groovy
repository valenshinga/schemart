package com.schemart.security

import com.schemart.JwtCookieTokenReader

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.rest.oauth.OauthUser
import grails.plugin.springsecurity.rest.oauth.OauthUserDetailsService
import grails.plugin.springsecurity.rest.token.reader.TokenReader
import groovy.util.logging.Slf4j
import javax.servlet.http.Cookie
import org.pac4j.core.profile.CommonProfile
import org.springframework.security.core.GrantedAuthority
import grails.converters.JSON
import com.schemart.Auxiliar
import com.schemart.User

@Slf4j
class AuthController implements GrailsConfigurationAware {

    String grailsServerUrl
    TokenReader tokenReader
    def springSecurityService
    int jwtExpiration

    static allowedMethods = [
            success: 'GET',
            logout: 'POST'
    ]

    protected Cookie jwtCookie(String tokenValue) { 
        Cookie jwtCookie = new Cookie( cookieName(), tokenValue )
        jwtCookie.maxAge = jwtExpiration 
        jwtCookie.path = '/'
        jwtCookie.setHttpOnly(true) 
        if ( httpOnly() ) {
            jwtCookie.setSecure(true) 
        }
        jwtCookie
    }

    @Override
    void setConfiguration(Config co) {
        jwtExpiration = co.getProperty('grails.plugin.springsecurity.rest.token.storage.memcached.expiration', Integer, 3600) 
        grailsServerUrl = co.getProperty('grails.serverURL', String)
    }

    protected boolean httpOnly() {
        grailsServerUrl?.startsWith('https')
    }

}