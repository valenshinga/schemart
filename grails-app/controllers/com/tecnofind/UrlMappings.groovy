package com.tecnofind

class UrlMappings {

    static mappings = {
        "/api/$controller/$action?/$id?"()

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "login", action: "index")
		"/index"(controller: "login", action: "index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
