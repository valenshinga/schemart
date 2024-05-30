package com.schemart

import com.schemart.Auxiliar
import com.schemart.User
import com.schemart.Role


class ItemMenu {
	String tipo = ''
	
	String nombre =''
	String icono = ''
	
	String controller = ''
	String action = ''
	
	Long orden
	
	ItemMenu padre
	static hasMany = [hijos: ItemMenu, roles: Role]
	static mappedBy = [hijos: 'padre']
	
	//Esto devuelve el nodo padre del Arbol
	def public ItemMenu getRootNode(){
		if(padre){
			return padre.getRootNode()	
		}else{
			return this
		}		
	}
	
	def public boolean esHoja(){
		return hijos.isEmpty()
	}
	
	def public boolean isControllerInSun(String controller){
		boolean respuesta = false
		hijos.each{
			if(it.esHoja()){
				if(it.controller?.toLowerCase() == controller.toLowerCase())
					respuesta = true
			}else{
				respuesta = it.isControllerInSun(controller)
			}
		}
		
		return respuesta
	}
	
    static constraints = {
		tipo nullable:false
		
		nombre nullable:true
		icono nullable:true
		
		controller nullable:true
		action nullable:true
		
		padre nullable:true
    }
	
	static mapping = {
		hijos sort: "orden"
	}

	public String toString(){
		return (this.icono ? 'nodo' : 'hijo') + " $nombre"
	}

	public static getNodos(User user){
		try {
			return ItemMenu.findAllByPadreAndTipo(null, 'PRINCIPAL').findAll{!user.authorities.disjoint(it.roles)} 
		}
		catch(Exception e) {
			Auxiliar.printearError(e, "Error cargando nodos del menú para usuario $user")
			return []
		}
		
	}

	public getHijos(User user){ hijos.findAll{ !user.authorities.disjoint(it.roles) } }
}
