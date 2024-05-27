package com.tecnofind

import org.joda.time.LocalDateTime
import com.tecnofind.User

class UserTrack {
	LocalDateTime fechaHora
	User user
	String ip
	String action
	String controller
	String url
	String params
	Boolean ajax

	static constraints = {
		fechaHora nullable:false
		user nullable:true
		ip nullable:true
		action nullable:true
		controller nullable:true
		url nullable:true
		params maxSize:2048, nullable:true
		ajax nullable:true
	}
}