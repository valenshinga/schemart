grails.gorm.default.mapping = {
	'user-type' (type: org.jadira.usertype.dateandtime.joda.PersistentLocalDate, class: org.joda.time.LocalDate)
	'user-type' (type: org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime, class: org.joda.time.LocalDateTime)
}

jodatime.format.html5 = true
jodatime.format.org.joda.time.DateTime='dd/MM/yyyy HH:mm:ss z ZZ'
jodatime.format.org.joda.time.LocalDate='dd/MM/yyyy'
jodatime.format.org.joda.time.LocalDateTime='dd/MM/yyyy HH:mm:ss'

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.tecnofind.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.tecnofind.UserRole'
grails.plugin.springsecurity.authority.className = 'com.tecnofind.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/.well-known/**', access: ['permitAll']],
	[pattern: '/static/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/login/**',   access: ['permitAll']],
	[pattern: '/register/**',   access: ['permitAll']],
	[pattern: '/logout/**',   access: ['permitAll']],
	[pattern: '/**/assets/**',   access: ['permitAll']],
	[pattern: '/oauth/**',       access: ['permitAll']],
	[pattern: '/springSecurityOAuth/**',       access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/favicon.ico', access: ['permitAll']],
	[pattern: '/api/login', access: ['permitAll']],
	[pattern: '/api/**', access: ['isAuthenticated()']]
]


grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/.well-known/**', filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/favicon.ico', filters: 'none'],
    //Stateless chain
    [ pattern: '/api/**', filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],

    //Traditional chain
    [ pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']
]
grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = false

grails {
	mail {
		host = "smtp.mailgun.org"
		port = 587
		username = "notificaciones@tecnofind.es"
		password = "2602528f8462dce5d9d4416896ab7570-4c955d28-325030b4" // API KEY 82f85cc59b7b2bf5d701871d16fa2c1c-4c955d28-c37ef29d
		props = ["mail.smtp.auth":"true",
				"mail.smtp.socketFactory.port":"465",
				"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				"mail.smtp.socketFactory.fallback":"false"]
	}
}

grails {
	plugin {
		springsecurity {
			rest {
				token {
					validation {
						useBearerToken = false 
						enableAnonymousAccess = true
						headerName = 'X-Auth-Token'
					}
					storage {
						jwt {
							secret = 'foobar123'*4 
							expiration = 2678400
						}
					}
				}
				oauth {
					frontendCallbackUrl = { String tokenValue -> "https://app.calim.com.ar/auth/success?token=${tokenValue}" } 
					google {
						client = org.pac4j.oauth.client.Google2Client 
						key = '93928074758-p687srmuti88m9ud6k16ngaeenglac5n.apps.googleusercontent.com' 
						secret = 'p3IF_jei8YIv2FF7nYjqPBTM' 
						scope = org.pac4j.oauth.client.Google2Client.Google2Scope.EMAIL_AND_PROFILE 
						defaultRoles = ['ROLE_CUENTA'] 
					}
				}
			}
			providerNames = ['daoAuthenticationProvider', 'anonymousAuthenticationProvider'] 
		}
	}
}

grails.plugin.springsecurity.logout.handlerNames = ['rememberMeServices', 'securityContextLogoutHandler', 'cookieClearingLogoutHandler']
