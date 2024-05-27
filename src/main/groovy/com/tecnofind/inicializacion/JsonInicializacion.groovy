package com.tecnofind.inicializacion

import grails.converters.JSON
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.joda.time.Months
import com.tecnofind.oficina.Oficina
import com.tecnofind.User
import com.tecnofind.persona.TipoRelacion
import com.tecnofind.persona.Nombre
import com.tecnofind.credencial.Credencial
import com.tecnofind.sitio.Sitio
import com.tecnofind.Role
import com.tecnofind.persona.Persona
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.persona.Telefono
import com.tecnofind.persona.PersonaRelacionada

class JsonInicializacion {
	private static DecimalFormat decimalCurencyFormat = null;
	private static DecimalFormat decimalCurencyFormatSinPunto = null;
	public static String formatear(numero){
		if (decimalCurencyFormat == null){
			// println "\nCreando formateador decimal...\n"
			String patternCurrency = '###,###,##0.00'
			decimalCurencyFormat = new DecimalFormat(patternCurrency)
			DecimalFormatSymbols otherSymbols = new   DecimalFormatSymbols(Locale.ENGLISH)
			otherSymbols.setDecimalSeparator(',' as char)
			otherSymbols.setGroupingSeparator('.' as char)
			decimalCurencyFormat.setGroupingUsed(true)
			decimalCurencyFormat.setDecimalFormatSymbols(otherSymbols)
		}
		return decimalCurencyFormat.format(numero)
	}

	public static String formatearSinPunto(numero){
		if (decimalCurencyFormatSinPunto == null){
			// println "\nCreando formateador decimal...\n"
			String patternCurrency = '###,###,##0.00'
			decimalCurencyFormatSinPunto = new DecimalFormat(patternCurrency)
			DecimalFormatSymbols otherSymbols = new   DecimalFormatSymbols(Locale.ENGLISH)
			otherSymbols.setDecimalSeparator(',' as char)
			otherSymbols.setGroupingSeparator('.' as char)
			decimalCurencyFormatSinPunto.setGroupingUsed(false)
			decimalCurencyFormatSinPunto.setDecimalFormatSymbols(otherSymbols)
		}
		def a_formatear = numero ?: 0
		return decimalCurencyFormatSinPunto.format(a_formatear)
	}

	static def inicializar(){
		JSON.registerObjectMarshaller(Oficina){
			def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['nombre'] = it.nombre
            returnArray['ip'] = it.ip
            
            return returnArray
        }
        
		JSON.registerObjectMarshaller(Credencial){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['usuario'] = it.usuario
			returnArray['password'] = it.password
			returnArray['oficina'] = it.oficina.nombre
			returnArray['sitio'] = it.sitio.nombre
			return returnArray
		}

        JSON.registerObjectMarshaller(User){
			def returnArray = [:]
			    returnArray['id'] = it.id
				returnArray['serialVersionUID'] = it.serialVersionUID
				returnArray['username'] = it.username
				returnArray['password'] = it.password
				returnArray['nombre'] = it.nombre
				returnArray['oficina'] = it.oficina?.nombre ?: '-'
				returnArray['rol'] = it.getAuthorities().toString().replaceAll("\\[|\\]", "").replace('_', ' ').replace('ROLE','')
				returnArray['enabled'] = it.enabled
				returnArray['accountExpired'] = it.accountExpired
				returnArray['accountLocked'] = it.accountLocked
				returnArray['passwordExpired'] = it.passwordExpired
				returnArray['userTenantId'] = it.userTenantId  
            return returnArray
        }

        JSON.registerObjectMarshaller(TipoRelacion){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['nombre'] = it.nombre
            return returnArray
        }

		JSON.registerObjectMarshaller(Role){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['authority'] = it.authority.toString().replaceAll("\\[|\\]", "").replace('_', ' ').replace('ROLE','')
			return returnArray
		}

		JSON.registerObjectMarshaller(Persona){
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['primerNombre'] = it.primerNombre ?: "-"
            returnArray['segundoNombre'] = it.segundoNombre ?: "-" 
            returnArray['apellidoPadre'] = it.apellidoPadre ?: "-"
            returnArray['apellidoMadre'] = it.apellidoMadre ?: "-" 
			returnArray['tipoSociedad'] = it.tipoSociedad ?: "-"   
            returnArray['anoNacimiento'] = it.anoNacimiento ?: "-" 
            returnArray['documento'] = it.documento ?: "-" 
            returnArray['poblacionNacimiento'] = it.poblacionNacimiento ?: "-" 
            returnArray['autonomo'] = it.autonomo ?: "-"
            returnArray['oficina'] = it.oficina.nombre
            returnArray['telefonos'] = it.unirTelefonos()
            returnArray['domicilios'] = it.domicilios 
            returnArray['personasRelacionadas'] = it.personasRelacionadas
            return returnArray
        }
		
		JSON.registerObjectMarshaller(Nombre){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['castellano'] = it.castellano
			returnArray['catalan'] = it.catalan
			return returnArray
		}

		JSON.registerObjectMarshaller(Telefono){
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['numero'] = it.numero 
            returnArray['origen'] = it.origen ?: null 
            returnArray['estado'] = it.estado
			return returnArray
        }

		JSON.registerObjectMarshaller(Domicilio){
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['provincia'] = it.provincia ?: null
            returnArray['poblacion'] = it.poblacion ?: null
            returnArray['calle'] = it.calle ?: null
            returnArray['numero'] = it.numero ?: null
            returnArray['bloque'] = it.bloque ?: null
            returnArray['portal'] = it.portal ?: null
            returnArray['escalera'] = it.escalera ?: null
            returnArray['piso'] = it.piso ?: null
            returnArray['puerta'] = it.puerta ?: null
            returnArray['codigoPostal'] = it.codigoPostal ?: null
            returnArray['estado'] = it.estado
            returnArray['persona'] = it.persona.id  
			return returnArray
        }

		JSON.registerObjectMarshaller(PersonaRelacionada){
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['principal'] = it.principal  
            returnArray['relacionada'] = it.relacionada
            returnArray['relacion'] = it.relacion 
            returnArray['estado'] = it.estado
			return returnArray
        }
	}
}
