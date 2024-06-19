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
            </div>
            <div class="page-body m-3">
                <div class="card">
                    <div class="card-block">
                        <div class="row">
                            <div class="col-sm-5 col-md-7">
                                <input id="busqueda" type="text" class="form-control" value="" placeholder="Nombre a buscar...">
                            </div>
    
                            <div class="col-sm-6 col-md-1" style="text-align:right; margin-top: 6px;">
                                <label>Filtro:</label>
                            </div>
                            <div class="col-sm-6 col-md-3">
                                <select id="cbFiltro" name="filtro" class="form-control">
                                    <option value="todos" selected="true">Todos</option>
                                    <option value="alumno">Alumno</option>
                                    <option value="docente">Docente</option>
                                    <option value="empresa">Empresa</option>
                                </select>
                            </div>
    
                            <div class="col-sm-1">
                                <button type="button" id="btnBuscar" class="btn btn-primary waves-effect waves-light " onclick="buscar()">Buscar</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="divListCuentas" class="card" style="display:none;">
                    <div class="card-block">
                        <div class="dt-responsive table-responsive">
                            <table id="listCuentas" class="table table-striped table-bordered nowrap" style="cursor:pointer">
                                <thead>
                                    <tr>
                                        <th>CUIT</th>
                                        <th>Razón Social</th>
                                        <th>Locales</th>
                                        <th>Email</th>
                                        <th>Teléfono</th>
                                        <th>Etiqueta</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <script type="text/javascript">
        var tabla;
        jQuery(document).ready(function() {
            $("#busqueda").focus().select();
            $("#busqueda").on('keydown', function(event) {
                if(event.key === "Enter") {
                    event.preventDefault();
                    $("#btnBuscar").click()
                }
            });
            tabla = $('#listCuentas').DataTable({
                //bAutoWidth: false,
                //bSortCellsTop: true,
                //BProcessing: true,
                "ordering": true,
                "searching": true,
                oLanguage: {
                    sProcessing: "${message(code: 'default.datatable.processing', default: 'Buscando...')}",
                    sSearch: "",
                    sLengthMenu: "_MENU_",
                    sZeroRecords: "${message(code: 'zifras.cuenta.Cuenta.list.agregar', default: 'No hay resultados.')}</a>",
                    sInfo: "_START_ - _END_ de _TOTAL_",
                    sInfoFiltered: "${message(code: 'default.datatable.infoFiltered', default: '(filtrado de _MAX_ registros en total)')}",
                    sInfoPostFix: "",
                    sUrl: "",
                    sInfoEmpty: "${message(code: 'default.datatable.infoEmpty', default: '0 de 0')}",
                    oPaginate: {
                        "sFirst":	"${message(code: 'default.datatable.paginate.first', default: 'Primero')}",
                        "sPrevious":"<",
                        "sNext":	">",
                        "sLast":	"${message(code: 'default.datatable.paginate.last', default: '&Uacute;ltimo')}"
                    }
                },
                iDisplayLength: 100,
                //scrollX: true,
                aaSorting: [
                    [0, 'desc']
                ],
                aoColumnDefs: [{
                                "aTargets": [0],
                                "mData": "cuit",
                                'sClass': 'bold'
                            },{
                                "aTargets": [1],
                                "mData": "razonSocial"
                            },{
                                "aTargets": [2],
                                "mData": "locales",
                            },{
                                "aTargets": [3],
                                "mData": "email"
                            },{
                                "aTargets": [4],
                                "mData": "telefono"
                            },{
                                "aTargets": [5],
                                "mData": "etiqueta"
                            }],
                buttons: [{
                        extend: 'excelHtml5',
                        text: '<i class="fa fa-file-excel-o"></i>',
                        title: function () {
                                var nombre = "Cuentas";
                                return nombre;
                        }
                    },{
                        extend: 'pdfHtml5',
                        orientation: 'landscape',
                        text: '<i class="fa fa-file-pdf-o"></i>',
                        title: function () {
                            var nombre = "Cuentas";
                                return nombre;
                        }
                    },{
                        extend: 'copyHtml5',
                        text: '<i class="icofont icofont-copy-alt"></i>'
                    }],
                sPaginationType: 'simple',
                sDom: '<"row"<"col-4"l><"col-8"Bf>>t<"row"<"col-6"i><"col-6"p>>',
                fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                    if (aData['etiqueta']=='Rojo')
                        $(nRow).addClass("rowRojo")
                    else if (aData['etiqueta']=='Verde')
                        $(nRow).addClass("rowVerde")
                    else if (aData['etiqueta']=='Amarillo')
                        $(nRow).addClass("rowAmarillo")
                    // Row click
                    $(nRow).off('click').on('click', function() {
                        window.open('/cuenta/show/' + aData['id'], '_blank');
                    });
                }
            });
    
            $('#loaderGrande').fadeOut('slow', function() {
                $(this).hide();
            });
        });
        function buscar(){
            $('#divListCuentas').show()
            $('#loaderGrande').show()
            tabla.clear().draw();
            let campo
            if ($("#cbFiltro").val() == "todos")
                campo = "todos"
            else if ($("#cbFiltro").val() == "cuit")
                campo = "cuit"
            else if ($("#cbFiltro").val() == "email")
                campo = "email"
            else if ($("#cbFiltro").val() == "razon_social")
                campo = "razon_social"
            else if ($("#cbFiltro").val() == "telefono")
                campo = "telefono"
            else if ($("#cbFiltro").val() == "direccion")
                campo = "direccion"
            $.ajax("${createLink(action:'ajaxBuscarSQL')}", {
                dataType: "json",
                data: {
                    campo: campo,
                    filtro: $("#busqueda").val()
                }
            }).done(function(data) {
                if (data.length == 0) {
                    swal('Error', 'No se encontraron cuentas con esos datos.', 'error');
                } else if (data.length == 1){
                    window.location.href = '/cuenta/show/' + data[0].id;
                    return
                }
                tabla.rows.add(data)
                $('#loaderGrande').fadeOut('slow', function() {
                    $(this).hide();
                });
                $("#listCuentas").width("98%");
                tabla.draw();
            }).fail(function() {
                swal('Error', 'Hubo un error buscando la cuenta. Por favor actualice la página e intente nuevamente.', 'error');
                $('#loaderGrande').fadeOut('slow', function() {
                    $(this).hide();
                });
            });
        }
    </script>
</body>

</html>