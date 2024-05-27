package com.tecnofind.email

import com.tecnofind.User
import org.joda.time.LocalDateTime
import com.tecnofind.User

class Email {

    User receptor
    String emailReceptor
    String asunto
    String view
    Map<String, Object> parametros

    static constraints = {
        receptor nullable: true
        emailReceptor nullable: false, blank: false
        asunto nullable: false
        view nullable: true
        parametros nullable: true
    }
} 