<!DOCTYPE html>
<html lang="es">

<head>
	<meta name="layout" content="main" />
</head>

<body>
	<div class="main-body">
		<div class="page-wrapper">
			<div class="page-header card" style="width:100%;">
				<div class="row align-items-end">
					<div class="page-header-title col-sm-10">
						<h4>Clases</h4>
					</div>
					<div class="col-sm-2">
						<select id="cbDocentes" class="form-control" name="docentes" style="border-color: #084c61; color: #084c61;"></select>
					</div>
				</div>
			</div>
			<div class="page-body">
				<div class="card">
					<div class="card-block">
						<g:link action="create" class="btn btn-primary" title="Crear Clase" style="margin-bottom: 15px;"><i class="icofont icofont-ui-add"></i> Agregar Clase</g:link>
						<div id='calendar'></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function () {
			// Inicializar FullCalendar
			function cargarEventos(docenteId) {
				$('#calendar').fullCalendar('removeEvents'); // Elimina eventos actuales
				$('#calendar').fullCalendar('addEventSource', function (start, end, timezone, callback) {
					if (docenteId){
						console.log(docenteId)
						$.ajax({
							url: "${createLink(controller: 'clase', action: 'ajaxListClasesByDocente')}",
							dataType: 'json',
							data: {
								docenteId: docenteId
							},
							success: function (data) {
								var events = [];
								$(data).each(function () {
									events.push({
										id: this.claseId,
										title: 'Clase ' + this.idiomaNombre + " " + this.nivel,
										start: this.fechaInicio,
										end: this.fechaFin,
										description: '',
										color: '#b0f489'
									});
								});
								callback(events);
							}
						});
					} else {
						console.log(docenteId)
						$.ajax({
							url: "${createLink(controller: 'clase', action: 'ajaxListClases')}",
							dataType: 'json',
							data: {
							},
							success: function (data) {
								var events = [];
								$(data).each(function () {
									events.push({
										id: this.claseId,
										title: 'Clase ' + this.idiomaNombre + " " + this.nivel,
										start: this.fechaInicio,
										end: this.fechaFin,
										description: '',
										color: '#b0f489'
									});
								});
								callback(events);
							}
						});
					}
				});
			}

			$('#calendar').fullCalendar({
				locale: 'es',
				header: {
					left: 'prev,next today',
					center: 'title',
					right: 'month,agendaWeek,agendaDay'
				},
				displayEventTime: true,
				displayEventEnd: true,
				events: function (start, end, timezone, callback) {
					cargarEventos($('#cbDocentes').val()); // Cargar eventos con el docente seleccionado al iniciar
				},
				eventClick: function (event) {
					window.location.href = "${createLink(controller: 'clase', action: 'edit')}/" + event.id;
				}
			});

			// Inicializar select2 y llenar el comboBox
			$("#cbDocentes").select2({
				placeholder: 'Seleccione un docente',
				formatNoMatches: function() {
					return 'No hay elementos';
				},
				formatSearching: function() {
					return 'Buscando...';
				},
				minimumResultsForSearch: 1,
				formatSelection: function(item) {
					return item.text;
				}
			});
			llenarCombo({
				comboId : "cbDocentes",
				ajaxLink : "${createLink(controller: 'empleado', action: 'ajaxGetDocentes')}",
				identificador: "id",
				idDefault: 0,
				atributo: "nombreApellido"
			});

			// Escuchar el cambio de selección en cbDocentes
			$('#cbDocentes').on('change', function() {
				let docenteId = $(this).val(); // Obtener el ID del docente seleccionado
				if (docenteId == 0){
					docenteId = null
				}
				cargarEventos(docenteId); // Recargar eventos con el docente seleccionado
			});
		});
	</script>
</body>

</html>
