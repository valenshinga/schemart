package com.tecnofind.domicilio
 
import grails.transaction.Transactional
import com.tecnofind.Estado
import org.hibernate.transform.Transformers
import grails.plugin.springsecurity.SpringSecurityUtils

import com.tecnofind.persona.Persona
import com.tecnofind.persona.PersonaService
import com.tecnofind.persona.PersonaCommand
import java.text.Normalizer
import info.debatty.java.stringsimilarity.*
import com.tecnofind.sitio.Sitio


@Transactional
class DomicilioService {
    def sessionFactory
    def personaService
    
    public List<Provincia> listProvincias() {
        return Provincia.list()
    } 

    public List<Poblacion> listPoblacionesByProvincia(Long id) {
        def provinciaId = id
        return Poblacion.createCriteria().list {
            eq("provincia.id", provinciaId)
        }
    }

    public List<Poblacion> listPoblaciones() {
        return Poblacion.list() 
    }

    def asignarPoblacion(String nombrePoblacion, Provincia provincia){
        Poblacion poblacion
        if(nombrePoblacion){
            poblacion = Poblacion.findByNombre(nombrePoblacion.toUpperCase())
            if(!poblacion){
                return crearPoblacion(nombrePoblacion, provincia)
            }
        }
        return poblacion
    }

    def asignarProvincia(String nombreProvincia, Sitio sitio){
        def sitioNombre
        def nombreNormalizado = nombreProvincia
        if (sitio.nombre == "Abcteléfonos" || sitio.nombre == "Bittools" || sitio.nombre == "Telexplorer" ){
            sitioNombre = "_" + sitio.nombre.toLowerCase().replaceAll("é", "e")
            nombreNormalizado = normalizarCadena(nombreProvincia)
        }
        String query = """
        SELECT id from provincia where nombre${sitioNombre ?: ""} = '${nombreNormalizado}';
        """        
        Provincia provincia = Provincia.get(sessionFactory.currentSession.createSQLQuery(query).uniqueResult())
        if (provincia == null) {
            def sitiosTry = ['', '_abctelefonos', '_telexplorer', '_bittools']
            for (String sitioTry : sitiosTry) {
                query = """
                SELECT id FROM provincia WHERE nombre${sitioTry} LIKE '${nombreNormalizado}';
                """
                provincia = Provincia.get(sessionFactory.currentSession.createSQLQuery(query).uniqueResult())
                if (provincia != null) break
            }
        }
        return provincia
    }


    def crearPoblacion(String nombrePoblacion, Provincia provincia){
        println "Creando población $nombrePoblacion..."
        def poblacion = new Poblacion()
        poblacion.nombre = nombrePoblacion.toUpperCase()
        poblacion.provincia = provincia
        poblacion.save(flush:true, failOnError:true)
        return poblacion
    }

    def crearProvincia(String nombreProvincia){
        println "Creando provincia $nombreProvincia..."
        def provincia = new Provincia()
        provincia.nombre = nombreProvincia.toUpperCase()
        provincia.save(flush:true, failOnError:true)
        return provincia
    }

    def List<Domicilio> listDomiciliosByPersona(Long personaId) {
        def domicilios = Domicilio.createCriteria().list {
            eq('persona.id', personaId)
        }
        return domicilios
    }

    public List<Domicilio> updateDomiciliosPersona(PersonaCommand personaCommand, List<Domicilio> domiciliosEditados) { 

        Persona persona = personaService.getPersona(personaCommand.id)

        List<Domicilio> domicilios = Domicilio.findAllByPersona(persona)
        
        domiciliosEditados.each { domicilioEditado ->
            def domicilio = domicilios.find { it.id == domicilioEditado.id }
            if (domicilio) {
                domicilio.calle = domicilioEditado?.calle  
                domicilio.numero = new Integer(domicilioEditado?.numero) 
                domicilio.portal = domicilioEditado?.portal 
                domicilio.escalera = domicilioEditado?.escalera 
                domicilio.bloque = domicilioEditado?.bloque 
                domicilio.piso = domicilioEditado?.piso 
                domicilio.puerta = domicilioEditado?.puerta 
                domicilio.codigoPostal = domicilioEditado?.codigoPostal 
                domicilio.estado = Estado.findByNombre(domicilioEditado?.estado?.nombre) 
                domicilio.provincia = Provincia.findByNombre(domicilioEditado?.provincia?.nombre)
                domicilio.poblacion = Poblacion.findByNombre(domicilioEditado?.poblacion?.nombre)  
                domicilio.save(flush: true, failOnError: true)
            }
        }
        return domicilios
    }

    public getProvincia(Long id) {
        return Provincia.get(id)
    }

    public getPoblacion(Long id) {
        return Poblacion.get(id)
    }

    public getDomicilio(Long id) {
        return Domicilio.get(id)
    }
    
    def obtenerNombreProvincia(Long provinciaId) {
        def provincia = Provincia.get(provinciaId)
        return provincia?.nombre
    }

    def obtenerNombrePoblacion(Long poblacionId) {
        if(poblacionId){
            def poblacion = Poblacion.get(poblacionId)
            return poblacion?.nombre
        }
        else{
            return null
        }
    }
    
    def getDomiciliosByPersona(Long personaId) {
        def domicilios = Domicilio.createCriteria().list {
            eq('persona.id', personaId)
        }
        return domicilios
    }

    def normalizarCadena(String cadena) {
        def normalizedString = Normalizer.normalize(cadena, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toUpperCase()
                .replaceAll("Ñ", "N")
                .trim()
        return normalizedString
    }
}
