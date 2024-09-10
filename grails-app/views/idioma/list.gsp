<!DOCTYPE html>
<html lang="es">

<head>
	<meta name="layout" content="main" />
</head>
<body>
	<style>
		.swal-z-index{
			z-index: 5001;
		}
	</style>
	<div class="main-body">
		<div class="page-wrapper">
			<div class="page-header card" style="width:96%;">
				<div class="row align-items-end">
					<div class="page-header-title col-sm-9">
						<h4>Idiomas</h4>
					</div>
				</div>
			</div>
			<div class="page-body">
				<div class="card">
					<div class="card-block ">
						<div class="row d-flex justify-content-end" style="margin-bottom:15px;">
							<div class="col-1 d-flex justify-content-end">
								<button type="button" onclick="showModalIdioma()" class="btn btn-primary" title="Crear Idioma">Crear</button>
							</div>
						</div>
						<div class="dt-responsive table-responsive">
							<table id="listIdiomas" class="table table-striped table-bordered nowrap" style="cursor: pointer;">
								<thead>
									<tr>
										<th id="listTh1"></th>
										<th>Nombre</th>
										<th>Nivel</th>
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

	<div class="modal fade" id="modalIdioma" tabindex="-1" aria-hidden="true" role="dialog">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="tituloModal"></h4>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body" style="padding: 0px">
					<g:hiddenField id="idiomaId" name="idiomaId"/>
					<div class="card-block">
						<div class="form-group row" style="margin-bottom: 10px;">
							<label class="col-sm-2 col-form-label">Nombre</label>
							<div class="col-sm-10">
								<input id="nombre" name="nombre" class="form-control-primary form-control" style="align-self: left;"></input>
							</div>
						</div>
						<div class="form-group row" style="margin-bottom: 10px;">
							<label class="col-sm-2 col-form-label">Nivel</label>
							<div class="col-sm-10">
								<input id="nivel" name="nivel" class="form-control-primary form-control" style="align-self: left;"></input>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer d-flex justify-content-start">
					<button id="btnBorrar" type="button" class="btn btn-primary "
					onclick="deleteIdioma()">Borrar</button>
					<button id="btnActualizar" type="button" class="btn btn-success "
					onclick="updateIdioma()">Guardar</button>
					<button id="btnCrear" type="button" class="btn btn-success "
					onclick="saveIdioma()">Guardar</button>
					<button id="btnVolver" type="button" class="btn btn-volver" data-dismiss="modal">
						Volver</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var tablaIdiomas;
		$(document).ready(function () {
			tablaIdiomas = $('#listIdiomas').DataTable({
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
					[0, 'desc'],
					[1, 'desc']
				],		
				aoColumnDefs: [{
								"aTargets": [0],
								"mData": "id",
								"visible": false,
							},{
								"aTargets": [1],
								"mData": "nombre",
							},{
								"aTargets": [2],
								"mData": "nivel"
							}],
				buttons: [{
						extend: 'excelHtml5',
						text: '<i class="fa fa-file-excel-o"></i>',
						title: function () {
								var nombre = "Idiomas";
								return nombre;
						}
					},{
						extend: 'pdfHtml5',
						orientation: 'landscape',
						text: '<i class="fa fa-file-pdf-o"></i>',
						title: function () {
							var nombre = "Idiomas";
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
						showModalIdioma(aData)
					});
				}
			});

			llenarDatoslistIdiomas()
	
			$('#loaderGrande').fadeOut('slow', function() {
				$(this).hide();
			});
		});

		function llenarDatoslistIdiomas(){
			tablaIdiomas.clear().draw();
			$.ajax("${createLink(controller:'idioma', action: 'ajaxGetIdiomas')}", {
				dataType: "json",
				data: {
				}
			}).done(function(data) {
				tablaIdiomas.rows.add(data).draw()
			});
		}

		function saveIdioma(){
			if ($('#nombre').val() && $('#nivel').val()){
				$.ajax("${createLink(controller:'idioma', action: 'ajaxSaveIdioma')}", {
					dataType: "json",
					data: {
						nombre: $('#nombre').val(),
						nivel: $('#nivel').val()
					}
				}).done(function(data) {
					if (data.error){
						Swal.fire({
							title: 'Error',
							text: data.mensaje,
							icon: "error",
							confirmButtonText: 'Aceptar',
							customClass: {
								confirmButton: 'btn btn-primary ',
								container: "swal-z-index"
							}
						})
					}else{
						Swal.fire({
							title: 'Éxito',
							text: data.mensaje,
							icon: "success",
							confirmButtonText: 'Aceptar',
							customClass: {
								confirmButton: 'btn btn-primary ',
								container: "swal-z-index"
							}
						}).then((result) => {
							if (result.isConfirmed) {
								$('#modalIdioma').modal('hide');
								llenarDatoslistIdiomas()
								limpiarModal()
							}
						});
					}
				});
			}else{
				Swal.fire({
					title: 'Error',
					text: "No has completado todos los datos.",
					icon: "error",
					confirmButtonText: 'Aceptar',
					customClass: {
						confirmButton: 'btn btn-primary ',
						container: "swal-z-index"
					}
				})
			}
		}

		function deleteIdioma(id){
			$.ajax("${createLink(controller:'idioma', action: 'ajaxDeleteIdioma')}", {
				dataType: "json",
				data: {
					id: $('#idiomaId').val()
				}
			}).done(function(data) {
				if (data.error){
					Swal.fire({
						title: 'Error',
						text: data.mensaje,
						icon: "error",
						confirmButtonText: 'Aceptar',
						customClass: {
							confirmButton: 'btn btn-primary ',
							container: "swal-z-index"
						}
					})
				}else{
					Swal.fire({
						title: 'Éxito',
						text: data.mensaje,
						icon: "success",
						confirmButtonText: 'Aceptar',
						customClass: {
							confirmButton: 'btn btn-primary ',
							container: "swal-z-index"
						}
					}).then((result) => {
						if (result.isConfirmed) {
							$('#modalIdioma').modal('hide');
							llenarDatoslistIdiomas()
							limpiarModal()
						}
					});
				}
			});
		}

		function updateIdioma(){
			if ($('#nombre').val() && $('#nivel').val()){
				$.ajax("${createLink(controller:'idioma', action: 'ajaxUpdateIdioma')}", {
					dataType: "json",
					data: {
						id: $('#idiomaId').val(),
						nombre: $('#nombre').val(),
						nivel: $('#nivel').val()
					}
				}).done(function(data) {
					if (data.error){
						Swal.fire({
							title: 'Error',
							text: data.mensaje,
							icon: "error",
							confirmButtonText: 'Aceptar',
							customClass: {
								confirmButton: 'btn btn-primary ',
								container: "swal-z-index"
							}
						})
					}else{
						Swal.fire({
							title: 'Éxito',
							text: data.mensaje,
							icon: "success",
							confirmButtonText: 'Aceptar',
							customClass: {
								confirmButton: 'btn btn-primary ',
								container: "swal-z-index"
							}
						}).then((result) => {
							if (result.isConfirmed) {
								llenarDatoslistIdiomas()
								$('#modalIdioma').modal('hide');
								limpiarModal()
							}
						});
					}
				});
			}else{
				Swal.fire({
					title: 'Error',
					text: "No has completado todos los datos.",
					icon: "error",
					confirmButtonText: 'Aceptar',
					customClass: {
						confirmButton: 'btn btn-primary ',
						container: "swal-z-index"
					}
				})
			}
		}

		function limpiarModal(){
			$('#idiomaId').val('')
			$('#nombre').val('')
			$('#nivel').val('')
		}

		function showModalIdioma(data) {
			limpiarModal()
			if (data){
				$('#idiomaId').val(data.id)
				$('#tituloModal').html('Editar Idioma');
				$('#btnCrear').hide();
				$('#btnActualizar').show();
				$('#btnBorrar').show();
				$('#nombre').val(data.nombre)
				$('#nivel').val(data.nivel)
			}else{
				$('#tituloModal').html('Nuevo Idioma');
				$('#btnActualizar').hide();
				$('#btnCrear').show();
				$('#btnBorrar').hide();
			}
			$('#modalIdioma').modal('show');
		}
	</script>
</body>

</html>