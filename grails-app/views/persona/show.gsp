<!DOCTYPE html>
<html lang="en">

<head>
    <meta name="layout" content="main" />
    <style>
input[type="checkbox"][disabled][checked] {
  filter: invert(100%) hue-rotate(18deg) brightness(1) sepia(45%) saturate(15000%) hue-rotate(330deg);
}
input[type="checkbox"][disabled] {
  filter: invert(100%) hue-rotate(18deg) brightness(1) sepia(45%) saturate(15000%) hue-rotate(330deg);
}
    </style>
</head>

<body>
    <div class="main-body">
        <div class="page-wrapper">
            <div class="page-header card">
                <div class="row align-items-end">
                    <div class="col-lg-9">
                        <div class="page-header-title">
                            <div class="d-inline">
                                <h4>Detalles de ${persona?.primerNombre} ${persona?.segundoNombre} ${persona?.apellidoPadre} ${persona?.apellidoMadre} </h4>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex justify-content-end col-lg-3" style="padding-right: 0;">
                        <div class="row pr-3">
                            <g:link controller="persona" class="btn btn-volver ml-2" action="list">Volver</g:link>
                            <g:link controller="persona" class="btn btn-primary ml-3" action="edit" params="[id: persona.id]">Editar</g:link>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page-body">
                <div class="row">
                    <div class="col-6">
                        <div class="card">
                            <div class="card-block user-detail-card">
                                <div style="overflow: hidden;">
                                    <h4 style="float:left;">Datos Personales</h4>
                                </div>
                                <div class="user-detail">
                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-4">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-id"></i>Documento</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-8 ">
                                            <h6 id="documento" class="m-b-30">${persona?.documento ?: '-'}</h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-4">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-ui-user"></i>Nombre
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-8">
                                            <h6 id="primerNombre" class="m-b-30">${persona?.primerNombre}</h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-4">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-ui-user"></i>Segundo
                                                Nombre</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-8">
                                            <h6 id="segundoNombre" class="m-b-30">${persona?.segundoNombre ?: '-'}</h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-4">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-users"></i>Apellido
                                                Padre</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-8">
                                            <h6 id="apellidoPadre" class="m-b-30">${persona?.apellidoPadre ?: '-'}</h6>
                                        </div>
                                    </div>

                                    <div class="row" style="border-bottom: none;">
                                        <div class="col-5 col-md-5 col-lg-4">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-users"></i>Apellido
                                                Madre</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-8">
                                            <h6 id="apellidoMadre" class="m-b-30">${persona?.apellidoMadre ?: '-'}</h6>
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
                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-people"></i>Tipo
                                                Sociedad</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <h6 id="tipoSociedad" class="m-b-30">${persona?.tipoSociedad ?: '-'}</h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30"><i
                                                    class="icofont icofont-businessman"></i>Autónomo</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                           <h6 id="autonomo" class="m-b-30">
                                            ${persona?.autonomo ? 'SI' : 'NO'}
                                            </h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-400 m-b-30"><i class="icofont icofont-calendar"></i>Año de
                                                Nacimiento</h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <h6 id="anoNacimiento" class="m-b-30">${persona?.anoNacimiento ?: '-'}</h6>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-5 col-md-5 col-lg-5">
                                            <h6 class="f-w-450 m-b-30"><i
                                                    class="icofont icofont-location-pin"></i>Población de Nacimiento
                                            </h6>
                                        </div>
                                        <div class="col-7 col-md-7 col-lg-7">
                                            <h6 id="apellidoPadre" class="m-b-30">${persona?.poblacionNacimiento?.nombre
                                                ?: '-'}</h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <g:if test="${telefonosList?.isEmpty() == false}">
                        <div class="col-6">
                            <div class="card">
                                <div class="card-block user-detail-card">
                                    <ul>
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Teléfonos</h4>
                                    </div>
                                        <g:each var="telefono" status="i" in="${telefonosList}">
                                            <div class="telefono-item" style="margin-bottom: 10px;">
                                                <div class="card-block" style="padding: 10px;">
                                                    <li>
                                                        <div style="color: #f26805; font-size:14px;">
                                                            <i class="icofont icofont-smart-phone"></i>
                                                            ${telefono?.numero} - ${telefono?.origen?.nombre} - ${telefono?.estado.nombre}
                                                            ${telefono.trabajo ? '- (Autónomo)' : ''}
                                                        </div>
                                                    </li>
                                                </div>
                                            </div>
                                        </g:each>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </g:if>
                    <g:else>
                        <div class="col-6">
                            <div class="card">
                                <div class="card-block user-detail-card">
                                    <div style="overflow: hidden;">
                                        <h5 style="float:left;">Teléfonos</h5>
                                    </div>
                                    <br>
                                    <div class="col-7 col-md-7 col-lg-7">
                                        <h6 id="telefonoEstado" class="m-b-30">No se encontraron teléfonos.</h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </g:else>
                    <div class="col-6">
                        <g:if test="${domicilioList?.isEmpty() == false}">
                            <div class="card">
                                <div class="card-block user-detail-card">
                                    <div class="row">
                                        <div class="col-12">
                                            <div style="overflow: hidden;">
                                                <h4 style="float:left;">Domicilios</h4>
                                            </div>
                                            <g:each var="domicilio" in="${domicilioList}">
                                                <a href="#" data-toggle="modal"
                                                    data-target="#modalDomicilio${domicilio.id}">
                                                    <div class="domicilio-item"
                                                        style="margin-bottom: 10px; cursor: pointer;">
                                                        <div class="card-block" style="color: #f26805; font-size:14px; padding: 10px;">
                                                            <li>
                                                                <i class="icofont icofont-home"></i> ${domicilio?.calle}
                                                                ${domicilio?.numero}, ${domicilio.poblacion?.nombre},
                                                                ${domicilio.provincia?.nombre}
                                                            </li>
                                                        </div>
                                                    </div>
                                                </a>
                                        
                                                <!-- Modal para cada domicilio -->
                                                <div class="modal fade" id="modalDomicilio${domicilio.id}" tabindex="-1"
                                                    role="dialog" aria-labelledby="modalDomicilioLabel"
                                                    aria-hidden="true">
                                                    <div class="modal-dialog modal-dialog-center" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="modalDomicilioLabel">
                                                                    Detalles del Domicilio </h5>
                                                                <h6 style="right: auto;">${domicilio?.estado?.nombre}
                                                                </h6>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="col-12 user-detail">
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-location-pin"></i>Provincia
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="provincia" class="m-b-30">
                                                                                ${domicilio?.provincia?.nombre}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-building-alt"></i>Población
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="poblacion" class="m-b-30">
                                                                                ${domicilio?.poblacion?.nombre ?:'-'}
                                                                            </h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-road"></i>Calle
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="calle" class="m-b-30">
                                                                                ${domicilio?.calle ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-tag"></i>Número
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="numero" class="m-b-30">
                                                                                ${domicilio?.numero != null ? domicilio.numero : '-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-home"></i>Bloque
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="bloque" class="m-b-30">
                                                                                ${domicilio?.bloque ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-home"></i>Portal
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="portal" class="m-b-30">
                                                                                ${domicilio?.portal ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-home"></i>Escalera
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="escalera" class="m-b-30">
                                                                                ${domicilio?.escalera ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-home"></i>Piso
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="piso" class="m-b-30">
                                                                                ${domicilio?.piso ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-home"></i>Puerta
                                                                            </h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="puerta" class="m-b-30">
                                                                                ${domicilio?.puerta ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-5 col-md-5 col-lg-5">
                                                                            <h6 class="f-w-400 m-b-30"><i
                                                                                    class="icofont icofont-location-arrow"></i>Código
                                                                                Postal</h6>
                                                                        </div>
                                                                        <div class="col-7 col-md-7 col-lg-7">
                                                                            <h6 id="codigoPostal" class="m-b-30">
                                                                                ${domicilio?.codigoPostal ?:'-'}</h6>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button id="buttonLocalVolver" type="button"
                                                                    class="btn btn-volver" data-dismiss="modal">
                                                                    Volver</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </g:each>
                                            <g:if test="${persona.domicilioAutonomo}">
                                                            <li class="ml-3" style="font-size:14px; color: #f26805; padding-top:5px; padding-bottom:5px;">
                                                                <i class="icofont icofont-home"></i>
                                                                ${persona.domicilioAutonomo} - (Autónomo)
                                                            </li>
                                            </g:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </g:if>
                        <g:else>
                            <div class="card">
                                <div class="card-block user-detail-card">
                                    <div style="overflow: hidden;">
                                        <h4 style="float:left;">Domicilios</h4>
                                    </div>
                                    <br>
                                    <div class="col-7 col-md-7 col-lg-7">
                                        <h6 id="domicilioEstado" class="m-b-30">No se encontraron domicilios.</h6>
                                    </div>
                                </div>
                            </div>
                        </g:else>
                    </div>
                </div>

                <div class="card">
                    <div class="card-block">
                        <div style="overflow: hidden;">
                            <h4 style="float:left;">Personas Relacionadas</h4>
                        </div>
                        <div class="dt-responsive table-responsive">
                            <table id="listPersonasRelacionadas" class="table table-striped table-bordered nowrap"
                                style="cursor: pointer">
                                <thead>
                                    <tr>
                                        <th>Tipo de Relación</th>
                                        <th>Primer Nombre</th>
                                        <th>Segundo Nombre</th>
                                        <th>Apellido Padre</th>
                                        <th>Apellido Madre</th>
                                        <th>Teléfono</th>
                                        <th>Domicilio Principal</th>
                                        <th>Domicilio Anterior</th>
                                        <th>Documento</th>
                                        <th>Año de Nacimiento</th>
                                    </tr>
                                    <tr id="filterRow">
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>       

                <style>
                    .domicilio-item {
                        cursor: pointer;
                        transition: background-color 0.3s ease;
                        padding: 5px;
                        border-radius: 5px;
                        margin-bottom: 5px;
                    }

                    .domicilio-item:hover {
                        background-color: #f1eadd;
                    }
                </style>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous">
        </script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script type="text/javascript">
        var tabla;
        var isSuperAdmin = '${userRoles}'.includes('ROLE_SUPER_ADMIN');
        function onKeyUp(e, input, colIdx) {
            var api = tabla;
            e.stopPropagation();
            // Get the search value
            $(input).attr('title', $(input).val());
            var regexr = '({search})'; //$(input).parents('th').find('select').val();
            var cursorPosition = input.selectionStart;
            // Search the column for that value
            api.column(colIdx).search(
                input.value != ''
                    ? regexr.replace('{search}', '(((' + input.value + ')))')
                    : '',
                input.value != '',
                input.value == ''
            )
            api.draw();
            $(input)
                .focus()[0]
                .setSelectionRange(cursorPosition, cursorPosition);
        }
        jQuery(document).ready(function () {
            tabla = $('#listPersonasRelacionadas').DataTable({
                initComplete: function () {
                    var api = this.api();
                    api.columns().eq(0).each(function (colIdx) {
                        // Set the header cell to contain the input element
                        var cellFilter = $('#filterRow th').eq(
                            $(api.column(colIdx).header()).index()
                        );
                        var cell = $('th').eq(
                            $(api.column(colIdx).header()).index()
                        );
                        var title = $(cell).text();
                        if (title != "") {
                            $(cellFilter).html('<input type="text" style="width:100%;" placeholder="' + title + '" onkeyup="onKeyUp(event, this,' + colIdx + ')" />');
                        }
                    });
                },
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
                        "mData": "primernombre"
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
                        "mData": "telefonos",
                    },{
                        "aTargets": [6],
                        "mData": "domicilioprincipal",
                    },{
                        "aTargets": [7],
                        "mData": "domicilioanterior",
                    },{
                        "aTargets": [8],
                        "mData": "documento",
                    }, {
                        "aTargets": [9],
                        "mData": "anonacimiento",
                    }],
                buttons: [],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).off('click').on('click', function () {
                        window.location.href = ('${createLink(controller:"persona", action:"show")}') + '/' + aData['id'];
                    });
                }

            });
            llenarDatosListPersonaRelacionada('${persona.id}');
        });

        function llenarDatosListPersonaRelacionada(personaId) {
            tabla.clear().draw();
            $.ajax("${createLink(controller:'persona', action:'ajaxGetPersonasRelacionadas')}/" + personaId, {
                dataType: "json",
                data: {
                }
            }).done(function (data) {
                $("#loaderGrande").fadeOut("slow");
                tabla.rows.add(data)
                tabla.draw();
            });
        }
    </script>
</body>

</html>