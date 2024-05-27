package com.tecnofind.persona

import grails.transaction.Transactional
import java.text.Normalizer
import org.hibernate.transform.Transformers

@Transactional
class NombreService {
    def sessionFactory

    public List<Nombre> listNombres() {
        return Nombre.list()
    }

    public Nombre getNombre(Long id) {
        return Nombre.get(id)
    }

    public Nombre save(NombreCommand nombreCommand) {
        def nombre = new Nombre()
        nombre.castellano = nombreCommand.castellano.toUpperCase()
        nombre.catalan = nombreCommand.catalan.toUpperCase()
        return nombre.save(flush:true, failOnError:true)
    }

    public Nombre update(NombreCommand nombreCommand) {
        def nombre = getNombre(nombreCommand.id)
        nombre.castellano = nombreCommand.castellano.toUpperCase()
        nombre.catalan = nombreCommand.catalan.toUpperCase()
        return nombre.save(flush:true, failOnError:true)
    }

    public Nombre delete(Long id) {
        def nombre = getNombre(id)
        nombre.delete(flush:true, failOnError:true)
    }

    def getNombreCommand(Long id) {
        def nombre = getNombre(id)
        def nombreCommand = new NombreCommand()
        nombreCommand.id = nombre.id
        nombreCommand.castellano = nombre.castellano
        nombreCommand.catalan = nombre.catalan
        return nombreCommand
    }

    public String getTraduccion(String nombre) {
        String normalizedNombre = normalizar(nombre)

        def nombreTraducido = Nombre.findAll().find { 
            String normalizedCastellano = normalizar(it.castellano)
            String normalizedCatalan = normalizar(it.catalan)
            normalizedCastellano == normalizedNombre || normalizedCatalan == normalizedNombre
        }

        if (nombreTraducido) {
            if (normalizar(nombreTraducido.castellano) == normalizedNombre) {
                return nombreTraducido.catalan
            } else {
                return nombreTraducido.castellano
            }
        }

        return null
    }

    public String normalizar(String nombre) {
        return Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase()
    }

}