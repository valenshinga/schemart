package com.tecnofind

import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import java.util.Random

/**
 * Clase de servicio para manejar operaciones relacionadas con instancias de ProxyCalim.
 *
 * Esta clase proporciona métodos para obtener instancias aleatorias de ProxyCalim
 * dependiendo de si se utilizan para servicios web o para selenium.
 */
class ProxyTecnofindService {
	/**
	 * Método para obtener una instancia aleatoria de ProxyCalim basada en el valor de webService.
	 * Este método recupera todas las instancias de ProxyCalim que coinciden con los criterios (webService y baja en false),
	 * y luego selecciona una de ellas de forma aleatoria.
	 *
	 * @param webService el valor booleano de webService para filtrar las instancias de ProxyCalim.
	 * @return una instancia aleatoria de ProxyCalim que coincide con el valor de webService y baja en false, o null si no hay ninguna.
	 */
	public ProxyTecnofind obtenerProxyAleatorio() {
		// Obtener la lista de proxies que coinciden con los criterios
		def proxies = ProxyTecnofind.findAllByHabilitado(true)

		int cantidad = proxies.size()
		return proxies[new Random().nextInt(cantidad)]
	}
}