package com.schemart

class Estado {
	String nombre
	
    static constraints = {
		nombre nullable:false
    }
	
	public String toString() {
		return "Id: "+ id.toString() + ' - ' + nombre
	}
}