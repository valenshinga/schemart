grails.gorm.default.mapping = {
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
	"user-type" type: org.jadira.usertype.dateandtime.joda.PersistentLocalDate, class: org.joda.time.LocalDate
}

jodatime.format.html5 = true
jodatime.format.org.joda.time.DateTime='dd/MM/yyyy HH:mm:ss z ZZ'
jodatime.format.org.joda.time.LocalDate='dd/MM/yyyy'
jodatime.format.org.joda.time.LocalDateTime='dd/MM/yyyy HH:mm:ss'

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.schemart.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.schemart.UserRole'
grails.plugin.springsecurity.authority.className = 'com.schemart.Role'
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

// grails {
// 	mail {
// 		host = "smtp.mailgun.org"
// 		port = 587
// 		username = ""
// 		password = 
// 		props = ["mail.smtp.auth":"true",
// 				"mail.smtp.socketFactory.port":"465",
// 				"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
// 				"mail.smtp.socketFactory.fallback":"false"]
// 	}
// }

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
			}
			providerNames = ['daoAuthenticationProvider', 'anonymousAuthenticationProvider'] 
		}
	}
}

grails.plugin.springsecurity.logout.handlerNames = ['rememberMeServices', 'securityContextLogoutHandler', 'cookieClearingLogoutHandler']
