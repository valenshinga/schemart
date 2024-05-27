package com.tecnofind.persona

import grails.transaction.Transactional
import org.hibernate.transform.Transformers
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.Estado

@Transactional
class TipoRelacionService {
    def sessionFactory

    public List<TipoRelacion> listTiposRelacion() {
        List<TipoRelacion> list = TipoRelacion.list()
        return list
    } 

    public TipoRelacion save(TipoRelacionCommand command) { 
        TipoRelacion tipoRelacion = new TipoRelacion()
        tipoRelacion.nombre = command.nombre
        return tipoRelacion.save(flush:true, failOnError:true)
    }

    public TipoRelacion getTipoRelacion(Long id) {
        return TipoRelacion.get(id)
    }

    public TipoRelacion update(TipoRelacionCommand command) {
        TipoRelacion tipoRelacion = getTipoRelacion(command.id)
        tipoRelacion.nombre = command.nombre 
        return tipoRelacion.save(flush:true, failOnError:true)
    }

    public TipoRelacion delete(Long id) {
        TipoRelacion tipoRelacion = getTipoRelacion(id)
        tipoRelacion.delete(flush:true, failOnError:true)
    }

    def getTipoRelacionCommand(Long id) {
        def tipoRelacion = getTipoRelacion(id)
        def tipoRelacionCommand = new TipoRelacionCommand()
        tipoRelacionCommand.id = tipoRelacion.id
        tipoRelacionCommand.nombre = tipoRelacion.nombre 
        return tipoRelacionCommand
    }

    def asignarTipoRelacion(Persona persona, Persona relacionada) {
        if (persona.domicilios && relacionada.domicilios) {
            for (Domicilio domicilioActual : persona.domicilios) {
                for (Domicilio domicilioRelacionada : relacionada.domicilios) {
                    if (sonMismaDireccion(domicilioActual, domicilioRelacionada)) {
                        asignarRelacion(persona, relacionada, "Conviviente")
                        return
                    } else if (sonMismoEdificio(domicilioActual, domicilioRelacionada)) {
                        asignarRelacion(persona, relacionada, "Vecino")
                        return
                    }
                }
            }
            for (Domicilio domicilioAnterior : persona.domicilios.findAll { it.domicilioAnterior }) {
                for (Domicilio domicilioRelacionada : relacionada.domicilios) {
                    if (sonMismoEdificio(domicilioAnterior, domicilioRelacionada)) {
                        asignarRelacion(persona, relacionada, "Anterior Vecino")
                        return
                    }
                }
            }
        }
        if(tienenMismoApellido(persona, relacionada)) {
            asignarRelacion(persona, relacionada, "Familiar")
            return
        } else{
            asignarRelacion(persona, relacionada, "Compañero de Trabajo")
            return
        }
    }

    private boolean sonMismaDireccion(Domicilio domicilio1, Domicilio domicilio2) {
        return domicilio1?.calle == domicilio2?.calle &&
            domicilio1?.numero == domicilio2?.numero &&
            domicilio1?.codigoPostal == domicilio2?.codigoPostal &&
            domicilio1?.bloque == domicilio2?.bloque &&
            domicilio1?.piso == domicilio2?.piso &&
            domicilio1?.puerta == domicilio2?.puerta
    }

    private boolean sonMismoEdificio(Domicilio domicilio1, Domicilio domicilio2) {
        return domicilio1?.calle == domicilio2?.calle && domicilio1?.numero == domicilio2?.numero
    }

    private boolean tienenMismoApellido(Persona persona1, Persona persona2) {
        return persona1?.apellidoPadre == persona2?.apellidoPadre || persona1?.apellidoMadre == persona2?.apellidoMadre
    }

    private void asignarRelacion(Persona principal, Persona relacionada, String tipoRelacionNombre) {
        TipoRelacion tipoRelacion = TipoRelacion.findByNombre(tipoRelacionNombre)
        Estado estadoInicial = Estado.findByNombre("Sin Confirmar")

        if (!existeRelacion(principal, relacionada)) {
            PersonaRelacionada relacion = new PersonaRelacionada()
            relacion.principal = principal
            relacion.relacionada = relacionada
            relacion.relacion = tipoRelacion
            relacion.estado = estadoInicial
            relacion.save(flush: true, failOnError: true)
            principal.addToPersonasRelacionadas(relacion)
            relacionada.addToPersonasRelacionadas(relacion)
        }
    }

    private boolean existeRelacion(Persona principal, Persona relacionada) {
        List<PersonaRelacionada> relaciones = PersonaRelacionada.findAllByPrincipalAndRelacionada(principal, relacionada)
        return !relaciones.isEmpty()
    }
}
