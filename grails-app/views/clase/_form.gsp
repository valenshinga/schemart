<style>
	.swal-z-index{
		z-index: 5001;
	}
</style>
<g:hiddenField id="id" name="id" value="${command?.id}" />
<select id="alumnos" name="alumnos" multiple="multiple" style="display: none;"></select>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Idioma*</label>
	<div class="col-sm-10">
		<select id="cbIdiomas" class="form-control" name="idiomaId" style="border-color: #084c61; color: #084c61;" ></select>
	</div>
</div> 
<div class="form-group row" style="margin-bottom: 10px;">
	<label class="col-sm-2 col-form-label">Fecha</label>
	<div class="input-group date col-sm-10" style="margin-left: 0px;">
		<div class="input-group-addon" onclick="$('#fecha').datepicker('show')">
			<span class="icofont icofont-ui-calendar"></span>
		</div>
		<input data-format="m" type="text" id="fecha" name="fecha" class="form-control-primary form-control" value="${command?.fecha}" style="align-self: left;">
	</div>
</div>
<div class="form-group row" style="margin-bottom: 10px;">
	<label class="col-sm-2 col-form-label">Inicio</label>
	<div class="col-sm-10">
		<input id="inicio" name="inicio" placeholder="hh:mm" value="${command?.inicio}" class="form-control-primary form-control" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row" style="margin-bottom: 10px;">
	<label class="col-sm-2 col-form-label">Fin</label>
	<div class="col-sm-10">
		<input id="fin" name="fin" placeholder="hh:mm" value="${command?.fin}" class="form-control-primary form-control" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row" style="margin-bottom: 10px;">
	<label class="col-sm-2 col-form-label">Docente</label>
	<div class="col-sm-10">
		<select id="cbDocentes" class="form-control" value="${command?.docenteId}" name="docenteId" style="border-color: #084c61; color: #084c61;"></select>
	</div>
</div>
<div class="form-group row" style="margin-bottom: 10px;">
	<label class="col-sm-2 col-form-label">Alumnos</label>
	<div class="dt-responsive table-responsive col-sm-9">
		<table id="listAlumnos" class="table table-striped table-bordered nowrap" style="cursor: pointer;">
			<thead>
				<tr>
					<th></th>
					<th>Nombre y Apellido</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<div class="col-sm-1">
		<button id="btnAddAlumno" type="button" class="form-control btn btn-primary" onclick="showModalAlumno()">
			<i class="icofont icofont-ui-add"></i>
		</button>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Campos obligatorios*</label>
</div>

<div class="modal fade" id="modalAgregarAlumno" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg modal-dialog-center" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="tituloModal">Agregar Alumno</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body" style="padding: 0px">
				<g:hiddenField id="rowIndex" name="rowIndex"/>
				<g:hiddenField id="alumnoId" name="alumnoId"/>
				<div class="card-block">
					<div class="form-group row" style="margin-bottom: 10px;">
						<div class="col-sm-12">
							<select id="cbAlumnos" class="form-control" name="cbAlumnos" style="border-color: #084c61; color: #084c61;"></select>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer d-flex justify-content-start">
				<button id="btnCrear" type="button" class="btn btn-success "
				onclick="agregarAlumno()">Guardar</button>
				<button id="btnVolver" type="button" class="btn btn-volver" data-dismiss="modal">
					Volver</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function () {
		$("#cbIdiomas").select2({
			placeholder: 'Seleccione un idioma',
			formatNoMatches: function() {
				return 'No se encontraron idiomas';
			},
			formatSearching: function() {
				return 'Buscando...';
			},
			minimumResultsForSearch: 1,
			formatSelection: function(item) {
				return item.text;
			},
		});

		llenarCombo({
			comboId : "cbIdiomas",
			ajaxLink : "${createLink(controller: 'idioma', action: 'ajaxGetIdiomas')}",
			identificador: "id",
			idDefault: "${command?.idiomaId}",
			atributo: "idioma"
		});

		$("#cbAlumnos").select2({
			placeholder: 'Seleccione un Alumno',
			formatNoMatches: function() {
				return 'No hay alumnos disponibles';
			},
			language: {
				noResults: function() {
					return 'No hay alumnos disponibles';
				}
			},
			formatSearching: function() {
				return 'Buscando...';
			},
			minimumResultsForSearch: 1,
			formatSelection: function(item) {
				return item.text;
			},
		});

		$("#cbDocentes").select2({
			placeholder: 'Seleccione un Docente',
			formatNoMatches: function() {
				return 'No hay docentes disponibles';
			},
			language: {
				noResults: function() {
					return 'No hay docentes disponibles';
				}
			},
			formatSearching: function() {
				return 'Buscando...';
			},
			minimumResultsForSearch: 1,
			formatSelection: function(item) {
				return item.text;
			},
		});

		$("#fecha").datepicker({
			startView: "days",
			minViewMode: "days",
			maxViewMode: "years",
			format: "dd-mm-yyyy",
			language: "es",
			autoclose: true,
			orientation: "bottom auto", 
		}).on('changeDate', validarYActualizarDocentes);
		tablaAlumnos = $('#listAlumnos').DataTable({
			"ordering": false,
			"paging": false,  
			"info": false, 
			oLanguage: {
				sProcessing: "Buscando...",
				sSearch: "",
				sLengthMenu: "_MENU_",
				sZeroRecords: "No has agregado Alumnos a la clase. ¡Agrégalos!",
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
			//scrollX: true,
			aaSorting: [
				[1, 'desc']
			],
			aoColumnDefs: [{
							"aTargets": [0],
							"mData": "id",
							"visible": false
						},{
							"aTargets": [1],
							"mData": "nombreApellido",
						},{
							"aTargets": [2],
							"mData": "id",
							"sClass": "text-center",
							"mRender": function (data, type, full, meta) {
								return "<a href='#' onclick='eliminarAlumno(" + meta.row + ")'><i class='icofont icofont-delete-alt'></i></a>";
							}
						}],
			buttons: [],
			sPaginationType: 'simple',
			sDom: '<"row"<"col-4"l><"col-8"Bf>>t<"row"<"col-6"i><"col-6"p>>',
			fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				// Row click
				$('td:eq(0)', nRow).off('click').on('click', function() {
					showModalAlumno(aData.id);
				});
			}
		});

		llenarDatoslistAlumnos()

		$('#loaderGrande').fadeOut('slow', function() {
			$(this).hide();
		});

		$('#inicio').on('input', function() {
			const maxLength = 4;
			let inicio = $(this).val().replace(/:/g, ''); 

			if (inicio.length > maxLength) {
				inicio = inicio.slice(0, maxLength);
			}

			if (inicio.length > 2) {
				inicio = inicio.slice(0, 2) + ':' + inicio.slice(2);
			}

			$(this).val(inicio);
			validarYActualizarDocentes();
		});

		$('#fin').on('input', function() {
			const maxLength = 4;
			let fin = $(this).val().replace(/:/g, ''); // Remover cualquier ':' existente

			// Limitar el número máximo de caracteres
			if (fin.length > maxLength) {
				fin = fin.slice(0, maxLength);
			}

			// Formatear como "hh:mm" solo si se tiene más de 2 caracteres
			if (fin.length > 2) {
				fin = fin.slice(0, 2) + ':' + fin.slice(2);
			}

			$(this).val(fin);
			validarYActualizarDocentes();
		});

		if ("${command?.id}"){
			$('#fin').trigger('input')
		}else{
			$('#fin').trigger('input')
		}
	});

	function validarHora(hora) {
		const regexHora = /^([01]\d|2[0-3]):([0-5]\d)$/; // Formato de 24 horas (hh:mm)
		return regexHora.test(hora);
	}

	function validarYActualizarDocentes() {
		let fecha = $('#fecha').val();
		let inicio = $('#inicio').val();
		let fin = $('#fin').val();
		let idiomaId = $('#cbIdiomas').val() ? $('#cbIdiomas').val() : "${command?.idiomaId}"  

		if (fecha && validarHora(inicio) && validarHora(fin)) {
			llenarCombo({
				comboId : "cbDocentes",
				ajaxLink : "${createLink(controller: 'empleado', action: 'ajaxGetDocentesDisponibles')}",
				parametros: {
							fecha: fecha,
							inicio: inicio,
							fin: fin,
							idiomaId: idiomaId
						},
				identificador: "id",
				idDefault: null,
				atributo: "nombreCompleto"
			});
		}
	}

	function showModalAlumno(alumnoId) {
		$('#cbAlumnos').val('')
		$.ajax("${createLink(controller:'alumno', action: 'ajaxGetAlumnosDisponibles')}", {
			dataType: "json",
			data: {
				fecha: $('#fecha').val(),
				inicio: $('#inicio').val(),
				fin: $('#fin').val(),
				idiomaId: $('#cbIdiomas').val()
			}
		}).done(function(data) {
			llenarCombo({
				comboId : "cbAlumnos",
				data: data,
				atributo: "nombreCompleto",
				idDefault: alumnoId ? alumnoId : null,
				identificador: "id"
			});
		})
		$('#modalAgregarAlumno').modal('show');
	}

	function llenarDatoslistAlumnos(){
		tablaAlumnos.clear().draw();
		$.ajax("${createLink(controller:'clase', action: 'ajaxGetAlumnosByClase')}", {
			dataType: "json",
			data: {
				claseId: '${command?.id}'
			}
		}).done(function(data) {
			tablaAlumnos.rows.add(data).draw()
			$("#alumnos").val(JSON.stringify($('#listAlumnos').dataTable().fnGetData()))
		});
	}

	function agregarAlumno() {
		const nuevoId = $('#cbAlumnos').val();
		
		if (nuevoId && !registroYaExiste(nuevoId)) {
			$('#modalAgregarAlumno').modal('hide');
			tablaAlumnos.row.add({
				id: nuevoId,
				nombreApellido: $('#cbAlumnos option:selected').text()
			}).draw();

			// Obtener el select multiple y agregar el nuevo ID
			let longIdsSelect = $('#alumnos');
			
			// Agregar un nuevo option al select múltiple
			longIdsSelect.append(new Option(nuevoId, nuevoId, true, true)); // 
		} else if (registroYaExiste(nuevoId)) {
			Swal.fire({
				title: 'Error',
				text: "El alumno ya está registrado a la clase.",
				icon: "error",
				confirmButtonText: 'Aceptar',
				customClass: {
					confirmButton: 'btn btn-primary ',
					container: "swal-z-index"
				}
			});
		} else {
			Swal.fire({
				title: 'Error',
				text: "No has completado todos los datos.",
				icon: "error",
				confirmButtonText: 'Aceptar',
				customClass: {
					confirmButton: 'btn btn-primary ',
					container: "swal-z-index"
				}
			});
		}
	}

	function registroYaExiste(id) {
		var data = tablaAlumnos.rows().data().toArray();
		return data.some(alumno => alumno.id == id); 
	}

	function eliminarAlumno(index){
		tablaAlumnos.row(index).remove().draw()
		$("#lumnos").val(JSON.stringify($('#tablaAlumnos').dataTable().fnGetData()))
	}

	// function actualizarDisponibilidad(){
	// 	$('#modalAgregarDisponibilidad').modal('hide');
	// 	var index = $("#rowIndex").val()
	// 	tablaDisponibilidades.row(index).data({
	// 		"id": $('#disponibilidadId').val(),
	// 		"dia": $('#cbDias').val(),
	// 		"desde": $('#desde').val(),
	// 		"hasta": $('#hasta').val()
	// 	}).draw()
	// 	$("#disponibilidades").val(JSON.stringify($('#listDisponibilidades').dataTable().fnGetData()))
	// }
</script>
