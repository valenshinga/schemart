package spring

import com.schemart.UserPasswordEncoderListener
import com.schemart.JwtCookieTokenReader
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler

// Place your Spring DSL code here
beans = {

    userPasswordEncoderListener(UserPasswordEncoderListener)
    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        defaultLocale = new java.util.Locale('es')
    }

    tokenReader(JwtCookieTokenReader) {
        cookieName = 'jwt'
    }

    cookieClearingLogoutHandler(CookieClearingLogoutHandler, ['jwt'])

}
