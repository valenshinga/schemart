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
                                <h4>Historial de Búsquedas</h4>
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
                        <table id="listBusquedas" class="table table-striped table-bordered nowrap"
                            style="cursor: pointer">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th id="listTh1"></th>
                                    <th>Responsable</th>
                                    <th>Input</th>
                                    <th>Resultados</th>
                                    <th>Fecha y hora</th>
                                    <th>Sitios</th>
                                </tr>
                                <tr id="filterRow">
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
            tabla = $('#listBusquedas').DataTable({
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
                    "mData": "id",
                    "visible": false,
                }, {
                    "aTargets": [1],
                    "mData": "id",
                    "className": "text-right",
                    "mRender": function (data, type, full) {
                        return "<a style='color: #77a649' href='${createLink(controller:'busqueda', action:'ajaxDescargarExcelBusqueda')}/" + data + "'><i class='icofont icofont-file-excel'></i></a>";
                    },
                    "width": "1%",
                    "orderable": false,    
                }, {
                    "aTargets": [2],
                    "mData": "responsable"
                }, {
                    "aTargets": [3],
                    "mData": "inputs",
                }, {
                    "aTargets": [4],
                    "mData": "resultados",
                }, {
                    "aTargets": [5],
                    "mData": "fechahora",
                }, {
                    "aTargets": [6],
                    "mData": "sitios",
                }],
                buttons: [],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).off('click').on('click', function () {
                        window.location.href = ('${createLink(controller:"busqueda", action:"show")}') + '/' + aData['id'];
                    });
                
                },
                initComplete: function () {
                    var api = this.api();
                    api.columns().eq(0).each(function (colIdx) {
                        var header = api.column(colIdx).header();
                        if (header) {
                            var cellFilter = $('#filterRow th').eq(
                                $(header).index()
                            );
                            var cell = $('th').eq(
                                $(header).index()
                            );
                            var title = $(cell).text();
                            if (title != "") {
                                $(cellFilter).html('<input type="text" style="width:100%;" placeholder="' + title + '" onkeyup="onKeyUp(event, this,' + colIdx + ')" />');
                            }
                        }
                    });
                },

            });
            llenarDatosListBusqueda();
        });

        function llenarDatosListBusqueda() {
            tabla.clear().draw();
            $.ajax("${createLink(controller:'busqueda', action:'ajaxGetBusquedas')}", {
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