<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div class="main-body ">
        <div class="page-wrapper" style="height:4em;">
            <div class="page-header card">
                <div class="row align-items-end">
                    <div class="col-lg-8">
                        <div class="page-header-title">
                            <div class="d-inline">
                                <h4>Tipos de relación</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="page-body m-3">
            <div class="card">
                <div class="card-block">
                    <div class="dt-responsive table-responsive">
                        <div style="float: right;margin-left: 1em;">
                            <button onclick="showModalTipoRelacion()" type="button"
                                class="btn btn-success">Agregar</button>
                        </div>
                        <table id="listTiposRelacion" class="table table-striped table-bordered nowrap"
                            style="cursor: pointer">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
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

    <div class="modal fade" id="modalTipoRelacion" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg modal-dialog-center" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="tituloModal"></h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" style="padding: 0px">
                    <div class="card-block">
                        <div class="form-group row" style="margin-bottom: 10px;">
                            <label class="col-sm-2 col-form-label" style="padding-right: 0;">Nombre:</label>
                            <div class="col-sm-10" style="padding-left: 0;">
                                <div class="input-group">
                                    <input id="modalNombre" class="form-control" name="nombre"
                                        placeholder="Nombre de la relación"></input>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="buttonLocalVolver" type="button" class="btn btn-volver" data-dismiss="modal">
                        Volver</button>
                    <button id="buttonBorrar" type="button" class="btn btn-primary "
                        onclick="borrarTipoRelacion(tipoRelacionId)">Borrar</button>
                    <button id="buttonActualizar" type="button" class="btn btn-success "
                        onclick="actualizarTipoRelacion()">Guardar</button>
                    <button id="buttonCrear" type="button" class="btn btn-success "
                        onclick="crearTipoRelacion()">Guardar</button>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script type="text/javascript">
        var tabla;
        var tipoRelacionId;
        var noEditables = ["Vecino", "Familiar", "Conviviente", "Antiguo Vecino", "Compañero de Trabajo"]
        jQuery(document).ready(function () {
            tabla = $('#listTiposRelacion').DataTable({
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
                    [0, 'desc']
                ],
                aoColumnDefs: [{
                    "aTargets": [0],
                    "mData": "nombre",
                },],
                buttons: [],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4"l><"col-8"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    // Row click
                    $(nRow).on('click', function () {
                        if (noEditables.includes(aData.nombre)) {
                            Swal.fire({
                                title: "No se puede editar este tipo de relación",
                                icon: "warning",
                                showConfirmButton: true,
                                confirmButtonText: "Ok",
                                confirmButtonColor: '#f29705',
                                customClass: {
                                    confirmButton: 'btn btn-primary',
                                }
                            });
                        } else {
                            showModalTipoRelacion(aData)
                        }
                    });
                }
            });

            llenarDatosListTipoRelacion();
        });

        function showModalTipoRelacion(data = null) {
            $('#tituloModal').html('Nuevo tipo de relación');
            $('#buttonActualizar').hide();
            $('#buttonCrear').show();
            $('#buttonBorrar').hide();
            if (data) {
                $('#tituloModal').html('Editar tipo de relación');
                $('#buttonCrear').hide();
                $('#buttonActualizar').show();
                $('#buttonBorrar').show();
            }
            $("#modalNombre").val(data ? data.nombre : '');
            tipoRelacionId = data ? data.id : null;
            $('#modalTipoRelacion').modal('show');
        }

        function llenarDatosListTipoRelacion() {
            tabla.clear().draw();
            $.ajax("${createLink(controller:'tipoRelacion', action:'ajaxGetTiposRelacion')}", {
                dataType: "json",
                data: {
                }
            }).done(function (data) {
                $("#loaderGrande").fadeOut("slow");
                tabla.rows.add(data)
                tabla.draw();
            });
        }

        function crearTipoRelacion() {
            if (!$("#modalNombre").val()) {
                Swal.fire({
                    title: "Faltan datos",
                    text: "Rellenar el campo.",
                    icon: "error",
                    showConfirmButton: true,
                    confirmButtonText: "Ok",
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    }
                });
            } else {
                $.ajax('${createLink(controller:"tipoRelacion", action:"save")}', {
                    dataType: "json",
                    data: {
                        nombre: $("#modalNombre").val()
                    }
                }).done(function (data) {
                    if (data.error) {
                        Swal.fire({
                            title: "Ocurrió un error al guardar",
                            icon: "error",
                            showConfirmButton: true,
                            confirmButtonText: "Ok",
                            confirmButtonColor: '#f29705',
                            customClass: {
                                confirmButton: 'btn btn-primary',
                            }
                        });
                    }
                    else {
                        $("#modalNombre").val("");
                        llenarDatosListTipoRelacion();
                        Swal.fire({
                            title: "Guardado exitoso",
                            icon: "success",
                            showConfirmButton: true,
                            confirmButtonText: "Ok",
                            confirmButtonColor: '#f29705',
                            customClass: {
                                confirmButton: 'btn btn-primary',
                            }
                        });
                    }
                });
                $('#modalTipoRelacion').modal('hide');
                return;
            }
        }

        function borrarTipoRelacion() {
            $.ajax('${createLink(controller:"tipoRelacion", action:"delete")}', {
                dataType: "json",
                data: {
                    id: tipoRelacionId
                }
            }).done(function (data) {
                if (data.error) {
                    Swal.fire({
                        title: "Ocurrió un error al borrar",
                        icon: "error",
                        showConfirmButton: true,
                        confirmButtonText: "Ok",
                        confirmButtonColor: '#f29705',
                        customClass: {
                            confirmButton: 'btn btn-primary',
                        }
                    });
                }
                else {
                    $("#modalNombre").val("");
                    llenarDatosListTipoRelacion();
                    Swal.fire({
                        title: "Borrado exitoso",
                        icon: "success",
                        showConfirmButton: true,
                        confirmButtonText: "Ok",
                        confirmButtonColor: '#f29705',
                        customClass: {
                            confirmButton: 'btn btn-primary',
                        }
                    });
                }
            });
            $('#modalTipoRelacion').modal('hide');
            return;
        }

        function actualizarTipoRelacion() {
            if (!$("#modalNombre").val()) {
                Swal.fire({
                    title: "Faltan datos",
                    text: "Rellenar el campo.",
                    icon: "error",
                    showConfirmButton: true,
                    confirmButtonText: "Ok",
                    confirmButtonColor: '#f29705',
                    customClass: {
                        confirmButton: 'btn btn-primary',
                    }
                });
            }
            else {
                $.ajax('${createLink(controller:"tipoRelacion", action:"update")}', {
                    dataType: "json",
                    data: {
                        nombre: $("#modalNombre").val(),
                        id: tipoRelacionId
                    }
                }).done(function (data) {
                    if (data.error) {
                        Swal.fire({
                            title: "Ocurrió un error al guardar",
                            icon: "error",
                            showConfirmButton: true,
                            confirmButtonText: "Ok",
                            confirmButtonColor: '#f29705',
                            customClass: {
                                confirmButton: 'btn btn-primary',
                            }
                        });
                    }
                    else {
                        $("#modalNombre").val("");
                        llenarDatosListTipoRelacion();
                        Swal.fire({
                            title: "Guardado exitoso",
                            icon: "success",
                            showConfirmButton: true,
                            confirmButtonText: "Ok",
                            confirmButtonColor: '#f29705',
                            customClass: {
                                confirmButton: 'btn btn-primary',
                            }
                        });
                    }
                });
                $('#modalTipoRelacion').modal('hide');
                return;
            }
        }
    </script>
</body>

</html>