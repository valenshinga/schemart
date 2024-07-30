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
                        <h4>Empleados</h4>
                    </div>
                </div>
            </div>
            <div class="page-body">
                <div class="card">
                    <div class="card-block ">
                        <div class="row d-flex justify-content-end" style="margin-bottom:15px;">
                            <div class="col-1 d-flex justify-content-end">
                                <g:link action="create" class="btn btn-primary"  title="Crear empleado">Crear</g:link>
                            </div>
                        </div>
                        <div class="dt-responsive table-responsive">
                            <table id="listEmpleados" class="table table-striped table-bordered nowrap">
                                <thead>
                                    <tr>
                                        <th id="listTh1" ></th>
                                        <th></th>
                                        <th>Fecha</th>
                                        <th>Cuit</th>
                                        <th>Proveedor</th>
                                        <th>Tipo</th>
                                        <th>Número</th>
                                        <th data-toggle="tooltip" title="Desestimar para Liquidación de IVA">Des.<br/>IVA</th>
                                        <th data-toggle="tooltip" title="Desestimar para Liquidación de IIBB">Des.<br/>IIBB</th>
                                        <th>Neto</th>
                                        <th>Iva</th>
                                        <th>Total</th>
                                        <th>Advertencia</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- Statistics and revenue End -->
                </div>
            </div>
        </div>
    </div>


    <script type="text/javascript">
        var tablaEmpleados;
        jQuery(document).ready(function() {
            tablaEmpleados = $('#listEmpleados').DataTable({
                "ordering": true,
                "searching": true,
                oLanguage: {
                    sProcessing: "Buscando...",
                    sSearch: "",
                    sLengthMenu: "_MENU_",
                    sZeroRecords: "No hay resultados.",
                    sInfo: "_START_ - _END_ de _TOTAL_",
                    sInfoFiltered: "filtrado de _MAX_ registros en total",
                    sInfoPostFix: "",
                    sUrl: "",
                    sInfoEmpty: "0 de 0",
                    oPaginate: {
                        "sFirst":	"Primero",
                        "sPrevious":"<",
                        "sNext":	">",
                        "sLast":	"Último"
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
                    // Row click
                    $(nRow).off('click').on('click', function() {
                    });
                }
            });

            llenarDatoslistEmpleados()
    
            $('#loaderGrande').fadeOut('slow', function() {
                $(this).hide();
            });
        });
        function llenarDatoslistEmpleados(){
		tablaEmpleados.clear().draw();
		$.ajax("${createLink(controller:'empleado', action: 'ajaxGetEmpleados')}", {
			dataType: "json",
			data: {
			}
		}).done(function(data) {
			tablaEmpleados.rows.add(data).draw()
		});
	}
    </script>
</body>

</html>