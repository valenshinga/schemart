<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
    <g:set var="userRoles" value="${request.isUserInRole('ROLE_SUPER_ADMIN') ? 'ROLE_SUPER_ADMIN' : (request.isUserInRole('ROLE_ADMIN') ? 'ROLE_ADMIN' : '')}" />
</head>
<body>
    <div class="main-body ">
        <div class="page-wrapper" style="height:4em;">
            <div class="page-header card">
                <div class="row align-items-end">
                    <div class="col-lg-8">
                        <div class="page-header-title">
                            <div class="d-inline">
                                <h4>Personas</h4>
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
                        <table id="listPersonas" class="table table-striped table-bordered nowrap"
                            style="cursor: pointer">
                            <thead>
                                <tr>
                                    <th id="listTh1"></th>
                                    <th>Nombre</th>
                                    <th>Segundo Nombre</th>
                                    <th>Apellido Padre</th>
                                    <th>Apellido Madre</th>
                                    <th>Documento</th>
                                    <th>Teléfonos</th>
                                    <th>Año de Nacimiento</th>
                                    <th>Lugar de Nacimiento</th>
                                    <th>Oficina</th>
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
        </div>
    </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script type="text/javascript">
        var tabla;
        var isSuperAdmin = '${userRoles}'.includes('ROLE_SUPER_ADMIN');
        var isAdmin = '${userRoles}'.includes('ROLE_ADMIN');
        var isAdminOrSuperAdmin = false
        if (isSuperAdmin || isAdmin){
            isAdminOrSuperAdmin = true
        } 
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
            tabla = $('#listPersonas').DataTable({
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
                    [10, 'desc']
                ],
                aoColumnDefs: [{
                    "aTargets": [0],
                    "mData": "id",
                    "className": "text-right",
                    "mRender": function (data, type, full) {
                        var actionsHtml = '<a class="mr-3" style="color: #77a649" href="${createLink(controller:"persona", action:"ajaxDescargarExcelBusqueda")}/' + data + '"><i class="icofont icofont-file-excel"></i></a>';

                        if (isAdminOrSuperAdmin) {
                            actionsHtml += '&nbsp';
                            actionsHtml += ' <i class="icofont icofont-trash" onclick="borrarPersona(' + full['id'] + ')"></i>';
                        }
                        return actionsHtml;
                    },
                    "width": "1%",
                    "orderable": false,
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
                    "mData": "documento",
                }, {
                    "aTargets": [6],
                    "mData": "telefonos",
                }, {
                    "aTargets": [7],
                    "mData": "anonacimiento",
                }, {
                    "aTargets": [8],
                    "mData": "poblacionnacimiento",
                }, {
                    "aTargets": [9],
                    "mData": "oficinanombre",
                    "visible": isSuperAdmin,
                }, {
                    "aTargets": [10],
                    "mData": "id",
                    "visible": false,
                }],
                buttons: [],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).find('td:not(:first-child)').off('click').on('click', function (e) {
                        if (!$(e.target).is('.icofont-trash')) {
                            window.location.href = ('${createLink(controller:"persona", action:"show")}') + '/' + aData['id'];
                        }
                    });
                }
            });
            llenarDatosListPersona();
        });

        function llenarDatosListPersona() {
            tabla.clear().draw();
            $.ajax("${createLink(controller:'persona', action:'ajaxGetPersonas')}", {
                dataType: "json",
                data: {
                }
            }).done(function (data) {
                $("#loaderGrande").fadeOut("slow");
                tabla.rows.add(data)
                tabla.draw();
            });
        }

        function borrarPersona(id) {
            if (id == null){
                    swal("Error", "La persona seleccionada ya fue dada de baja previamente.", "error");
                return
            }
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

    </script>
</body>

</html>
