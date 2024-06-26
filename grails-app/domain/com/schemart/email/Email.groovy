package com.schemart.email

import com.schemart.User
import org.joda.time.LocalDateTime

class Email {

    String emisor
    String receptor
    String asunto
    String mensaje

    static constraints = {
        receptor nullable: false
        emisor nullable: false
        asunto nullable: false
        mensaje nullable: false
    }
} 