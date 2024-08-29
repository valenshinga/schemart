package com.schemart.inicializacion

import grails.converters.JSON
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import com.schemart.User
import com.schemart.Role
import com.schemart.empleado.Empleado
import com.schemart.alumno.Alumno
import com.schemart.idioma.Idioma
import com.schemart.disponibilidad.Disponibilidad

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
        JSON.registerObjectMarshaller(User){
			def returnArray = [:]
			    returnArray['id'] = it.id
				returnArray['serialVersionUID'] = it.serialVersionUID
				returnArray['username'] = it.username
				returnArray['password'] = it.password
				returnArray['nombre'] = it.nombre
				returnArray['rol'] = it.getAuthorities().toString().replaceAll("\\[|\\]", "").replace('_', ' ').replace('ROLE','')
				returnArray['enabled'] = it.enabled
				returnArray['accountExpired'] = it.accountExpired
				returnArray['accountLocked'] = it.accountLocked
				returnArray['passwordExpired'] = it.passwordExpired
            return returnArray
        }

		JSON.registerObjectMarshaller(Role){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['authority'] = it.authority.toString().replaceAll("\\[|\\]", "").replace('_', ' ').replace('ROLE','')
			return returnArray
		}

		JSON.registerObjectMarshaller(Empleado){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['nombreApellido'] = "${it.nombre} ${it.apellido}" 
			returnArray['dni'] = it.dni 
			returnArray['cuit'] = it.cuit
			returnArray['domicilio'] = it.domicilio
			returnArray['email'] = it.email
			returnArray['telefono'] = it.telefono
			returnArray['fechaNacimiento'] = it.fechaNacimiento.toString('dd/MM/YYYY')
			returnArray['cargo'] = it.cargo 
			returnArray['estado'] = it.estado.nombre
			return returnArray
		}

		JSON.registerObjectMarshaller(Alumno){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['nombreApellido'] = "${it.nombre} ${it.apellido}" 
			returnArray['domicilio'] = it.domicilio
			returnArray['email'] = it.email ?: '-'
			returnArray['telefono'] = it.telefono ?: '-'
			returnArray['fechaNacimiento'] = it.fechaNacimiento.toString('dd/MM/YYYY')
			returnArray['tipoCurso'] = it.tipoCurso?.nombre 
			returnArray['estado'] = it.estado?.nombre
			return returnArray
		}

		JSON.registerObjectMarshaller(Idioma){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['idioma'] = "${it.nombre} ${it.nivel}" 
			returnArray['nombre'] = it.nombre 
			returnArray['nivel'] = it.nivel
			return returnArray
		}

		JSON.registerObjectMarshaller(Disponibilidad){
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['dia'] = it.dia 
			returnArray['desde'] = it.desde.toString('HH:mm') 
			returnArray['hasta'] = it.hasta.toString('HH:mm') 
			return returnArray
		}
	}
}
