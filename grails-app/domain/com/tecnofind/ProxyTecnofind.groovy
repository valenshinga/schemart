package com.tecnofind

class ProxyTecnofind {
	String ip
	Integer puerto
	Boolean habilitado
    
	static constraints = {
	}

	public String toString() {
		return "${ip}:$puerto"
	}
}
