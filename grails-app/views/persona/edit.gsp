<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div class="main-body">
        <div class="page-wrapper">
            <form action="${createLink(controller: 'persona', action: 'update')}" method="post">
                <div class="page-header card">
                    <div class="row ">
                        <div class="col-lg-9 col-md-9">
                            <div class="page-header-title">
                                <div class="d-inline">
                                    <h4>Editar detalles de ${personaCommand?.primerNombre} ${personaCommand?.segundoNombre} ${personaCommand?.apellidoPadre} ${personaCommand?.apellidoMadre} </h4>

                                    <g:hiddenField name="id" value="${personaCommand?.id}" />
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-3 d-flex justify-content-end" style="padding-right: 0; margin-right: 0; ">
                            <g:link controller="persona" class="btn btn-volver" action="show"
                                params="[id: personaCommand.id]">Volver</g:link>
                            <button onclick="borrarPersona('${personaCommand.id}')" class="btn btn-primary"
                                href="javascript:;" style="margin-left: 5px;" type="button">Borrar</button>
                            <button class="btn btn-success" type="submit" style="margin-left: 5px;">Guardar</button>
                        </div>
                    </div>
                </div>
                <div class="page-body">
                    <div class="row">
                        <div class="col-6">
                            <div class="card" >
                                <div class="card-block user-detail-card" >
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Datos Personales</h4>
                                    </div>
                                    <div class="user-detail">
                                        <div class="row" style="border-bottom: 0em;" >
                                            <div class="col-5 col-md-5 col-lg-4" >
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-id"></i>Documento
                                                </h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-8 ">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="documento" id="documento"
                                                    value="${personaCommand?.documento}">
                                            </div>
                                        </div>

                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-4">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-ui-user"></i>Nombre
                                                </h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-8">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="primerNombre" id="primerNombre"
                                                    value="${personaCommand?.primerNombre}">
                                            </div>
                                        </div>

                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-4">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-ui-user"></i>Segundo Nombre</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-8">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="segundoNombre" id="segundoNombre"
                                                    value="${personaCommand?.segundoNombre}">
                                            </div>
                                        </div>

                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-4">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-users"></i>Apellido
                                                    Padre</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-8">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="apellidoPadre" id="apellidoPadre"
                                                    value="${personaCommand?.apellidoPadre}">
                                            </div>
                                        </div>

                                        <div class="row" style="border-bottom: none; padding-bottom: 0; border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-4">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-users"></i>Apellido
                                                    Madre</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-8">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="apellidoMadre" id="apellidoMadre"
                                                    value="${personaCommand?.apellidoMadre}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="card">
                                <div class="card-block user-detail-card">
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Datos Razón Social</h4>
                                    </div>
                                    <div class="user-detail">
                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-5">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-people"></i>Tipo
                                                    Sociedad</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-7">
                                                <input type="text" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="tipoSociedad" id="tipoSociedad"
                                                    value="${personaCommand?.tipoSociedad}">
                                            </div>
                                        </div>
                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-5">
                                                <h6 class="f-w-400 m-b-30"><i
                                                        class="icofont icofont-businessman"></i>Autónomo</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-7">
                                                <input type="radio" id="autonomoTrue" name="autonomo" value="true" />
                                                <label for="autonomoTrue" class="mr-3">SI</label>
                                                <input type="radio" id="autonomoFalse" name="autonomo" value="false" />
                                                <label for="autonomoFalse">NO</label>
                                            </div>
                                        </div>

                                        <div class="row" style="border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-5">
                                                <h6 class="f-w-400 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-calendar"></i>Año
                                                    de Nacimiento</h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-7">
                                                <input type="number" class="form-control m-b-30"
                                                    style="margin-bottom: 0px;" name="anoNacimiento" id="anoNacimiento"
                                                    value="${personaCommand?.anoNacimiento}">
                                            </div>
                                        </div>

                                        <div class="row" style="padding-bottom: 0; border-bottom: 0em;">
                                            <div class="col-5 col-md-5 col-lg-5">
                                                <h6 class="f-w-450 m-b-30" style="padding-top: 7px;"><i
                                                        class="icofont icofont-location-pin"></i>Población de Nacimiento
                                                </h6>
                                            </div>
                                            <div class="col-7 col-md-7 col-lg-7">
                                                <div class="row">
                                                    <select id="cbProvinciaPoblacionNacimiento" class="form-control m-b-30" style="margin-bottom: 0px;" name="provinciaPoblacionNacimiento"></select>
                                                </div>
                                                <div class="row" style="margin-top: 15px;">
                                                    <select id="cbPoblacionNacimiento" class="form-control m-b-30" style="margin-bottom: 0px;" name="poblacionNacimientoId"></select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-block">
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Teléfonos</h4>
                                    </div>
                                    <div class="dt-responsive table-responsive">
                                        <table id="listTelefonos" class="table table-striped table-bordered nowrap"
                                            style="cursor: pointer">
                                            <thead>
                                                <tr>
                                                    <th>Estado</th>
                                                    <th>Origen</th>
                                                    <th>Número</th>
                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-block">
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Domicilios</h4>
                                    </div>
                                    <div class="dt-responsive table-responsive">
                                        <table id="listDomicilios" class="table table-striped table-bordered nowrap"
                                            style="cursor: pointer">
                                            <thead>
                                                <tr>
                                                    <th>Estado</th>
                                                    <th>Calle</th>
                                                    <th>Número</th>
                                                    <th>Población</th>
                                                    <th>Provincia</th>
                                                    <th>Bloque</th>
                                                    <th>Portal</th>
                                                    <th>Piso</th>
                                                    <th>Puerta</th>
                                                    <th>Código postal</th>
                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-block">
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Personas Relacionadas</h4>
                                    </div>
                                    <div class="dt-responsive table-responsive">
                                        <table id="listPersonasRelacionadas"
                                            class="table table-striped table-bordered nowrap" style="cursor: pointer">
                                            <thead>
                                                <tr>
                                                    <th>Tipo de Relación</th>
                                                    <th>Primer Nombre</th>
                                                    <th>Segundo Nombre</th>
                                                    <th>Apellido Padre</th>
                                                    <th>Apellido Madre</th>
                                                    <th>Documento</th>
                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <g:hiddenField name="datosForm" />
            </form>

            <!-- Modal domicilio -->
            <div class="card-block user-detail-card">
                <div class="modal fade" id="modalDomicilio" tabindex="-1" role="dialog"
                    aria-labelledby="modalDomicilioLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-center" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalDomicilioLabel">Detalles del Domicilio </h5>
                            </div>
                            <div class="modal-body" style="padding-bottom: 0; padding-top: 0;">
                                <div class="col-12 user-detail">
                                    <div class="row" style="border-bottom: 0em;">
                                        <input type="hidden" id="domicilioEstado" name="domicilioEstado" value="">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 4px;">Estado
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <div class="row d-flex justify-content-end" style="padding-right: 15px;">
                                                <div style="margin-right: 5px;">
                                                    <button type="button" class="btn text-center" id="btnDomicilioEstadoConfirmado"
                                                        style="background-color: transparent; font-size: 20px; padding: 0; border-radius: 0;"
                                                        name="domicilioEstado"><i class="icofont icofont-ui-check"
                                                            style="color: #77a649; padding-left: 8px;"></i></button>
                                                </div>
                                                <div style="margin-left: 15px;">
                                                    <button type="button" class="btn" id="btnDomicilioEstadoErroneo"
                                                        style="background-color: transparent;  font-size: 20px; padding: 0;"
                                                        name="domicilioEstado"><i
                                                            class="icofont icofont-ui-close" style="padding-left: 10px;"></i></button>
                                                </div>
                                                <div class="row d-flex justify-content-end"
                                                    style="padding-right: 15px;">
                                                    <h6 id="domicilioEstadoConfirmado" class="m-b-30"></h6>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Provincia
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <select name="provincia" id="cbProvincia"
                                                class="form-control form-select form-select-sm"></select>
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Población
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <select name="poblacion" id="cbPoblacion"
                                                class="form-control form-select form-select-sm"></select>
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Calle</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioCalle"
                                                id="domicilioCalle">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Número</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioNumero"
                                                id="domicilioNumero">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Bloque</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioBloque"
                                                id="domicilioBloque">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Portal</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioPortal"
                                                id="domicilioPortal">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Escalera</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioEscalera"
                                                id="domicilioEscalera">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Piso</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioPiso"
                                                id="domicilioPiso">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Puerta</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioPuerta"
                                                id="domicilioPuerta">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em; padding-bottom: 20px;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Código
                                                Postal</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="domicilioCodigoPostal"
                                                id="domicilioCodigoPostal">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="buttonLocalVolver" type="button" class="btn btn-volver"
                                    data-dismiss="modal">
                                    Volver</button>
                                <button id="btnGuardarCambiosDomicilio" type="button" class="btn btn-success"
                                    data-dismiss="modal">
                                    Aceptar</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal telefono -->
                <div class="modal fade" id="modalTelefono" tabindex="-1" role="dialog"
                    aria-labelledby="modalDomicilioLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-center" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalTelefonoLabel">Detalles del Teléfono</h5>
                            </div>
                            <div class="modal-body" style="padding-bottom: 0; padding-top: 0;">
                                <div class="col-12 user-detail">
                                    <div class="row" style="border-bottom: 0em;">
                                        <input type="hidden" id="telefonoEstado" name="telefonoEstado" value="">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 4px;">Estado
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <div class="row d-flex justify-content-end" style="padding-right: 15px;">
                                                <div style="margin-right: 5px;">
                                                    <button type="button" class="btn" id="btnConfirmarTelefono"
                                                        style="background-color: transparent; font-size: 20px; padding: 0;"><i
                                                            class="icofont icofont-ui-check"
                                                            style="color: #77a649; padding-left: 10px;"></i></button>
                                                </div>
                                                <div style="margin-left: 15px;">
                                                    <button type="button" class="btn" id="btnErroneoTelefono"
                                                        style="background-color: transparent;  font-size: 20px; padding: 0;"><i
                                                            class="icofont icofont-ui-close" style="padding-left: 10px;"></i></button>
                                                </div>
                                            </div>
                                            <div class="row d-flex justify-content-end" style="padding-right: 15px;">
                                                <h6 id="telefonoEstadoConfirmado" class="m-b-30"></h6>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 style="padding-top: 7px;" class="f-w-400 m-b-30">Número
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <input type="text" class="form-control" name="telefonoNumero"
                                                id="telefonoNumero">
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em; padding-bottom: 20px;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 style="padding-top: 7px;" class="f-w-400 m-b-30">Origen
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <select id="cbSitio" class="form-control" name="telefonoOrigen"></select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="buttonLocalVolver" type="button" class="btn btn-volver"
                                    data-dismiss="modal">
                                    Volver</button>
                                <button id="btnGuardarCambiosTelefono" type="button" class="btn btn-success"
                                    data-dismiss="modal">
                                    Aceptar</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal para Personas Relacionadas -->
                <div class="modal fade" id="modalPersonaRelacionada" tabindex="-1" role="dialog"
                    aria-labelledby="modalPersonaRelacionadaLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-center" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalPersonaRelacionadaLabel">Detalles de Persona
                                    Relacionada
                                </h5>
                            </div>
                            <div class="modal-body" style="padding-bottom: 0; padding-top: 0;">
                                <div class="col-12 user-detail">
                                    <div class="row" style="border-bottom: 0em;">
                                        <input type="hidden" id="relacionEstado" name="relacionEstado" value="">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 4px;">Estado
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <div class="row d-flex justify-content-end" style="padding-right: 15px;">
                                                <div style="margin-right: 5px;">
                                                    <button type="button" class="btn" id="btnConfirmarRelacion"
                                                        style="background-color: transparent; font-size: 20px; padding: 0;"><i
                                                            class="icofont icofont-ui-check"
                                                            style="color: #77a649; padding-left: 10px;"></i></button>
                                                </div>
                                                <div style="margin-left: 15px;">
                                                    <button type="button" class="btn" id="btnRelacionErronea"
                                                        style="background-color: transparent;  font-size: 20px; padding: 0;"><i
                                                            class="icofont icofont-ui-close" style="padding-left: 10px;"></i></button>
                                                </div>
                                            </div>
                                            <div class="row d-flex justify-content-end" style="padding-right: 15px;">
                                                <h6 id="relacionEstadoConfirmado" class="m-b-30"></h6>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" style="border-bottom: 0em; padding-bottom: 20px;">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30" style="padding-top: 7px;">Relación
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <select id="cbRelacion" class="form-control" name="tipoRelacion"></select>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button id="buttonLocalVolver" type="button" class="btn btn-volver"
                                        data-dismiss="modal">
                                        Volver</button>
                                    <button id="btnGuardarCambiosPersonaRelacionada" type="button"
                                        class="btn btn-success" data-dismiss="modal">
                                        Aceptar</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script type="text/javascript">
            var tablaTelefono;
            var tablaDomicilio;
            var tablaPersonasRelacionadas;
            jQuery(document).ready(function () {

                const autonomoValue = '${personaCommand.autonomo}';
                if (autonomoValue === 'true') {
                    $('#autonomoTrue').prop('checked', true);
                } else {
                    $('#autonomoFalse').prop('checked', true);
                }

                tablaTelefono = $('#listTelefonos').DataTable({
                    //bAutoWidth: false,
                    //bSortCellsTop: true,
                    //BProcessing: true,
                    "ordering": true,
                    "searching": true,
                    oLanguage: {
                        sProcessing: "Buscando...",
                        sSearch: "",
                        sLengthMenu: "_MENU_",
                        sZeroRecords: "No se encontraron registros",
                        sInfo: "_START_ - _END_ de _TOTAL_",
                        sInfoFiltered: "${message(code: 'default.datatable.infoFiltered', default: '(filtrado de MAX registros en total)')}",
                        sInfoPostFix: "",
                        sUrl: "",
                        sInfoEmpty: "${message(code: 'default.datatable.infoEmpty', default: '0 de 0')}",
                        oPaginate: {
                            "sFirst": "${message(code: 'default.datatable.paginate.first', default: 'Primero')}",
                            "sPrevious": "<",
                            "sNext": ">",
                            "sLast": "${message(code: 'default.datatable.paginate.last', default: '&Uacute;ltimo')}"
                        }
                    },
                    aaSorting: [
                        [1, 'desc']
                    ],
                    aoColumnDefs: [{
                        "aTargets": [0],
                        "mData": "estado.nombre"
                    }, {
                        "aTargets": [1],
                        "mData": function (row) {
                            return row.origen?.nombre || null; // Usa el operador de navegación segura
                        }
                    }, {
                        "aTargets": [2],
                        "mData": "numero",
                    }],
                    buttons: [],
                    sPaginationType: 'simple',
                    sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                    fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                        $(nRow).on('click', function () {
                            showModalTelefono(aData)
                        });
                    }
                });
                llenarDatosListTelefono();

                tablaDomicilio = $('#listDomicilios').DataTable({
                    //bAutoWidth: false,
                    //bSortCellsTop: true,
                    //BProcessing: true,
                    "ordering": true,
                    "searching": true,
                    oLanguage: {
                        sProcessing: "Buscando...",
                        sSearch: "",
                        sLengthMenu: "_MENU_",
                        sZeroRecords: "No se encontraron registros",
                        sInfo: "_START_ - _END_ de _TOTAL_",
                        sInfoFiltered: "${message(code: 'default.datatable.infoFiltered', default: '(filtrado de MAX registros en total)')}",
                        sInfoPostFix: "",
                        sUrl: "",
                        sInfoEmpty: "${message(code: 'default.datatable.infoEmpty', default: '0 de 0')}",
                        oPaginate: {
                            "sFirst": "${message(code: 'default.datatable.paginate.first', default: 'Primero')}",
                            "sPrevious": "<",
                            "sNext": ">",
                            "sLast": "${message(code: 'default.datatable.paginate.last', default: '&Uacute;ltimo')}"
                        }
                    },
                    aaSorting: [
                        [1, 'desc']
                    ],
                    aoColumnDefs: [{
                        "aTargets": [0],
                        "mData": "estado.nombre",
                    }, {
                        "aTargets": [1],
                        "mData": "calle"
                    }, {
                        "aTargets": [2],
                        "mData": "numero",
                    }, {
                        "aTargets": [3],
                        "mData": function (row) {
                            return row.poblacion?.nombre || null; // Usa el operador de navegación segura
                        }
                    }, {
                        "aTargets": [4],
                        "mData": function (row) {
                            return row.provincia?.nombre || null; // Usa el operador de navegación segura
                        }
                    }, {
                        "aTargets": [5],
                        "mData": "bloque",
                    }, {
                        "aTargets": [6],
                        "mData": "portal",
                    }, {
                        "aTargets": [7],
                        "mData": "piso",
                    }, {
                        "aTargets": [8],
                        "mData": "puerta",
                    }, {
                        "aTargets": [9],
                        "mData": "codigoPostal",
                    }],
                    buttons: [],
                    sPaginationType: 'simple',
                    sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                    fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                        $(nRow).on('click', function () {
                            showModalDomicilio(aData)
                        });
                    }
                });
                llenarDatosListDomicilio();

                tablaPersonasRelacionadas = $('#listPersonasRelacionadas').DataTable({
                    //bAutoWidth: false,
                    //bSortCellsTop: true,
                    //BProcessing: true,
                    "ordering": true,
                    "searching": true,
                    oLanguage: {
                        sProcessing: "Buscando...",
                        sSearch: "",
                        sLengthMenu: "_MENU_",
                        sZeroRecords: "No se encontraron registros",
                        sInfo: "_START_ - _END_ de _TOTAL_",
                        sInfoFiltered: "${message(code: 'default.datatable.infoFiltered', default: '(filtrado de MAX registros en total)')}",
                        sInfoPostFix: "",
                        sUrl: "",
                        sInfoEmpty: "${message(code: 'default.datatable.infoEmpty', default: '0 de 0')}",
                        oPaginate: {
                            "sFirst": "${message(code: 'default.datatable.paginate.first', default: 'Primero')}",
                            "sPrevious": "<",
                            "sNext": ">",
                            "sLast": "${message(code: 'default.datatable.paginate.last', default: '&Uacute;ltimo')}"
                        }
                    },
                    aaSorting: [
                        [1, 'desc']
                    ],
                    aoColumnDefs: [
                        {
                        "aTargets": [0],
                        "mData": "nombrerelacion",
                        "mRender": function (data, type, full) {
                            var estado = full.estado;
                            var iconClass = '';
                            var iconColor = '';

                            if (estado === "Confirmado") {
                                iconClass = 'icofont icofont-ui-check';
                                iconColor = '#424949';
                            } else if (estado === "Erróneo") {
                                iconClass = 'icofont icofont-ui-close';
                                iconColor = '#424949';
                            }
                            var tooltip = "Estado: " + estado;                   
                            return data + "<span style='margin-left: 20px;'></span> <i class='" + iconClass + "' style='color: " + iconColor + "' title='" + tooltip + "'></i>";
                            }
                        }, {
                            "aTargets": [1],
                            "mData": "primernombre",
                        }, {
                            "aTargets": [2],
                            "mData": "segundonombre",
                        }, {
                            "aTargets": [3],
                            "mData": "apellidopadre",
                        }, {
                            "aTargets": [4],
                            "mData": "apellidomadre",
                        }, {
                            "aTargets": [5],
                            "mData": "documento",
                        }],
                    buttons: [],
                    sPaginationType: 'simple',
                    sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                    fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                        $(nRow).off('click').on('click', function () {
                            showModalPersonaRelacionada(aData)
                        });
                    }

                });
                llenarDatosListPersonaRelacionada('${personaCommand.id}');
                guardarDatos();
            });

            function guardarDatos() {
                // Obtener y preparar los datos de telefonos, domicilios, personas relacionadas, etc.
                var datos = {
                    telefonos: obtenerDatosTabla(tablaTelefono),
                    domicilios: obtenerDatosTabla(tablaDomicilio),
                    personasRelacionadas: obtenerDatosTabla(tablaPersonasRelacionadas)
                    // Agrega más datos según sea necesario
                };
                // Convertir datos a JSON y asignar al campo oculto
                var datosString = JSON.stringify(datos);
                $("#datosForm").val(datosString);
            }


            function obtenerDatosTabla(tabla) {
                var datos = tabla.rows().data().toArray();
                return datos;
            }
            function showModalTelefono(data = null) {
                telefonoId = data ? data.id : null;
                $("#telefonoNumero").val(data ? data.numero : '');
                $("#telefonoOrigen").val(data ? data.origen : '');
                llenarCombo({
                    comboId: "cbSitio",
                    ajaxLink: "${createLink(controller: 'credencial', action: 'ajaxGetSitios')}",
                    idDefault: data.origen?.id,
                    atributo: "nombre"
                });
                $.ajax("${createLink(controller:'persona', action:'ajaxGetTelefono')}/" + data.id, {
                    dataType: "json",
                    telefono: {
                    }
                }).done(function (telefono) {
                    if (data.estado.nombre != 'Sin Confirmar' && data.estado.nombre === telefono.estado.nombre) {
                        $('#btnConfirmarTelefono').hide();
                        $('#btnErroneoTelefono').hide();
                        $('#telefonoEstadoConfirmado').show();
                        var telefonoEstadoVal;
                        var telefonoEstadoText;
                        if (telefono.estado.nombre === 'Confirmado') {
                            telefonoEstadoVal = telefono.estado.nombre
                            telefonoEstadoText = telefono.estado.nombre
                        } else if (telefono.estado.nombre === 'Erróneo') {
                            telefonoEstadoVal = telefono.estado.nombre
                            telefonoEstadoText = telefono.estado.nombre
                        }
                        $('#telefonoEstadoConfirmado').text(telefonoEstadoText);
                        $("#telefonoEstado").val(telefonoEstadoVal)
                    } else if (telefono.estado.nombre === 'Sin Confirmar') {
                        $('#btnConfirmarTelefono').show();
                        $('#btnErroneoTelefono').show();
                        $("#telefonoEstado").val(telefono.estado.nombre)
                        $('#telefonoEstadoConfirmado').hide();
                    }
                });
                $("#telefonoEstado").val(null);
                $('#modalTelefono').modal('show');
            }

            $("#cbProvincia").change(function () {
                var selectedValue = $(this).val();
                var selectedPoblacion
                if ($("#cbPoblacion").val()) {
                    selectedPoblacion = $("#cbPoblacion").val()
                }
                llenarCombo({
                    comboId: "cbPoblacion",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetPoblaciones')}/" + selectedValue,
                    idDefault: selectedPoblacion,
                    identificador: 'id',
                    atributo: "nombre"
                })
            });

            function showModalDomicilio(data = null) {
                $("#domicilioCalle").val(data ? data.calle : '');
                $("#domicilioNumero").val(data ? data.numero : '');
                $("#domicilioBloque").val(data ? data.bloque : '');
                $("#domicilioPortal").val(data ? data.portal : '');
                $("#domicilioEscalera").val(data ? data.escalera : '');
                $("#domicilioPiso").val(data ? data.piso : '');
                $("#domicilioPuerta").val(data ? data.puerta : '');
                $("#domicilioCodigoPostal").val(data ? data.codigoPostal : '');
                $("#domicilioPortal").val(data ? data.portal : '');
                llenarCombo({
                    comboId: "cbProvincia",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetProvincias')}",
                    idDefault: data.provincia.id,
                    identificador: 'id',
                    atributo: "nombre"
                });
                $.ajax("${createLink(controller:'domicilio', action:'ajaxGetDomicilio')}/" + data.id, {
                    dataType: "json",
                    telefono: {
                    }
                }).done(function (domicilio) {
                    if (data.estado.nombre != 'Sin Confirmar' && data.estado.nombre === domicilio.estado.nombre) {
                        $('#btnDomicilioEstadoConfirmado').hide();
                        $('#btnDomicilioEstadoErroneo').hide();
                        $('#domicilioEstadoConfirmado').show();
                        var domicilioEstadoVal;
                        var domicilioEstadoText;
                        if (domicilio.estado.nombre === 'Confirmado') {
                            domicilioEstadoVal = domicilio.estado.nombre
                            domicilioEstadoText = domicilio.estado.nombre
                        } else if (domicilio.estado.nombre === 'Erróneo') {
                            domicilioEstadoVal = domicilio.estado.nombre
                            domicilioEstadoText = domicilio.estado.nombre
                        }                        
                        $('#domicilioEstadoConfirmado').text(domicilioEstadoText);
                        $("#domicilioEstado").val(domicilioEstadoVal)
                    } else if (domicilio.estado.nombre === 'Sin Confirmar') {
                        $('#btnDomicilioEstadoConfirmado').show();
                        $('#btnDomicilioEstadoErroneo').show();
                        $("#domicilioEstado").val(domicilio.estado.nombre)
                        $('#domicilioEstadoConfirmado').hide();
                    }
                });
                llenarCombo({
                    comboId: "cbPoblacion",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetPoblaciones')}/" + data.provincia.id,
                    idDefault: data.poblacion?.id,
                    identificador: 'id',
                    atributo: "nombre"
                })
                domicilioId = data ? data.id : null;
                $('#modalDomicilio').modal('show');
            }

            function showModalPersonaRelacionada(data = null) {
                $("#relacion").val(data ? data.estado.nombre : '');
                llenarCombo({
                    comboId: "cbRelacion",
                    ajaxLink: "${createLink(controller: 'tipoRelacion', action: 'ajaxGetTiposRelacion')}",
                    idDefault: data.relacionid,
                    atributo: "nombre"
                });
                $.ajax("${createLink(controller:'persona', action:'ajaxGetPersonaRelacionada')}/" + data.personarelacionadaid, {
                    dataType: "json",
                    telefono: {
                    }
                }).done(function (personaRelacionada) {
                    if (data.estado.nombre != 'Sin Confirmar' && data.estado.nombre === personaRelacionada.estado.nombre) {
                        $('#btnConfirmarRelacion').hide();
                        $('#btnRelacionErronea').hide();
                        $('#relacionEstadoConfirmado').show();
                        var relacionEstadoVal;
                        var relacionEstadoText;
                        if (personaRelacionada.estado.nombre === 'Confirmado') {
                            relacionEstadoVal = personaRelacionada.estado.nombre
                            relacionEstadoText = personaRelacionada.estado.nombre
                        } else if (personaRelacionada.estado.nombre === 'Erróneo') {
                            relacionEstadoVal = personaRelacionada.estado.nombre
                            relacionEstadoText = personaRelacionada.estado.nombre
                        }     
                        $('#relacionEstadoConfirmado').text(relacionEstadoText);
                        $("#relacionEstado").val(relacionEstadoVal)
                    } else if (personaRelacionada.estado.nombre === 'Sin Confirmar') {
                        $('#btnConfirmarRelacion').show();
                        $('#btnRelacionErronea').show();
                        $("#relacionEstado").val(personaRelacionada.estado.nombre)
                        $('#relacionEstadoConfirmado').hide();
                    }
                });
                personaRelacionada = data.relacionada
                personaRelacionadaId = data ? data.id : null;
                primernombre = data ? data.primernombre : null;
                segundonombre = data ? data.segundonombre : null;
                apellidopadre = data ? data.apellidopadre : null;
                apellidomadre  = data ? data.apellidomadre : null;
                nombrerelacion = data ? data.nombrerelacion : null;
                documento = data ? data.documento : null;
                estado = data ? data.estado : null;
                $('#modalPersonaRelacionada').modal('show');
            }

            function llenarDatosListTelefono() {
                tablaTelefono.clear().draw();
                $.ajax("${createLink(controller:'persona', action:'ajaxGetTelefonosByPersona')}/" + '${personaCommand.id}', {
                    dataType: "json",
                    data: {
                    }
                }).done(function (data) {
                    $("#loaderGrande").fadeOut("slow");
                    tablaTelefono.rows.add(data)
                    tablaTelefono.draw();
                });
            }

            function llenarDatosListDomicilio() {
                tablaDomicilio.clear().draw();
                $.ajax("${createLink(controller:'domicilio', action:'ajaxGetDomiciliosByPersona')}/" + '${personaCommand.id}', {
                    dataType: "json",
                    data: {
                    }
                }).done(function (data) {
                    $("#loaderGrande").fadeOut("slow");
                    tablaDomicilio.rows.add(data)
                    tablaDomicilio.draw();
                });
            }

            function llenarDatosListPersonaRelacionada(personaId) {
                tablaPersonasRelacionadas.clear().draw();
                $.ajax("${createLink(controller:'persona', action:'ajaxGetPersonasRelacionadas')}/" + personaId, {
                    dataType: "json",
                    data: {
                    }
                }).done(function (data) {
                    $("#loaderGrande").fadeOut("slow");
                    tablaPersonasRelacionadas.rows.add(data)
                    tablaPersonasRelacionadas.draw();
                });
            }

            $("#cbProvincia").select2({
                placeholder: 'Seleccione la provincia',
                formatNoMatches: function () {
                    return '<g:message code="custom.no.matches.message" default="No hay elementos"/>';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                minimumResultsForSearch: 1,
                dropdownParent: $("#modalDomicilio"),
                formatSelection: function (item) {
                    return item.text;
                }
            });

            llenarCombo({
                    comboId: "cbProvinciaPoblacionNacimiento",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetProvincias')}",
                    idDefault: '${personaPoblacionNacimiento?.provincia?.id}',
                    identificador: 'id',
                    atributo: "nombre",
            });

            $("#cbProvinciaPoblacionNacimiento").select2({
                placeholder: 'Seleccione la provincia',
                formatNoMatches: function () {
                    return '<g:message code="custom.no.matches.message" default="No hay elementos"/>';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                minimumResultsForSearch: 1,
                formatSelection: function (item) {
                    return item.text;
                }
            });

            $("#cbPoblacionNacimiento").select2({
                placeholder: 'Seleccione la población',
                formatNoMatches: function () {
                    return '<g:message code="custom.no.matches.message" default="No hay elementos"/>';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                minimumResultsForSearch: 1,
                formatSelection: function (item) {
                    return item.text;
                }
            });

            $("#cbProvinciaPoblacionNacimiento").change(function () {
                var selectedValue = $(this).val();
                var selectedPoblacion
                if ($("#cbPoblacionNacimiento").val()) {
                    selectedPoblacion = $("#cbPoblacionNacimiento").val()
                }
                llenarCombo({
                    comboId: "cbPoblacionNacimiento",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetPoblaciones')}/" + selectedValue,
                    idDefault: selectedPoblacion,
                    identificador: 'id',
                    atributo: "nombre"
                })
            });

            $("#cbPoblacion").select2({
                placeholder: 'Seleccione la población',
                formatNoMatches: function () {
                    return '-Seleccione una provincia primero-';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                minimumResultsForSearch: 1,
                dropdownParent: $("#modalDomicilio"),
                formatSelection: function (item) {
                    return item.text;
                }
            });

            $("#cbSitio").select2({
                placeholder: 'Seleccione el sitio',
                formatNoMatches: function () {
                    return '<g:message code="default.no.elements" default="No hay elementos"/>';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                minimumResultsForSearch: 1,
                dropdownParent: $("#modalTelefono"),
                formatSelection: function (item) {
                    return item.text;
                }
            });

            $("#cbRelacion").select2({
                placeholder: 'Seleccione el tipo de relacion',
                formatNoMatches: function () {
                    return '<g:message code="default.no.elements" default="No hay elementos"/>';
                },
                formatSearching: function () {
                    return '<g:message code="default.searching" default="Buscando..."/>';
                },
                dropdownParent: $("#modalPersonaRelacionada"),
                minimumResultsForSearch: 1,
                formatSelection: function (item) {
                    return item.text;
                }
            });

            $("#btnDomicilioEstadoConfirmado").on("click", function () {
                $("#domicilioEstado").val("Confirmado");
            });

            $("#btnDomicilioEstadoErroneo").on("click", function () {
                $("#domicilioEstado").val("Erróneo");
            });

            function guardarCambiosDomicilio() {
                var data = {
                    id: domicilioId,
                    calle: $("#domicilioCalle").val(),
                    numero: $("#domicilioNumero").val(),
                    bloque: $("#domicilioBloque").val(),
                    portal: $("#domicilioPortal").val(),
                    escalera: $("#domicilioEscalera").val(),
                    piso: $("#domicilioPiso").val(),
                    puerta: $("#domicilioPuerta").val(),
                    codigoPostal: $("#domicilioCodigoPostal").val(),
                    provincia: {
                        id: $("#cbProvincia").val(),
                        nombre: null
                    },
                    poblacion: {
                        id: $("#cbPoblacion").val(),
                        nombre: null
                    },
                    estado: {
                        nombre: null
                    }
                };
                if ($("#domicilioEstado").val() === "Confirmado") {
                    data.estado.nombre = 'Confirmado'
                } else if ($("#domicilioEstado").val() === "Erróneo") {
                    data.estado.nombre = 'Erróneo'
                } else if ($("#domicilioEstado").val() === 'Sin Confirmar') {
                    data.estado.nombre = 'Sin Confirmar'
                }
                data.provincia.nombre = $("#cbProvincia option[value='" + data.provincia.id + "']").text()
                data.poblacion.nombre = $("#cbPoblacion option[value='" + data.poblacion.id + "']").text();
                var domiciliosActualizados = tablaDomicilio.rows().data().toArray();
                var indiceDomicilioModificado = domiciliosActualizados.findIndex(function (dom) {
                    return dom.id === domicilioId;
                });
                if (indiceDomicilioModificado !== -1) {
                    domiciliosActualizados.splice(indiceDomicilioModificado, 1);
                    domiciliosActualizados.push(data);
                    tablaDomicilio.clear().rows.add(domiciliosActualizados).draw();
                    guardarDatos();
                } else {
                    console.error("No se encontró el domicilio modificado en la nueva data.");
                }
            }
            $("#btnGuardarCambiosDomicilio").on("click", function () {
                guardarCambiosDomicilio();
            });

            $("#btnConfirmarRelacion").on("click", function () {
                $("#relacionEstado").val("Confirmado");
            });

            $("#btnRelacionErronea").on("click", function () {
                $("#relacionEstado").val("Erróneo");
            });

            function guardarCambiosTelefono() {
                var data = {
                    id: telefonoId,
                    numero: $("#telefonoNumero").val(),
                    origen: {
                        id: $("#cbSitio").val(),
                        nombre: null
                    },
                    estado: {
                        nombre: null
                    },
                };
                if ($("#telefonoEstado").val() === "Confirmado") {
                    data.estado.nombre = 'Confirmado'
                } else if ($("#telefonoEstado").val() === "Erróneo") {
                    data.estado.nombre = 'Erróneo'
                } else if ($("#telefonoEstado").val() === 'Sin Confirmar') {
                    data.estado.nombre = 'Sin Confirmar'
                }

                data.origen.nombre = $("#cbSitio option[value='" + data.origen.id + "']").text();
                var telefonosActualizados = tablaTelefono.rows().data().toArray();
                var indiceTelefonoModificado = telefonosActualizados.findIndex(function (tel) {
                    return tel.id === telefonoId;
                });
                if (indiceTelefonoModificado !== -1) {
                    telefonosActualizados.splice(indiceTelefonoModificado, 1);
                    telefonosActualizados.push(data);
                    tablaTelefono.clear().rows.add(telefonosActualizados).draw();
                    guardarDatos();
                } else {
                    console.error("No se encontró el teléfono modificado en la nueva data.");
                }
            }

            $("#btnGuardarCambiosTelefono").on("click", function () {
                guardarCambiosTelefono();
            });

            $("#btnConfirmarTelefono").on("click", function () {
                $("#telefonoEstado").val("Confirmado");
            });

            $("#btnErroneoTelefono").on("click", function () {
                $("#telefonoEstado").val("Erróneo");
            });

            function guardarCambiosPersonaRelacionada() {
                var data = {
                    id: personaRelacionadaId,
                    
                    relacionid: $("#cbRelacion").val(),
                    nombrerelacion: nombrerelacion,
                    estado: estado,
                    relacionadas: personaRelacionada,
                    primernombre: primernombre,
                    segundonombre: segundonombre,
                    apellidopadre: apellidopadre,
                    apellidomadre: apellidomadre,
                    documento: documento
                };
                if ($("#relacionEstado").val() === "Confirmado") {
                    data.estado = 'Confirmado'
                } else if ($("#relacionEstado").val() === "Erróneo") {
                    data.estado = 'Erróneo'
                } else if ($("#relacionEstado").val() === 'Sin Confirmar') {
                    data.estado = 'Sin Confirmar'
                }
                data.nombrerelacion = $("#cbRelacion option[value='" + data.relacionid + "']").text();
                var relacionesActualizadas = tablaPersonasRelacionadas.rows().data().toArray();
                var indiceRelacionModificada = relacionesActualizadas.findIndex(function (per) {
                    return per.id === personaRelacionadaId;
                });
                if (indiceRelacionModificada !== -1) {
                    relacionesActualizadas.splice(indiceRelacionModificada, 1);
                    relacionesActualizadas.push(data);
                    tablaPersonasRelacionadas.clear().rows.add(relacionesActualizadas).draw();
                    guardarDatos();
                } else {
                    console.error("No se encontró la relacion modificada en la nueva data.");
                }
            }
            $("#btnGuardarCambiosPersonaRelacionada").on("click", function () {
                guardarCambiosPersonaRelacionada();
            });

            function borrarPersona(id) {
                Swal.fire({
                    title: '¿Estás seguro?',
                    text: "Se borrará la persona",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: 'Borrar',
                    cancelButtonText: 'Cancelar',
                    cancelButtonColor: '#f2f2f2',
                    confirmButtonColor: '#f29705',
                    reverseButtons: true,
                    closeOnConfirm: true,
                    closeOnCancel: true,
                    customClass: {
                        confirmButton: 'btn btn-primary',
                        cancelButton: 'btn btn-volver'
                    }
                }).then((result) => {
                    if (result.isConfirmed)
                        window.location.href = "${createLink(controller:'persona', action:'delete')}" + '/' + id;
                });
            }

            llenarCombo({
                    comboId: "cbPoblacionNacimiento",
                    ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetPoblaciones')}/" + '${personaPoblacionNacimiento?.provincia?.id}',
                    idDefault: '${personaPoblacionNacimiento?.id}',
                    identificador: 'id',
                    atributo: "nombre"
                })
        </script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>

</html>