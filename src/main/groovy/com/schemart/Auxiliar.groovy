package com.schemart

import com.schemart.inicializacion.JsonInicializacion

import java.lang.AssertionError
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import grails.core.DefaultGrailsApplication
class Auxiliar {
	// No tocar el de abajo
	public static final boolean testingEnviromentAlternativo = grails.util.Environment.isDevelopmentMode() // No switchear
	// No tocar el de arriba
	// Este sí:
	public static final boolean testingEnviroment = testingEnviromentAlternativo // Este es el que switcheamos para testear cosas.

	public static String formatear(numero){
		return JsonInicializacion.formatear(numero) // Estas dos por ahora las tomo del JsonInicialización porque me parece que, como ahí el DecimalFormal queda inicializado y guardado, es más performante no hacerlo muchas veces.
	}

	public static String formatearSinPunto(numero){
		return JsonInicializacion.formatearSinPunto(numero) // Estas dos por ahora las tomo del JsonInicialización porque me parece que, como ahí el DecimalFormal queda inicializado y guardado, es más performante no hacerlo muchas veces.
	}

	public static String formatear(numero, int decimales) {formatear(numero,decimales,true,null)}
	public static String formatear(numero, int decimales, Boolean separadorCadaMil){formatear(numero,decimales,separadorCadaMil,null)}
	public static String formatear(numero, int decimales, Boolean separadorCadaMil, String nullValue){
		if (nullValue && ! numero)
			return nullValue
		String patternCurrency
		if (decimales)
			patternCurrency = '###,###,##0.' + ('0' * decimales)
		else
			patternCurrency = '###,###,##0'
		DecimalFormat decimalCurencyFormat = new DecimalFormat(patternCurrency)
		DecimalFormatSymbols otherSymbols = new   DecimalFormatSymbols(Locale.ENGLISH)
		otherSymbols.setDecimalSeparator(',' as char)
		otherSymbols.setGroupingSeparator('.' as char)
		decimalCurencyFormat.setGroupingUsed(separadorCadaMil)
		decimalCurencyFormat.setDecimalFormatSymbols(otherSymbols)
		return decimalCurencyFormat.format(numero)
	}

	public static String mapEnDatatable(map){ mapEnDatatable(map[0].keySet().collect{it.capitalize()},'') }
	public static String mapEnDatatable(map, String titulo){ mapEnDatatable(map[0].keySet().collect{it.capitalize()},map,titulo) }
	public static String mapEnDatatable(headers,map){ mapEnDatatable(headers,map,null) }
	public static String mapEnDatatable(headers, map, String titulo) {
		boolean filaPar = false
		StringBuilder texto = new StringBuilder()
		int[] columnWidths

		if (testingEnviroment) {
			columnWidths = new int[headers.size()]
			headers.eachWithIndex { header, i ->
				columnWidths[i] = header.length()
			}
			map.each { row ->
				row.eachWithIndex { item, i ->
					columnWidths[i] = Math.max(columnWidths[i], String.valueOf(item.value).length())
				}
			}

			if (titulo) {
				texto.append(titulo).append(':\n')
			}
			String separador = "-" * columnWidths.sum()
			texto.append(separador + '\n')
			headers.eachWithIndex { header, i ->
				texto.append(String.format("%-" + columnWidths[i] + "s   ", header))
			}
			texto.append('\n' + separador + '\n')

			map.each{
				String estilo = filaPar ? '' : 'background-color: rgba(0,0,0,.05);'
				filaPar = !filaPar
				it.eachWithIndex { item, i ->
					texto.append(String.format("%-" + columnWidths[i] + "s   ", item.value))
				}
				texto.append('\n')
			}
		} else {
			if (titulo) {
				texto.append('<p style="text-align:center;text-decoration:underline;font-weight:bold">').append(titulo).append(':</p>')
			}

			texto.append("""
			<style>
				table {-webkit-text-size-adjust:100%;-webkit-tap-highlight-color:transparent;font-weight:400;line-height:1.5;font-size:.875em;color:#353c4e;font-family:"Open Sans",sans-serif;box-sizing:inherit;background-color:transparent;border:1px solid #e9ecef;clear:both;max-width:none!important;border-collapse:collapse!important;border-top:none;margin-top:0!important;margin-bottom:0!important;width:100%}
				thead {-webkit-text-size-adjust: 100%;-webkit-tap-highlight-color: transparent;font-weight: 400;line-height: 1.5;font-size: 0.875em;color: #353c4e;font-family: "Open Sans", sans-serif;border-collapse: collapse !important;box-sizing: inherit;}
				thead tr {-webkit-text-size-adjust: 100%;-webkit-tap-highlight-color: transparent;font-weight: 400;line-height: 1.5;font-size: 0.875em;color: #353c4e;font-family: "Open Sans", sans-serif;border-collapse: collapse !important;box-sizing: inherit;}
				thead th {-webkit-text-size-adjust: 100%;-webkit-tap-highlight-color: transparent;line-height: 1.5;font-size: 0.875em;color: #353c4e;font-family: "Open Sans", sans-serif;border-collapse: collapse !important;text-align: left;padding: .75rem;border: 1px solid #e9ecef;vertical-align: bottom;border-bottom: 2px solid #e9ecef;border-bottom-width: 2px;box-sizing: content-box;border-bottom-color: #ccc;white-space: nowrap;cursor: pointer;position: relative;border-left-width: 0;padding-right: 30px;width: 458.867px;}
				tbody {-webkit-text-size-adjust:100%;-webkit-tap-highlight-color:transparent;font-weight:400;line-height:1.5;font-size:.875em;color:#353c4e;font-family:"Open Sans",sans-serif;border-collapse:collapse!important;box-sizing:inherit}
				tbody tr {-webkit-text-size-adjust: 100%;-webkit-tap-highlight-color: transparent;font-weight: 400;line-height: 1.5;font-size: 0.875em;color: #353c4e;font-family: "Open Sans", sans-serif;border-collapse: collapse !important;box-sizing: inherit;}
				tbody td {-webkit-text-size-adjust: 100%;-webkit-tap-highlight-color: transparent;font-weight: 400;line-height: 1.5;font-size: 0.875em;color: #353c4e;font-family: "Open Sans", sans-serif;border-collapse: collapse !important;padding: .75rem;vertical-align: top;border: 1px solid #e9ecef;box-sizing: content-box;white-space: nowrap;border-left-width: 0;border-bottom-width: 0;}
			</style>
			<table>
				<thead>
					<tr>"""
			)
			texto.append("<th>").append(headers.join("</th><th>")).append("</th>")
			texto.append("</thead><tbody>")

			map.each{
				String estilo = filaPar ? '' : 'background-color: rgba(0,0,0,.05);'
				filaPar = !filaPar
				texto.append("<tr style='$estilo'><td>")
				texto.append(it.collect{it.value}.join("</td><td>")).append("</td></tr>")
			}
			texto.append("</tbody></table>")
		}

		return texto.toString()
	}

	public static subdividirLista(Object lista, int cant){
		def lista_sep = lista.collate((int) java.lang.Math.ceil(lista.size() / cant))
		// if (lista_sep.size() != cant)
			// lista_sep[-2] = lista_sep[-2] + lista_sep.removeLast()
		return lista_sep
	}

	public static boolean compararNumeros(inputA, inputB, Double tolerancia = null){
		if (tolerancia == null)
			tolerancia = 2
		def a = inputA instanceof String ? new Double(inputA.replace(".","").replace(",",".")) : inputA
		if (!a)
			a = 0
		def b = inputB instanceof String ? new Double(inputB.replace(".","").replace(",",".")) : inputB
		if (!b)
			b = 0
		return Math.abs(a - b) <= tolerancia
	}

	public static void debug(Object objeto, String mensaje = null) {
		def props = objeto instanceof java.util.LinkedHashMap ? objeto : objeto.properties
		int maxKeyLength = props.keySet().stream().mapToInt({ it.toString().length() }).max().orElse(0)

		separador()
		if (mensaje)
			println mensaje
		println props.sort { it.key }.collect {
			def valor = it.value instanceof Number ? (it.value == it.value.toLong() ? it.value.toLong() : formatear(it.value)) : it.value
			return "  ${it.key.padRight(maxKeyLength)} : ${valor}"
		}.join('\n')
		separador()
	}

	public static String corregirTelefono(String telefono){
		if (! telefono)
			return telefono

		//println telefono

		telefono=telefono.replaceAll(/\D+/, '')
		//println "Se deja sólo números"
		//println telefono
		//Se saca el 54 si existe
		telefono=telefono.replaceFirst(/^54/, '')
		//println "Se quita el 54 si existe"
		//println telefono
		//Si sólo tiene 8 números, no posee código de área y seguramente es de capital
		if(telefono.size()==8){
			telefono= '11' + telefono
			//println "El número contenía 8 digitos y se agrega por defecto el 11 de capital"
			//println telefono
		}else{
			//Si tiene más de 10 dígitos, se intenta quitar el 9 de cel internacional
			//y el 0 de código de área
			if(telefono.size()>10){
				//println "Tiene más de 10 dígitos"
				//println "Se quita 9 adelante si lo tiene"
				telefono=telefono.replaceFirst(/^9/, '')
				//println telefono
				//println "Se quita 0 adelante si lo tiene"
				telefono=telefono.replaceFirst(/^0/, '')
				//println telefono
			}

			if(telefono.size()==10){
				//println "Tiene justo 10 digitos, si empieza con 15 reemplazo a 11"
				telefono=telefono.replaceFirst(/^15/, '11')
				//println telefono
			}

			if(telefono.size()==12){
				//Tiene un 15 demás que suele estar entre el código de área y el bloque de números
				//el código de área puede ser de 2, 3 o 4 dígitos, y el bloque que le sigue
				//tiene que sumar 10 dígitos en total junto con el código de área (8, 7 o 6 respectivamente)
				//println "Tiene 12 dígitos, se busca el 15 para quitarlo"
				def partes = telefono.split('15', 2)
				def unir = false
				if(partes.size()==2){
					if((partes[0].size()==2) && (partes[1].size()==8) ){
						unir = true
						//println "Se encuentra codigo de 2 digitos y bloque de 8"
						//println partes[0]
						//println partes[1]
					}

					if((partes[0].size()==3) && (partes[1].size()==7) ){
						unir = true
						//println "Se encuentra codigo de 3 digitos y bloque de 7"
						//println partes[0]
						//println partes[1]
					}

					if((partes[0].size()==4) && (partes[1].size()==6) ){
						unir = true
						//println "Se encuentra codigo de 4 digitos y bloque de 6"
						//println partes[0]
						//println partes[1]
					}

					if(unir){
						telefono = partes[0] + partes[1]
						//println "Se une el codigo de area con el bloque"
						//println telefono
					}
				}
			}
		}
		//Se agrega adelante el 54 de arg y 9 de celular
		//println "Se agrega el 54 de arg y el 9 de celular"
		telefono = '+549' + telefono
		//println telefono
		return telefono
	}

	public static String printearError(e){ printearError(e, null) }
	public static String printearError(e, String mensaje){
		if (!mensaje)
			mensaje = "Error"
		String printeo = "\n\n{" + new LocalDateTime().toString("dd/MM/yyyy HH:mm:ss") + "}\t$mensaje"
		mensaje = e?.message
		if (e instanceof AssertionError){
			if (mensaje?.contains("finerror"))
				printeo += "\nMensaje: " + mensaje?.split("finerror")[0]
			else
				printeo += "\nMensaje: " + mensaje?.split("Expression:")[0]
		}
		else{
			printeo += "\nMensaje: " + mensaje
		}
		printeo += "\nStackTrace:"
		printeo += "\n" + e?.stackTrace?.findAll {
			it.toString()?.with {
				contains(".groovy:") &&
				!contains("com.schemart.selenium.SeleniumService.buscar") &&
				!contains("com.schemart.selenium.SeleniumService.clickear") &&
				!toLowerCase().with {
					contains("transaction") ||
					contains("springsecurity") ||
					contains(".grails.") ||
					contains("grails.gorm")
				}
			}
		}?.collect {
			"\t-$it"
		}?.join("\n")
		println printeo
		return printeo
	}

	public static String insertarEnters(String texto){ insertarEnters(texto, 100, "<br/>") }
	public static String insertarEnters(String texto, int caracteres){ insertarEnters(texto, caracteres, "<br/>") }
	public static String insertarEnters(String texto, String separador){ insertarEnters(texto, 100, separador) }
	public static String insertarEnters(String texto, int caracteres, String separador){
		if (texto.size() > caracteres)
			return texto.toList().collate(caracteres)*.join().join(separador)
		return texto
	}

	public static LocalDate calcularFechaSueldos(mes = null, ano = null, Integer diaCorte = 20){
		if (mes && ano)
			return new LocalDate(new Integer(ano),new Integer(mes),1)
		def hoy = new LocalDate()
		if (hoy.dayOfMonth < diaCorte)
			hoy = hoy.minusMonths(1)
		return hoy
	}

	public static String calcularFechaSueldosString(mes = null, ano = null, Integer diaCorte = 20){
		return calcularFechaSueldos(mes, ano, diaCorte).toString("MM/yyyy")
	}

	public static calcularDuracion(Closure funcion, boolean returnDuration = false) {
		LocalDateTime inicio = LocalDateTime.now()

		def retorno = funcion.call()

		LocalDateTime fin = LocalDateTime.now()

		Duration duracion = new Duration(inicio.toDateTime(), fin.toDateTime())
		long millis = duracion.getMillis()
		if (returnDuration)
			return [retorno, millis]
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		String metodoLlamador = '-'
		for(i in 10..20)
			try {
				metodoLlamador = stackTrace[i]?.getMethodName()?.toString().split("tt__")[1]
				break
			}
			catch(Exception e) {}
		int segundos = millis / 1000
		println "El método ${metodoLlamador} se demoró ${millis/1000} segundos"
		return retorno
	}

	public static calcularDuracionEstadistica(Closure funcion, int numRepeticiones = 10) {
		def duraciones = []
		def retorno
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		String metodoLlamador = '-'
		for(i in 10..20)
			try {
				metodoLlamador = stackTrace[i]?.getMethodName()?.toString().split("tt__")[1]
				break
			}
			catch(Exception e) {}
		println "Inciando cálculo de Estadísticas de duración para (${metodoLlamador}).\n\tMuestras:"
		for (int i = 0; i < numRepeticiones; i++) {
			def (r, d) = calcularDuracion(funcion, true)
			duraciones.add(d)
			retorno = r
			println "\t\t[${i+1}/$numRepeticiones]: $d"
		}

		long suma = duraciones.sum()
		double promedio = suma / numRepeticiones
		def segundos = (promedio / 1000).round(3)
		int desviacionEstandar = Math.sqrt(duraciones.collect { Math.pow(it - promedio, 2) }.sum() / numRepeticiones)

		println "Resultados de Estadísticas de duración para (${metodoLlamador}):"
		println "\tPromedio: ${segundos}s"
		println "\tDesviación estándar: ${desviacionEstandar}ms"

		return retorno
	}

	public static String getPathExcelTemplates(DefaultGrailsApplication grailsApplication) {
		if(testingEnviromentAlternativo) {
			return System.getProperty("user.dir") + "/excelTemplates/"
		}
		else {
			return grailsApplication.config.getProperty('pathExcelTemplates') + "/"
		}
	}

	public static void medirRegistroFuncion(registro){
		// Paso 1: Obtener el tiempo total
		LocalDateTime tiempoInicio = registro[1][1]
		LocalDateTime tiempoFinal = registro[-1][1]
		long tiempoTotal = tiempoFinal.getMillisOfDay() - tiempoInicio.getMillisOfDay()

		// Paso 2: Procesar la lista 'registro'
		def tiemposPorLinea = []
		registro.eachWithIndex { item, index ->
			if(index < registro.size() - 1) {
				long duracion = registro[index + 1][1].getMillisOfDay() - item[1].getMillisOfDay()
				tiemposPorLinea << [linea: item[0], duracion: duracion]
			}
		}

		// Paso 3: Ordenar las entradas por tiempo
		tiemposPorLinea.sort { -it.duracion }

		// Paso 4: Calcular el tiempo porcentual
		tiemposPorLinea.each {
			it.porcentaje = (it.duracion / (double) tiempoTotal) * 100
		}

		// Imprimir el análisis
		println "Tiempo total de la función: $tiempoTotal milisegundos"
		println "Líneas más impactantes:"
		tiemposPorLinea.each {
			println "${it.linea} -> ${it.duracion} milisegundos (${String.format("%.2f", it.porcentaje)}%)"
		}
	}

	public static void separador(String param = null) {
		def terminalWidth = 100
		if (param) {
			println "\n" + ('#' * terminalWidth) + "\n"
			println param
		}
		println "\n" + ('#' * terminalWidth)

	}

	public static Double parsearString(String string){
		Double salida = 0
		if (string)
			salida = new Double(string.replaceAll("\\.","").replaceAll(",","."))
		return salida
	}

	public static Map<String, String> separarTiempo(String tiempo) {
		def salida = [:]
		def partes = tiempo.split(':')

		salida.hora = partes[0]
		salida.minuto = partes[1] 
		
		return salida
	}

	public static Map parseJson(txt){
		def lazyMap = new groovy.json.JsonSlurper().parseText(txt)
		println 'esto es lazymap: ' + lazyMap
		def map = [:]
		for ( prop in lazyMap ) {
			println prop
			map[prop.key] = prop.value
		}
		println map
		return map
	}

}