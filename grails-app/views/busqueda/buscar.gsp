<!DOCTYPE html>
<html lang="es">

<head>
<meta name="layout" content="main" />

</head>

<body>
<div class="main-body">
    <div class="page-wrapper" style="height:4em">
        <div class="page-header card" style="width:96%;">
            <div class="row align-items-end">
                <div class="page-header-title col-sm-9">
                    <h4>Buscar</h4>
                </div>
            </div>
            <div class="row align-items-end">
                <div class="col-sm-9" style="padding-top: 20px;">      
                    <!-- MODAL CON INPUT DE ARCHIVO (FILER) -->
                    <div class="modal fade" id="modalImport" tabindex="-1" role="dialog">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title">Importar</h4>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body" style="padding: 0px">
                                    <div class="card-block" id="padre">
                                        <input type="file" name="files" id="archivos_importar" />
                                    </div>
                            </div>
                            <div class="modal-footer">
                                <div class="col-md-3">
                                    <button id="buttonPlantilla" type="button" style="color: #f39c12; font-size: 16px;" onClick="bajarPlantilla()" class="btn btn-link" data-dismiss="modal">
                                        <i class="icofont icofont-download"></i> Descargar plantilla de ejemplo
                                    </button>
                                </div>
                                <div class="col-md-5">
                                </div>
                                <div class="text-right col-md-4 mt-2">
                                    <button id="buttonVolver" type="button" class="btn btn-default waves-effect" data-dismiss="modal">
                                        Volver
                                    </button>
                                    <button id="buttonAceptar" onClick="cargarArchivo()" type="button" class="ml-2 btn btn-success" data-dismiss="modal">
                                        Importar
                                    </button>
                                </div>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
<div class="page-body m-3">
    <div class="card" style="width:96%; border-radius: 0;">
        <div class="card-body">
            <!-- Tabs -->
            <ul class="nav nav-tabs" id="tipoBusquedaTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active font-weight-bold" id="nombres-tab" data-toggle="tab" href="#nombres" role="tab" aria-controls="nombres" aria-selected="true">Por Nombre</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link font-weight-bold" id="direccion-tab" data-toggle="tab" href="#direccion" role="tab" aria-controls="direccion" aria-selected="false">Por Dirección</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link font-weight-bold" id="tab-documento" data-toggle="tab" href="#documento-tab" role="tab" aria-controls="documento-tab" aria-selected="false">Por Documento</a>
                </li>
            </ul>
            <div class="tab-content" id="tipoBusquedaTabContent">
                <!-- Nombres Tab -->
                <div class="tab-pane fade show active" id="nombres" role="tabpanel" aria-labelledby="nombres-tab">
                    <div class="form-group row mt-3 " style="margin: 0; padding: 0;">
                        <label class="col-sm-2 text-left mt-2" style="padding: 0; padding-top: 7px;">Primer Nombre:</label>
                        <input id="primerNombre" name="primerNombre" style="height:21.09%" class="form-control col-sm-4 mt-2" value="${buscarCommand?.primerNombre}"></input>
                        <label class="col-sm-2 text-left mt-2" style="padding-left:15px; padding-top: 7px;">Segundo Nombre:</label>
                        <input id="segundoNombre" name="segundoNombre" style="height:21.09%" class="form-control col-sm-4 mt-2" value="${buscarCommand?.segundoNombre}"></input>
                    </div>
                    <div class="form-group row" style="margin: 0; padding: 0;">
                        <label class="col-sm-2 text-left mt-2" style="padding: 0; padding-top: 7px;">Apellido Paterno:</label>
                        <input id="apellidoPadre" style="height:21.09%" name="apellidoPadre" class="form-control col-sm-4 mt-2" value="${buscarCommand?.apellidoPadre}"></input>
                        <label class="col-sm-2 text-left mt-2" style="padding-left:15px; padding-top: 7px;">Apellido Materno:</label>
                        <input id="apellidoMadre" style="height:21.09%" name="apellidoMadre" class="form-control col-sm-4 mt-2" value="${buscarCommand?.apellidoMadre}"></input>
                    </div>
                </div>
                <div class="form-group row" style="margin: 0; padding: 0;">
                    <div class="col-sm-12" id="inputs">
                        <div class="row mt-3">                   
                            <label class="col-sm-2 text-left" style="padding-left: 0; padding-top: 7px;">Provincia:</label>                               
                            <div class="col-sm-4" style="padding: 0;">
                                <select name="domicilio.provinciaId" id="cbProvincia" class="form-control form-select form-select-sm"></select>
                            </div>
                                <label class="col-sm-2 text-left" style="padding-top: 7px;">Población:</label>
                            <div class="col-sm-4" style="padding: 0;">
                                <select name="domicilio.poblacionId" id="cbPoblacion" class="form-control form-select form-select-sm"></select>
                            </div>        
                        </div>
                    </div>
                </div>
                <!-- Dirección Tab -->
                <div class="tab-pane fade" id="direccion" role="tabpanel" aria-labelledby="direccion-tab">
                    <div class="form-group row" style="margin: 0; padding: 0;">
                        <div class="col-sm-12" id="inputs">
                            <div class="row" style="margin-top: 10px;">
                                <label class="col-sm-2 text-left" style="padding: 0; padding-top: 7px;">Calle:</label>
                                <input id="domicilio" name="domicilio.calle" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.calle}"></input>
                                <label class="col-sm-2 text-left" style="padding-left:15px;; padding-top: 7px;">Número:</label>
                                <input id="numero" name="domicilio.numero" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.numero}"></input>
                            </div>
                            <div class="row" style="margin-top: 10px;">
                                <label class="col-sm-2 text-left" style="padding: 0; padding-top: 7px;">Bloque:</label>
                                <input id="bloque" name="domicilio.bloque" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.bloque}"></input>
                                <label class="col-sm-2 text-left" style="padding-left:15px;; padding-top: 7px;">Portal:</label>
                                <input id="portal" name="domicilio.portal" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.portal}"></input>
                            </div>
                            <div class="row" style="margin-top: 10px;">
                                <label class="col-sm-2 text-left" style="padding: 0; padding-top: 7px;">Escalera:</label>
                                <input id="escalera" name="domicilio.escalera" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.escalera}"></input>
                                <label class="col-sm-2 text-left" style="padding-left:15px; padding-top: 7px;">Puerta:</label>
                                <input id="puerta" name="domicilio.puerta" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.puerta}"></input>
                            </div>
                            <div class="row" style="margin-top: 10px;">
                                <label class="col-sm-2 text-left" style="padding: 0; padding-top: 7px;">Piso:</label>
                                <input id="piso" name="domicilio.piso" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.piso}"></input>
                                <label class="col-sm-2 text-left" style="padding-left:15px;padding-top: 7px;">Código Postal:</label>
                                <input id="codigoPostal" name="domicilio.codigoPostal" class="form-control col-sm-4" value="${buscarCommand?.domicilio?.codigoPostal}"></input>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Documento Tab -->
                <div class="tab-pane fade" id="documento-tab" role="tabpanel" aria-labelledby="tab-documento">
                    <div class="form-group row" style="margin: 0; padding: 0;">
                        <div class="col-sm-12" id="inputs">
                            <div class="row">                   
                                <label class="col-sm-3 text-left mt-3" style="padding: 0; padding-top: 7px;">Documento:</label>
                                <input id="documento" name="documento" class="form-control mt-3 col-sm-9" value="${buscarCommand?.documento}"></input>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group row" style="margin: 0; padding: 0;">
                <div class="col-sm-12" id="inputs">
                    <div class="row">
                        <div class="col-sm-9 offset-sm-3 text-right" style="padding-right: 0px;">
                            <button onclick="nuevaBusqueda()" type="button" class="btn btn-success mt-3">Buscar</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

    
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
    inicializarFiler()
    var apellidoMadreInput = $("#apellidoMadre");
    var cbProvinciaSelect = $("#cbProvincia");
    let cbPoblacionSelect = $("#cbPoblacion");

    $(document.body).on("keydown", function(event) {
        if (event.key === "Tab" && document.activeElement === apellidoMadreInput[0]) {
            event.preventDefault();
            cbProvinciaSelect.focus();
            cbProvinciaSelect.select2("open");
        }
        if (event.key === "Tab" && document.activeElement === cbProvinciaSelect[0]) {
            event.preventDefault();
            cbPoblacionSelect.focus();
            cbPoblacionSelect.select2("open");
        }
    });

    $("#cbProvincia").select2({
            placeholder: 'Seleccione la provincia',
            formatNoMatches: function () {
                return '<g:message code="custom.no.matches.message" defau   lt="No hay elementos"/>';
            },
            formatSearching: function () {
                return '<g:message code="default.searching" default="Buscando..."/>';
            },
            minimumResultsForSearch: 1,
            formatSelection: function (item) {
                return item.text;
            }
        });

        llenarCombo({
            comboId: "cbProvincia",
            ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetProvincias')}",
            idDefault: 'id',
            identificador: 'id',
            atributo: "nombre"
        });

        $("#cbProvincia").change(function () {
            var selectedValue = $(this).val();
            cargarPoblaciones(selectedValue);
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
            formatSelection: function (item) {
                return item.text;
            }
        });
    })

    function isNumeric(value) {
        return /^\d+$/.test(value);
    }

    function validateForm() {
        const activeTab = $('.nav-tabs .active').attr('id');
        
        if (activeTab === 'nombres-tab') {
            const primerNombre = $("#primerNombre").val().trim();
            const apellidoPadre = $("#apellidoPadre").val().trim();
            if (primerNombre === "" || apellidoPadre === "") {
                Swal.fire({
                    title: 'Formulario Incompleto',
                    text: 'Debe ingresar al menos el primer nombre y el apellido paterno antes de realizar la búsqueda.',
                    icon: 'warning',
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    },
                });
                return false;
            }
        } else if (activeTab === 'direccion-tab') {
            const provincia = $("#cbProvincia").val();
            const poblacion = $("#cbPoblacion").val();
            const calle = $("input[name='domicilio.calle']").val().trim();
            const numero = $("input[name='domicilio.numero']").val().trim();
            if (!provincia || !poblacion || calle === "" || numero === "" || !isNumeric(numero)) {
                Swal.fire({
                    title: 'Formulario Incompleto',
                    text: 'Debe ingresar al menos la provincia, población, calle y número antes de realizar la búsqueda.',
                    icon: 'warning',
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    },
                });
                return false;
            }
        } else if (activeTab === 'tab-documento') {
            const documento = $("#documento").val().trim();
            if (documento === "") {
                Swal.fire({
                    title: 'Formulario Incompleto',
                    text: 'Debe ingresar el documento antes de realizar la búsqueda.',
                    icon: 'warning',
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    },
                });
                return false;
            }
        }
        return true;
    }


    function toggleAddressFieldsVisibilityAndWidth(show) {
        if (show) {
            $("#inputs").show();
            $(".card").css("width", "96%");
        } else {
            $("#inputs").hide(); 
            $(".card").css("width", "40%");
        }
    }

    $('#tipoBusquedaTab').on('shown.bs.tab', function (e) {
        var targetTab = $(e.target).attr("href");
        if (targetTab === '#documento-tab') {
            toggleAddressFieldsVisibilityAndWidth(false);
        } else {
            toggleAddressFieldsVisibilityAndWidth(true);
        }
        $("#cbProvincia").val('').trigger('change');
        $("#cbPoblacion").val('').trigger('change');       
        var currentTab = $(e.relatedTarget).attr('href');
        $(currentTab + ' input').val('');

        if (currentTab === '#direccion-tab') {
            toggleAddressFieldsVisibilityAndWidth(true);
        }

        var targetTab = $(e.target).attr("href");
        $(targetTab + ' input').val('');
    
    });

    

    function nuevaBusqueda() {
        if (validateForm()) {
        $.ajax("${createLink(action:'buscar')}", {
            dataType: "json",
            method: "POST",
            timeout: 600000 /*10 mins*/,
            error: function(jqXHR, textStatus, errorThrown) {
            },
            data: JSON.stringify({
                primerNombre: $("input[name='primerNombre']").val(),
                segundoNombre: $("input[name='segundoNombre']").val(),
                apellidoPadre: $("input[name='apellidoPadre']").val(),
                apellidoMadre: $("input[name='apellidoMadre']").val(),
                documento: $("input[name='documento']").val(),
                domicilio: {
                    provinciaId: $("#cbProvincia").val(),
                    poblacionId: $("#cbPoblacion").val(),
                    calle: $("input[name='domicilio.calle']").val().toUpperCase(),
                    numero: $("input[name='domicilio.numero']").val(),
                    bloque: $("input[name='domicilio.bloque']").val(),
                    portal: $("input[name='domicilio.portal']").val(),
                    escalera: $("input[name='domicilio.escalera']").val(),
                    piso: $("input[name='domicilio.piso']").val(),
                    puerta: $("input[name='domicilio.puerta']").val(),
                    codigoPostal: $("input[name='domicilio.codigoPostal']").val(),
                }
            }),
            contentType: "application/json",
        }).done(function(data) {
            if(data.invalido)
                Swal.fire({
                    title: 'Error',
                    text: data.mensaje,
                    icon: "error",
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    }
                });
            else
                Swal.fire({
                    title: 'Buscando...',
                    text: "Procesando tu búsqueda.\nTe notificaremos por email cuando esté lista.",
                    icon: "info",
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    }
                }).then((result) => {
                    $("#primerNombre").val('');
                    $("#segundoNombre").val('');
                    $("#apellidoPadre").val('');
                    $("#apellidoMadre").val('');
                    $("#documentoPersona").val('');
                    $("#cbProvincia").val('').trigger('change');
                    $("#cbPoblacion").val('').trigger('change');
                    $("input[name='domicilio.calle']").val('');
                    $("input[name='domicilio.numero']").val('');
                    $("input[name='domicilio.bloque']").val('');
                    $("input[name='domicilio.portal']").val('');
                    $("input[name='domicilio.escalera']").val('');
                    $("input[name='domicilio.piso']").val('');
                    $("input[name='domicilio.puerta']").val('');
                    $("input[name='domicilio.codigoPostal']").val('');
                });
        })};
    }

    function cargarPoblaciones(idProvincia) {
        $("#cbPoblacion").empty(); 
        llenarCombo({
            comboId: "cbPoblacion",
            ajaxLink: "${createLink(controller: 'domicilio', action: 'ajaxGetPoblaciones')}/" + idProvincia,
            idDefault: 'id',
            identificador: 'id',
            atributo: "nombre"
        });
    }
    
    function bajarPlantilla() {
        window.location.href = "${createLink(controller:'busqueda', action:'ajaxDescargarPlantillaExcel')}";
    }   

    function cargarArchivo() {
        const inputFile = document.getElementById("archivos_importar");
        const files = $("#archivos_importar").prop("jFiler").files_list;
        const file = files[0] ? files[0].file : null;
        if (file) {
            var objFormData = new FormData();
            objFormData.append('files', file);
            $.ajax({
                url: "${createLink(controller:'busqueda', action: 'busquedaConExcel')}",
                type: "POST",
                data: objFormData,
                processData: false,
                enctype: "multipart/form-data",
                contentType: false,
                beforeSend: function () {
                    Swal.fire({
                        title: "Aviso",
                        text: "Las búsquedas se realizarán en segundo plano.",
                        icon: "info",
                        showConfirmButton: true,
                        confirmButtonColor: '#f29705',
                        allowEscapeKey: false,
                        allowOutsideClick: false,
                        confirmButtonText: "Confirmar",
                        customClass: {
                            confirmButton: 'btn btn-primary',
                        },
                        didOpen: () => {
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            Swal.close()
                        }
                    });
                },
                success: function () {
                    $("#archivos_importar").prop("jFiler").reset();
                    $("#modalImport").modal("hide");
                },
                error: function () {
                    Swal.close();
                },
            });
        } else {
            Swal.fire({
                title: 'Error',
                text: 'Por favor, selecciona un archivo excel antes de importar.',
                icon: 'error',
                confirmButtonColor: '#f29705',
                customClass: {
                    confirmButton: 'btn btn-primary',
                },
            });
        }
    }

    function inicializarFiler() {
    
            $("#archivos_importar").filer(
            {
            limit: null,
            maxSize: null,
            extensions: null,
            changeInput:
            '<div class="jFiler-input-dragDrop"><div class="jFiler-input-inner"><div class="jFiler-input-icon"><i class="icon-jfi-cloud-up-o"></i></div><div class="jFiler-input-text"><h3>Arrastrá los archivos acá</h3> <span style="display:inline-block; margin: 15px 0">ó</span></div><a class="jFiler-input-choose-btn btn btn-primary waves-effect waves-light">Seleccionar archivos</a></div></div>',
            showThumbs: true,
            theme: "dragdropbox",
            templates: {
            box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
            item: '<li class="jFiler-item">\
                                        <div class="jFiler-item-container">\
                                            <div class="jFiler-item-inner">\
                                                <div class="jFiler-item-assets jFiler-row" style="margin-top:0px;">\
                                                    <ul class="list-inline pull-right">\
                                                        <li><span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span></li>\
                                                        <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
                                                    </ul>\
                                                </div>\
                                            </div>\
                                        </div>\
                                    </li>',
            itemAppend:
                '<li class="jFiler-item">\
                                            <div class="jFiler-item-container">\
                                                <div class="jFiler-item-inner">\
                                                    <div class="jFiler-item-thumb">\
                                                        <div class="jFiler-item-status"></div>\
                                                        <div class="jFiler-item-info">\
                                                            <span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
                                                            <span class="jFiler-item-others">{{fi-size2}}</span>\
                                                        </div>\
                                                        {{fi-image}}\
                                                    </div>\
                                                    <div class="jFiler-item-assets jFiler-row">\
                                                        <ul class="list-inline pull-left">\
                                                            <li><span class="jFiler-item-others">{{fi-icon}}</span></li>\
                                                        </ul>\
                                                        <ul class="list-inline pull-right">\
                                                            <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
                                                        </ul>\
                                                    </div>\
                                                </div>\
                                            </div>\
                                        </li>',
            itemAppendToEnd: false,
            removeConfirmation: true,
            },
            dragDrop: {
            dragEnter: null,
            dragLeave: null,
            drop: null,
            },
            addMore: false,
            clipBoardPaste: true,
            excludeName: null,
            beforeRender: null,
            afterRender: null,
            beforeShow: null,
            beforeSelect: null,
            onSelect: null,
            afterShow: null,
            onEmpty: null,
            options: null,
            captions: {
            button: "Elegir archivos",
            feedback: "Elegir archivos para subir",
            feedback2: "archivos elegidos",
            drop: "Arrastrá un archivo para subirlo",
            removeConfirmation:
                "¿Está seguro de que desea descartar este archivo?",
            errors: {
                filesLimit: "Pueden subirse hasta {{fi-limit}} archivos.",
                filesSize:
                "{{fi-name}} excede el tamaño máximo de {{fi-maxSize}} MB.",
                filesSizeAll:
                "Los archivos elegidos exceden el tamaño máximo de {{fi-maxSize}} MB.",
            },
            },
        });
        }
</script>
</body>
</html>