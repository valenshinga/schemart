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
						<h4>Eventos</h4>
					</div>
				</div>
			</div>
			<div class="page-body">
				<div class="card">
					<div class="card-block ">
						<div class="row d-flex justify-content-end" style="margin-bottom:15px;">
							<div class="col-1 d-flex justify-content-end">
								<g:link action="create" class="btn btn-primary" title="Crear Evento">Crear</g:link>
							</div>
						</div>
						<div class="dt-responsive table-responsive">
							<table id="listAlumnos" class="table table-striped table-bordered nowrap" style="cursor: pointer;">
								<thead>
									<tr>
										<th id="listTh1"></th>
										<th>Nombre y Apellido</th>
										<th>Fecha</th>
										<th>Desde</th>
										<th>Hasta</th>
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
		var tablaAlumnos;
		$(document).ready(function () {
			tablaAlumnos = $('#listAlumnos').DataTable({
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
				aaSorting: [
					[1, 'desc']
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
								"mData": "fecha"
							},{
								"aTargets": [3],
								"mData": "desde",
							},{
								"aTargets": [4],
								"mData": "hasta"
							}],
				buttons: [{
						extend: 'excelHtml5',
						text: '<i class="fa fa-file-excel-o"></i>',
						title: function () {
								var nombre = "Eventos";
								return nombre;
						}
					},{
						extend: 'pdfHtml5',
						orientation: 'landscape',
						text: '<i class="fa fa-file-pdf-o"></i>',
						title: function () {
							var nombre = "Eventos";
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
						window.location.href = "${createLink(action: 'editEvento')}/" + aData.id
					});
				}
			});

			llenarDatoslistEventos()
	
			$('#loaderGeneral').fadeOut('slow', function() {
				$(this).hide();
			});
		});
		function llenarDatoslistEventos(){
			tablaAlumnos.clear().draw();
			$.ajax("${createLink(controller:'alumno', action: 'ajaxGetAlumnos')}", {
				dataType: "json",
				data: {
				}
			}).done(function(data) {
				tablaAlumnos.rows.add(data).draw()
			});
		}
	</script>
</body>

</html>