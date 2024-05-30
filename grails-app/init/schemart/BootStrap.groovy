package schemart

import com.schemart.inicializacion.Inicializacion
import com.schemart.inicializacion.JsonInicializacion

class BootStrap {

    def init = { servletContext ->
		environments {
			development {
				Inicializacion.comienzo()
			}
			production {
                Inicializacion.comienzo()
			}
		}
		
		JsonInicializacion.inicializar()
    }
    def destroy = {
    }
}
