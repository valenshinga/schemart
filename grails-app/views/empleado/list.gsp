<!DOCTYPE html>
<html lang="es">

<head>
	<meta name="layout" content="main" />
</head>

<body>
	<div class="main-body">
		<div class="page-wrapper">
			<div class="page-header card" style="width:96%;">
				<div class="row align-items-end">
					<div class="page-header-title col-sm-9">
						<h4>Empleados</h4>
					</div>
				</div>
			</div>
			<!-- <div class="input-group date">
				<div class="input-group-addon" onclick="$('#fechaNacimiento').datepicker('show')">
					<span class="icofont icofont-ui-calendar"></span>
				</div>
				<input data-format="m" type="text" id="fechaNacimiento" name="fechaNacimiento" class="form-control-primary form-control" value="${command?.fechaNacimiento}" style="align-self: left;">
			</div> -->
			<div class="page-body">
				<div class="card">
					<div class="card-block ">
						<div class="row d-flex justify-content-end" style="margin-bottom:15px;">
							<div class="col-1 d-flex justify-content-end">
								<g:link action="create" class="btn btn-primary"  title="Crear empleado">Crear</g:link>
							</div>
						</div>
						<div class="dt-responsive table-responsive">
							<table id="listEmpleados" class="table table-striped table-bordered nowrap" style="cursor: pointer;">
								<thead>
									<tr>
										<th id="listTh1"></th>
										<th>Nombre y Apellido</th>
										<th>DNI</th>
										<th>Cargo</th>
										<th>CUIT</th>
										<th>Email</th>
										<th>Telefono</th>
										<th>Fecha de nacimiento</th>
										<th>Estado</th>
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
		$(document).ready(function () {
		//     $("#fechaNacimiento").datepicker({
		//     startView: "days",
		//     minViewMode: "days",
		//     maxViewMode: "years",
		//     format: "dd/mm/yyyy",
		//     language: "es",
		//     autoclose: true
		// })
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
								"mData": "id",
								"visible": false,
							},{
								"aTargets": [1],
								"mData": "nombreApellido",
							},{
								"aTargets": [2],
								"mData": "dni"
							},{
								"aTargets": [3],
								"mData": "cargo",
							},{
								"aTargets": [4],
								"mData": "cuit",
							},{
								"aTargets": [5],
								"mData": "email"
							},{
								"aTargets": [6],
								"mData": "telefono"
							},{
								"aTargets": [7],
								"mData": "fechaNacimiento"
							},{
								"aTargets": [8],
								"mData": "estado"
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
						window.location.href = "${createLink(action: 'edit')}/" + aData.id
					});
				}
			});

			llenarDatoslistEmpleados()
	
			$('#loaderGeneral').fadeOut('slow', function() {
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