package tecnofind

import com.tecnofind.inicializacion.Inicializacion
import com.tecnofind.inicializacion.JsonInicializacion

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
