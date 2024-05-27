package com.tecnofind.persona

import com.tecnofind.Auxiliar
import com.tecnofind.busqueda.PersonaBusqueda
import com.tecnofind.Estado
import com.tecnofind.domicilio.Domicilio
import com.tecnofind.domicilio.Provincia
import com.tecnofind.domicilio.Poblacion
import com.tecnofind.oficina.Oficina
import com.tecnofind.persona.Telefono
import com.tecnofind.domicilio.DomicilioService
import com.tecnofind.sitio.Sitio
import org.joda.time.DateTime

import grails.transaction.Transactional
import org.hibernate.transform.Transformers

@Transactional
class PersonaService {
    def sessionFactory
    def tipoRelacionService
    def domicilioService

    public List<Persona> listPersonas(Long oficinaUser = null) {
        String query = """
            SELECT DISTINCT
                persona.id AS Id,
                INITCAP(persona.primer_nombre) AS PrimerNombre,
                INITCAP(persona.segundo_nombre) AS SegundoNombre,
                (
                    SELECT STRING_AGG(numero, '<br/>')
                    FROM telefono
                    WHERE telefono.persona_id = persona.id
                    GROUP BY telefono.persona_id
                ) AS Telefonos,
                INITCAP(persona.apellido_padre) AS ApellidoPadre,
                INITCAP(persona.apellido_madre) AS ApellidoMadre,
                persona.autonomo,
                persona.documento,
                persona.tipo_sociedad AS TipoSociedad,
                persona.ano_nacimiento AS AnoNacimiento,
                poblacion.nombre AS poblacionNacimiento,
                oficina.nombre AS OficinaNombre
            FROM
                persona
            JOIN
                oficina ON persona.oficina_id = oficina.id
            LEFT JOIN
                poblacion ON persona.poblacion_nacimiento_id = poblacion.id
            ${oficinaUser ? 'WHERE persona.oficina_id = ' + oficinaUser : ''}
            ;
        """
        def personas = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
        return personas
    }

    public Persona getPersonaByTelefono(String numero) {
        def numeroTelefono = numero
        return Persona.createCriteria().get {
            telefonos {
                eq("numero", numeroTelefono)
            }
        }
    }

    public List<Persona> listPersonasPorBusqueda(Long id){
        def query = 
        """
        SELECT DISTINCT
                persona.id AS Id,
                INITCAP(persona.primer_nombre) AS PrimerNombre,
                INITCAP(persona.segundo_nombre) AS SegundoNombre,
                (
                    SELECT STRING_AGG(
                        COALESCE(calle || ' ', '') ||
                        numero || ', ' ||
                        COALESCE('blq ' || bloque || ', ', '') ||
                        COALESCE('por ' || portal || ', ', '') ||
                        COALESCE('esc ' || escalera || ', ', '') ||
                        COALESCE('piso ' || piso || ', ', '') ||
                        COALESCE('puerta ' || puerta || ', ', '') ||
                        COALESCE(codigo_postal || ', ', '') ||
                        COALESCE(poblacion.nombre || ', ', '') ||
                        COALESCE(provincia.nombre, ''),
                        '<br/>'
                    )
                    FROM domicilio
                    LEFT JOIN poblacion ON domicilio.poblacion_id = poblacion.id
                    LEFT JOIN provincia ON domicilio.provincia_id = provincia.id
                    WHERE domicilio.persona_id = persona.id AND domicilio.domicilio_anterior = FALSE
                    GROUP BY domicilio.persona_id
                ) AS DomicilioPrincipal,
                (
                    SELECT STRING_AGG(
                        COALESCE(calle || ' ', '') ||
                        numero || ', ' ||
                        COALESCE('blq ' || bloque || ', ', '') ||
                        COALESCE('por ' || portal || ', ', '') ||
                        COALESCE('esc ' || escalera || ', ', '') ||
                        COALESCE('piso ' || piso || ', ', '') ||
                        COALESCE('puerta ' || puerta || ', ', '') ||
                        COALESCE(codigo_postal || ', ', '') ||
                        COALESCE(poblacion.nombre || ', ', '') ||
                        COALESCE(provincia.nombre, ''),
                        '<br/>'
                    )
                    FROM domicilio
                    LEFT JOIN poblacion ON domicilio.poblacion_id = poblacion.id
                    LEFT JOIN provincia ON domicilio.provincia_id = provincia.id
                    WHERE domicilio.persona_id = persona.id AND domicilio.domicilio_anterior = TRUE
                    GROUP BY domicilio.persona_id
                ) AS DomicilioAnterior,
                (
                    SELECT STRING_AGG(numero, '<br/>')
                    FROM telefono
                    WHERE telefono.persona_id = persona.id
                    GROUP BY telefono.persona_id
                ) AS Telefonos,
                INITCAP(persona.apellido_padre) AS ApellidoPadre,
                INITCAP(persona.apellido_madre) AS ApellidoMadre,
                persona.autonomo,
                persona.documento,
                persona.tipo_sociedad AS TipoSociedad,
                persona.ano_nacimiento AS AnoNacimiento,
                poblacion.nombre AS poblacionNacimiento,
                oficina.nombre AS OficinaNombre
            FROM
                persona
            JOIN
                oficina ON persona.oficina_id = oficina.id
            JOIN
                persona_busqueda ON persona.id = persona_busqueda.persona_id
            JOIN
                busqueda ON persona_busqueda.busqueda_id = busqueda.id
            LEFT JOIN
                poblacion ON persona.poblacion_nacimiento_id = poblacion.id
            WHERE busqueda.id = ${id};
        """
        def personas = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
        return personas
    } 

    public List<Persona> listPersonaRelacionadas(Long personaPrincipal = null){
        String query = """
        SELECT DISTINCT
            persona.id AS Id,
            persona_relacionada.id as PersonaRelacionadaId,
            INITCAP(persona.primer_nombre) AS PrimerNombre,
            INITCAP(persona.segundo_nombre) AS SegundoNombre,
            (
                SELECT STRING_AGG(
                    COALESCE(calle || ' ', '') ||
                    numero || ', ' ||
                    COALESCE('blq ' || bloque || ', ', '') ||
                    COALESCE('por ' || portal || ', ', '') ||
                    COALESCE('esc ' || escalera || ', ', '') ||
                    COALESCE('piso ' || piso || ', ', '') ||
                    COALESCE('puerta ' || puerta || ', ', '') ||
                    COALESCE(codigo_postal || ', ', '') ||
                    COALESCE(poblacion.nombre || ', ', '') ||
                    COALESCE(provincia.nombre, ''),
                    '<br/>'
                )
                FROM domicilio
                LEFT JOIN poblacion ON domicilio.poblacion_id = poblacion.id
                LEFT JOIN provincia ON domicilio.provincia_id = provincia.id
                WHERE domicilio.persona_id = persona.id AND domicilio.domicilio_anterior = FALSE
                GROUP BY domicilio.persona_id
            ) AS DomicilioPrincipal,
            (
                SELECT STRING_AGG(
                    COALESCE(calle || ' ', '') ||
                    numero || ', ' ||
                    COALESCE('blq ' || bloque || ', ', '') ||
                    COALESCE('por ' || portal || ', ', '') ||
                    COALESCE('esc ' || escalera || ', ', '') ||
                    COALESCE('piso ' || piso || ', ', '') ||
                    COALESCE('puerta ' || puerta || ', ', '') ||
                    COALESCE(codigo_postal || ', ', '') ||
                    COALESCE(poblacion.nombre || ', ', '') ||
                    COALESCE(provincia.nombre, ''),
                    '<br/>'
                )
                FROM domicilio
                LEFT JOIN poblacion ON domicilio.poblacion_id = poblacion.id
                LEFT JOIN provincia ON domicilio.provincia_id = provincia.id
                WHERE domicilio.persona_id = persona.id AND domicilio.domicilio_anterior = TRUE
                GROUP BY domicilio.persona_id
            ) AS DomicilioAnterior,
            (
                SELECT STRING_AGG(numero, '<br/>')
                FROM telefono
                WHERE telefono.persona_id = persona.id
                GROUP BY telefono.persona_id
            ) AS Telefonos,
            INITCAP(persona.apellido_padre) AS ApellidoPadre,
            INITCAP(persona.apellido_madre) AS ApellidoMadre,
            persona.autonomo,
            persona.documento,
            persona.ano_nacimiento AS AnoNacimiento,
            oficina.nombre AS OficinaNombre,
            tipo_relacion.id as RelacionId,
            tipo_relacion.nombre AS NombreRelacion,
            (select nombre from estado where id = persona_relacionada.estado_id) as Estado
        FROM
            persona
        JOIN
            oficina ON persona.oficina_id = oficina.id
        LEFT JOIN
            telefono ON persona.id = telefono.persona_id
        JOIN
            (
                SELECT LEAST(principal_id, relacionada_id) AS persona_id1,
                    GREATEST(principal_id, relacionada_id) AS persona_id2,
                    MIN(id) AS id
                FROM persona_relacionada
                GROUP BY LEAST(principal_id, relacionada_id), GREATEST(principal_id, relacionada_id)
            ) AS unique_relacionada ON persona.id IN (unique_relacionada.persona_id1, unique_relacionada.persona_id2)
        JOIN
            persona_relacionada ON (persona.id = persona_relacionada.principal_id AND unique_relacionada.persona_id2 = persona_relacionada.relacionada_id) OR (persona.id = persona_relacionada.relacionada_id AND unique_relacionada.persona_id1 = persona_relacionada.principal_id)
        JOIN
            tipo_relacion ON persona_relacionada.relacion_id = tipo_relacion.id
        WHERE
            (${personaPrincipal ? "persona_relacionada.principal_id = ${personaPrincipal}" : ""} OR ${personaPrincipal ? "persona_relacionada.relacionada_id = ${personaPrincipal}" : ""})
            ${personaPrincipal ? "AND persona.id != ${personaPrincipal}" : ""}
        ;
    """
        def personas = sessionFactory.currentSession.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(LinkedHashMap)).list()
        return personas
    }

    public Persona getPersonaByDocumento(String documento) {
        return Persona.findByDocumento(documento)
    }

    public Persona instanciarPersona(Persona personaRecibida, PersonaCommand datosComparar, Boolean esPrincipal = false) {
        Persona persona
        if (personaRecibida){
            persona = personaRecibida
        }
        if (datosComparar.documento != null && datosComparar.documento != "DESCONOCIDO" && !personaRecibida){
            persona = Persona.findByDocumento(datosComparar.documento)
        }
        if(!persona){
            persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadreAndAnoNacimiento(datosComparar.primerNombre, datosComparar.segundoNombre, datosComparar.apellidoPadre, datosComparar.apellidoMadre, datosComparar.anoNacimiento)
        }
        if(!persona){
            persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadre(datosComparar.primerNombre, datosComparar.segundoNombre, datosComparar.apellidoPadre, datosComparar.apellidoMadre)
        }
        if(!persona){
            persona = new Persona()
        }

        persona.primerNombre = persona.primerNombre ?: datosComparar.primerNombre
        persona.segundoNombre = persona.segundoNombre ?: datosComparar.segundoNombre
        persona.apellidoPadre = persona.apellidoPadre ?: datosComparar.apellidoPadre
        persona.apellidoMadre = persona.apellidoMadre ?: datosComparar.apellidoMadre
        persona.anoNacimiento = persona.anoNacimiento ?: datosComparar.anoNacimiento

        persona.poblacionNacimiento = persona.poblacionNacimiento ?: domicilioService.getPoblacion(datosComparar.poblacionNacimientoId)
        persona.primerNombre = persona.primerNombre?.capitalize()
        persona.segundoNombre = persona.segundoNombre?.capitalize()
        persona.apellidoPadre = persona.apellidoPadre?.capitalize()
        persona.apellidoMadre = persona.apellidoMadre?.capitalize()

        persona.autonomo = persona.autonomo ?: datosComparar.autonomo
        persona.documento = persona.documento ?: datosComparar.documento
        persona.oficina = persona.oficina ?: Oficina.findById(datosComparar.oficinaId)

        if (datosComparar.telefonosEmpresa) {
            datosComparar.telefonosEmpresa.each { telefonoAutonomo ->
                if (!persona.telefonos.any { it.numero == telefonoAutonomo }) {
                    Sitio sitioTelefono = Sitio.findById(datosComparar.sitioId)
                    persona.addToTelefonos(new Telefono(numero: telefonoAutonomo, estado: Estado.findByNombre('Sin Confirmar'), origen: sitioTelefono, trabajo: true))
                }
            }
        }

        if (datosComparar.domicilioAutonomo){
            persona.domicilioAutonomo = datosComparar.domicilioAutonomo 
        }

        if (datosComparar.telefonos) {
            datosComparar.telefonos.each { numero ->
                if (!persona.telefonos.any { it.numero == numero }) {
                    Sitio sitioTelefono = Sitio.findById(datosComparar.sitioId)
                    persona.addToTelefonos(new Telefono(numero: numero, estado: Estado.findByNombre('Sin Confirmar'), origen: sitioTelefono))
                }
            }
        } 
        persona.save(flush:true, failOnError:true)

        if (datosComparar.domicilios) {
            datosComparar.domicilios.each { domicilio ->
                Domicilio domicilioInstanciado = Domicilio.findByCalleAndNumeroAndBloqueAndPortalAndEscaleraAndPisoAndPuertaAndProvinciaAndPersona(domicilio.calle, domicilio.numero, domicilio.bloque, domicilio.portal, domicilio.escalera, domicilio.piso, domicilio.puerta, Provincia.get(domicilio.provinciaId), persona)
                if (!domicilioInstanciado){
                    domicilioInstanciado = new Domicilio()
                    domicilioInstanciado.provincia = Provincia.get(domicilio.provinciaId)
                    domicilioInstanciado.poblacion = Poblacion.get(domicilio.poblacionId)
                    domicilioInstanciado.calle = domicilio.calle
                    domicilioInstanciado.numero = domicilio.numero
                    domicilioInstanciado.bloque = domicilio.bloque
                    domicilioInstanciado.portal = domicilio.portal
                    domicilioInstanciado.escalera = domicilio.escalera
                    domicilioInstanciado.piso = domicilio.piso
                    domicilioInstanciado.puerta = domicilio.puerta
                    domicilioInstanciado.codigoPostal = domicilio.codigoPostal
                    domicilioInstanciado.estado = Estado.get(domicilio.estadoId)
                    domicilioInstanciado.persona = persona
                    domicilioInstanciado.domicilioAnterior = domicilio.domicilioAnterior ?: false
                    domicilioInstanciado.save(flush:true, failOnError:true)
                    persona.addToDomicilios(domicilioInstanciado)
                }
            }
        }
        if(datosComparar.documentoEmpresa || datosComparar.razonSocialEmpresa) {
            Empresa empresa 
            if(datosComparar.documentoEmpresa) {
                empresa = Empresa.findByDocumento(datosComparar.documentoEmpresa)
            } 
            if(datosComparar.razonSocialEmpresa && !empresa) {
                empresa = Empresa.findByRazonSocial(datosComparar.razonSocialEmpresa)
            }
            if(!empresa) {
                empresa = new Empresa()
                empresa.documento = datosComparar.documentoEmpresa
                empresa.razonSocial = datosComparar.razonSocialEmpresa
                empresa.url = datosComparar.urlEmpresa
                empresa.save(flush:true, failOnError:true)
            }
            if(datosComparar.telefonosEmpresa) {
                datosComparar.telefonosEmpresa.each { numero ->
                    if (!empresa.telefonos.any { it.numero == numero }) {
                        empresa.addToTelefonos(new Telefono(numero: numero, estado: Estado.findByNombre('Sin Confirmar'), empresa: empresa))
                    }
                }
            }
            if(datosComparar.domicilioAutonomo) {
                datosComparar.domicilioAutonomo.each {
                    empresa.addToDomicilios(it)
                }
            }
            empresa.addToEmpleados(persona)
            empresa.save(flush:true, failOnError:true)
            persona.empresa = empresa
            persona.save(flush:true, failOnError:true)
        }

        if(datosComparar.personasRelacionadas.size() > 0) {
            datosComparar.personasRelacionadas.each { personaRelacionada ->
                def relacionada = instanciarPersonaRelacionada(persona, personaRelacionada)
                tipoRelacionService.asignarTipoRelacion(persona, relacionada)
            }
        }
        persona.personasRelacionadas = PersonaRelacionada.findAllByPrincipal(persona)
        return persona
    }

    public Persona instanciarPersonaRelacionada(Persona principal, PersonaCommand personaRelacionada) {
        Persona persona
        if (personaRelacionada.documento != null && personaRelacionada.documento != "DESCONOCIDO" ){
            persona = Persona.findByDocumento(personaRelacionada.documento)
        }
        if(!persona){
            persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadreAndAnoNacimiento(personaRelacionada.primerNombre, personaRelacionada.segundoNombre, personaRelacionada.apellidoPadre, personaRelacionada.apellidoMadre, personaRelacionada.anoNacimiento)
        }
        if(!persona){
            persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadre(personaRelacionada.primerNombre, personaRelacionada.segundoNombre, personaRelacionada.apellidoPadre, personaRelacionada.apellidoMadre)
        }
        if (!persona) {
            persona = new Persona()
            persona.primerNombre = personaRelacionada?.primerNombre?.capitalize()
            persona.segundoNombre = personaRelacionada?.segundoNombre?.capitalize()
            persona.apellidoPadre = personaRelacionada?.apellidoPadre?.capitalize()
            persona.apellidoMadre = personaRelacionada?.apellidoMadre?.capitalize()
            persona.anoNacimiento = personaRelacionada.anoNacimiento
            
            persona.poblacionNacimiento = domicilioService.getPoblacion(personaRelacionada.poblacionNacimientoId)
            persona.autonomo = personaRelacionada.autonomo
            persona.documento = personaRelacionada.documento
            persona.oficina = persona.oficina ?: Oficina.findById(personaRelacionada.oficinaId)
            if (personaRelacionada.telefonos) {
                personaRelacionada.telefonos.each { numero ->
                    if (!persona.telefonos.any { it.numero == numero }) {
                        Sitio sitioTelefono = Sitio.findById(personaRelacionada.sitioId)
                        persona.addToTelefonos(new Telefono(numero: numero, estado: Estado.findByNombre('Sin Confirmar'), origen: sitioTelefono))
                    }
                }
            } 

            persona.save(flush:true, failOnError:true)
            if (personaRelacionada.domicilios) {
                personaRelacionada.domicilios.each { domicilio ->
                    Domicilio domicilioInstanciado = Domicilio.findByCalleAndNumeroAndBloqueAndPortalAndEscaleraAndPisoAndPuertaAndProvinciaAndPersona(domicilio.calle, domicilio.numero, domicilio.bloque, domicilio.portal, domicilio.escalera, domicilio.piso, domicilio.puerta, Provincia.get(domicilio.provinciaId), persona)
                    if (!domicilioInstanciado){
                        domicilioInstanciado = new Domicilio()
                        domicilioInstanciado.provincia = Provincia.get(domicilio.provinciaId)
                        domicilioInstanciado.poblacion = Poblacion.get(domicilio.poblacionId)
                        domicilioInstanciado.calle = domicilio.calle
                        domicilioInstanciado.numero = domicilio.numero
                        domicilioInstanciado.bloque = domicilio.bloque
                        domicilioInstanciado.portal = domicilio.portal
                        domicilioInstanciado.escalera = domicilio.escalera
                        domicilioInstanciado.piso = domicilio.piso
                        domicilioInstanciado.puerta = domicilio.puerta
                        domicilioInstanciado.codigoPostal = domicilio.codigoPostal
                        domicilioInstanciado.estado = Estado.get(domicilio.estadoId)
                        domicilioInstanciado.persona = persona
                        domicilioInstanciado.domicilioAnterior = domicilio.domicilioAnterior ?: false
                        domicilioInstanciado.save(flush:true, failOnError:true)
                        persona.addToDomicilios(domicilioInstanciado)
                    }
                }
            }
        }
        if(personaRelacionada.documentoEmpresa || personaRelacionada.razonSocialEmpresa) {
            persona.empresa = principal.empresa
            if (personaRelacionada.telefonosEmpresa) {
                personaRelacionada.telefonosEmpresa.each { telefonoAutonomo ->
                    if (!persona.telefonos.any { it.numero == telefonoAutonomo }) {
                        Sitio sitioTelefono = Sitio.findById(personaRelacionada.sitioId)
                        persona.addToTelefonos(new Telefono(numero: telefonoAutonomo, estado: Estado.findByNombre('Sin Confirmar'), origen: sitioTelefono, trabajo: true))
                    }
                }
            }
        }
        persona.save(flush:true, failOnError:true)
        return persona
    }

    public List<Persona> savePersonasDireccion(List<PersonaCommand> personas) {
        List<Persona> personasInstanciadas = []
        Map<Persona, PersonaCommand> personasMap = [:]
        personas.each { personaCommand ->
            Persona persona
            if (personaCommand.documento != null && personaCommand.documento != "DESCONOCIDO" ){
                persona = Persona.findByDocumento(personaCommand.documento)
            }
            if(!persona){
                persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadreAndAnoNacimiento(personaCommand.primerNombre, personaCommand.segundoNombre, personaCommand.apellidoPadre, personaCommand.apellidoMadre, personaCommand.anoNacimiento)
            }
            if(!persona){
                persona = Persona.findByPrimerNombreAndSegundoNombreAndApellidoPadreAndApellidoMadre(personaCommand.primerNombre, personaCommand.segundoNombre, personaCommand.apellidoPadre, personaCommand.apellidoMadre)
            }
            if (!persona) {
                persona = new Persona()
                persona.primerNombre = personaCommand.primerNombre?.capitalize()
                persona.segundoNombre = personaCommand.segundoNombre?.capitalize()
                persona.apellidoPadre = personaCommand.apellidoPadre?.capitalize()
                persona.apellidoMadre = personaCommand.apellidoMadre?.capitalize()
                persona.anoNacimiento = personaCommand.anoNacimiento

                persona.poblacionNacimiento = domicilioService.getPoblacion(personaCommand.poblacionNacimientoId)

                persona.documento = personaCommand.documento
                persona.oficina = Oficina.findById(personaCommand.oficinaId)

                persona.save(flush:true, failOnError:true)

                if (personaCommand.domicilios) {
                    personaCommand.domicilios.each { domicilio ->
                        Domicilio domicilioInstanciado = Domicilio.findByCalleAndNumeroAndBloqueAndPortalAndEscaleraAndPisoAndPuertaAndProvinciaAndPersona(domicilio.calle, domicilio.numero, domicilio.bloque, domicilio.portal, domicilio.escalera, domicilio.piso, domicilio.puerta, Provincia.get(domicilio.provinciaId), persona)
                        if (!domicilioInstanciado){
                            domicilioInstanciado = new Domicilio()
                            domicilioInstanciado.provincia = Provincia.get(domicilio.provinciaId)
                            domicilioInstanciado.poblacion = Poblacion.get(domicilio.poblacionId)
                            domicilioInstanciado.calle = domicilio.calle
                            domicilioInstanciado.numero = domicilio.numero
                            domicilioInstanciado.bloque = domicilio.bloque
                            domicilioInstanciado.portal = domicilio.portal
                            domicilioInstanciado.escalera = domicilio.escalera
                            domicilioInstanciado.piso = domicilio.piso
                            domicilioInstanciado.puerta = domicilio.puerta
                            domicilioInstanciado.codigoPostal = domicilio.codigoPostal
                            domicilioInstanciado.estado = Estado.get(domicilio.estadoId)
                            domicilioInstanciado.persona = persona
                            domicilioInstanciado.domicilioAnterior = domicilio.domicilioAnterior ?: false
                            domicilioInstanciado.save(flush:true, failOnError:true)
                            persona.addToDomicilios(domicilioInstanciado)
                        }
                    }
                }
                persona.save(flush:true, failOnError:true)                
            }
            personasInstanciadas.add(persona)
        }
        for (int i = 0; i < personasInstanciadas.size(); i++) {
            def personaPrincipal = personasInstanciadas[i]
            for (int j = 0; j < personasInstanciadas.size(); j++) {
                def otraPersona = personasInstanciadas[j]
                if (i != j) {
                    tipoRelacionService.asignarTipoRelacion(personaPrincipal, otraPersona)
                }
            }
        }

        return personasInstanciadas
    }

    public Telefono getTelefono(Long id) {
        return Telefono.get(id)
    }

    public Persona getPersona(Long id) {
        return Persona.get(id)
    }

    public PersonaRelacionada getPersonaRelacionada(Long id) {
        return PersonaRelacionada.get(id)
    }

    public List<Telefono> listTelefonosByPersona(Long personaId) {
        def telefonos = Telefono.createCriteria().list {
            eq('persona.id', personaId)
        }
        return telefonos
    }

    public Persona updatePersona(PersonaCommand personaEditada) { 

        Persona persona = getPersona(personaEditada.id)
        int year = new DateTime().now().year().get();
        assert personaEditada?.anoNacimiento <= year : "El año de nacimiento es inválidofinerror"
        assert personaEditada?.primerNombre : "El primer nombre de la persona es inválidofinerror"
        assert personaEditada?.apellidoPadre : "El primer apellido de la persona es inválidofinerror"
        
        persona.primerNombre = personaEditada?.primerNombre?.toUpperCase()
        persona.segundoNombre = personaEditada?.segundoNombre?.toUpperCase()
        persona.apellidoPadre = personaEditada?.apellidoPadre?.toUpperCase()
        persona.apellidoMadre = personaEditada?.apellidoMadre?.toUpperCase()
        persona.tipoSociedad = personaEditada?.tipoSociedad
        persona.anoNacimiento = personaEditada?.anoNacimiento
        persona.documento = personaEditada?.documento
        persona.poblacionNacimiento = domicilioService.getPoblacion(personaEditada?.poblacionNacimientoId) ?: null
        persona.autonomo = personaEditada?.autonomo

        return persona.save(flush: true, failOnError:true) 
    }

    public List<Telefono> updateTelefonos(PersonaCommand personaCommand, List<Telefono> telefonosEditados) { 

        Persona persona = getPersona(personaCommand.id)

        List<Telefono> telefonos = Telefono.findAllByPersona(persona)
        
        telefonos.each { telefono ->
            def telefonoEditado = telefonosEditados.find { it.id == telefono.id }
            if (telefonoEditado) {
                assert telefonoEditado.numero : "Un telefono no tiene numerofinerror"
                assert telefonoEditado.origen : "Un telefono no tiene indicado su origenfinerror"

                telefono.numero = telefonoEditado?.numero
                telefonoEditado.origen = Sitio.findByNombre(telefonoEditado.origen.nombre) ?: null 
                telefono.origen = telefonoEditado?.origen ?: null
                telefono.estado = Estado.findByNombre(telefonoEditado?.estado?.nombre)
                telefono.save(flush: true, failOnError: true)
            }
        }
        return telefonos
    }

    public List<PersonaRelacionada> updatePersonasRelacionadas(PersonaCommand personaCommand, List<PersonaRelacionada> personasEditadas) { 
        Persona persona = getPersona(personaCommand.id)
        List<PersonaRelacionada> personasRelacionadas = PersonaRelacionada.findAllByPrincipal(persona)    
        personasRelacionadas.each { personaRelacionada ->
            def personaEditada = personasEditadas.find { it.id == personaRelacionada.relacionada.id }
            if (personaEditada) {
                personaRelacionada.relacion = TipoRelacion.get(personaEditada.relacionid) ?: null
                personaRelacionada.estado = Estado.findByNombre(personaEditada?.estado) ?: null
                personaRelacionada.save(flush: true, failOnError: true)
            }
        }
        return personasRelacionadas
    }

    public Persona delete(Long id){
        Persona persona = Persona.get(id)
        List<PersonaRelacionada> personasRelacionadasPrincipal = PersonaRelacionada.findAllByPrincipal(persona)
        List<PersonaRelacionada> personasRelacionadasRelacionada = PersonaRelacionada.findAllByRelacionada(persona)
        List<PersonaBusqueda> personasBusqueda = PersonaBusqueda.findAllByPersona(persona)

        personasRelacionadasPrincipal.each { personaRelacionada ->
            personaRelacionada.delete(flush: true, failOnError: true)
        }
        personasRelacionadasRelacionada.each { personaRelacionada ->
            personaRelacionada.delete(flush: true, failOnError: true)
        }
        personasBusqueda.each { personaBusq ->
            personaBusq.delete(flush:true, failOnError:true)
        }
        persona.delete(flush: true, failOnError: true)
        return 
    }

    def getPersonaCommand(Long id) {
        def persona = getPersona(id)
        def personaCommand = new PersonaCommand()

        personaCommand.id = persona.id
        personaCommand.version = persona.version
        personaCommand.primerNombre = persona.primerNombre?.toUpperCase()
        personaCommand.segundoNombre =persona.segundoNombre?.toUpperCase() ?: null
        personaCommand.apellidoPadre =persona.apellidoPadre?.toUpperCase() 
        personaCommand.apellidoMadre =persona.apellidoMadre?.toUpperCase() ?: null
        personaCommand.poblacionNacimientoId = persona.poblacionNacimiento?.id ?: null
        personaCommand.autonomo = persona.autonomo ?: null
        personaCommand.documento = persona.documento
        personaCommand.anoNacimiento =persona.anoNacimiento ?: null
        personaCommand.tipoSociedad = persona.tipoSociedad ?: null
        personaCommand.oficinaId = persona.oficina.id
        return personaCommand
    }
}
