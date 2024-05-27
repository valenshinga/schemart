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
                                <h4>Sinónimos</h4>
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
                            <g:link controller="nombre" action="create" class="btn btn-success">Agregar</g:link>
                        </div>
                        <table id="listNombres" class="table table-striped table-bordered nowrap"
                            style="cursor: pointer">
                            <thead>
                                <tr>
                                    <th>Castellano</th>
                                    <th>Catalán</th>
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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script type="text/javascript">
        var tabla;

        jQuery(document).ready(function () {
            tabla = $('#listNombres').DataTable({
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
                    "mData": "castellano"
                }, {
                    "aTargets": [1],
                    "mData": "catalan",
                }],
                buttons: [],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4"l><"col-8"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).off('click').on('click', function () {
                        window.location.href = ('${createLink(controller:"nombre", action:"edit")}') + '/' + aData['id'];
                    });
                }
            });

            llenarDatosListNombre();
        });

        function llenarDatosListNombre() {
            tabla.clear().draw();
            $.ajax("${createLink(controller:'nombre', action:'ajaxGetNombres')}", {
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