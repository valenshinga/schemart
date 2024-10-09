<!DOCTYPE html>
<html lang="es">

<head>
	<meta name="layout" content="main" />
</head>

<body>
	<!-- 
	AGREGAR FILTRO POR DOCENTE
	-->
	<div class="main-body">
		<div class="page-wrapper">
			<div class="page-header card" style="width:96%;">
				<div class="row align-items-end">
					<div class="page-header-title col-sm-9">
						<h4>Clases</h4>
					</div>
				</div>
			</div>
			<div class="page-body">
				<div class="card">
					<div class="card-block">
						<g:link action="create" class="btn btn-primary"  title="Crear Clase" style="margin-bottom: 15px;"><i class="icofont icofont-ui-add"></i> Agregar Clase</g:link>
						<div id='calendar'></div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function () {
			// Inicializar FullCalendar
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
				},
				eventClick: function (event) {
					window.location.href = "${createLink(controller: 'clase', action: 'edit')}/" + event.id;
				}
			});

			// Manejar el formulario de creación de eventos
			$('#eventForm').on('submit', function (e) {
				e.preventDefault();
				
				// Obtener los valores del formulario
				var title = $('#eventTitle').val();
				var start = $('#eventStart').val();
				var end = $('#eventEnd').val();

				// Agregar evento al calendario
				$('#calendar').fullCalendar('renderEvent', {
					title: title,
					start: start,
					end: end,
					allDay: false
				}, true);

				// Cerrar el modal
				$('#eventModal').modal('hide');

				// Limpiar el formulario
				$('#eventForm')[0].reset();
			});


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
			
		});
		function showModalClase(data=null) {
			$("#fechaNacimiento").datepicker({
				startView: "days",
				minViewMode: "days",
				maxViewMode: "years",
				format: "dd/mm/yyyy",
				language: "es",
				autoclose: true
			})
			if (data){
				limpiarModal()
				$("#claseId").val(data.id)
				$('#tituloModal').html('Editar Clase');
				$('#btnCrear').hide();
				$('#btnActualizar').show();
				$('#btnBorrar').show();
				$('#desde').val(data.desde)
				$('#hasta').val(data.hasta)
				llenarCombo({
					comboId : "cbDocentes",
					atributo: "nombre",
					ajaxLink : "${createLink(controller: 'empleado', action: 'ajaxGetDocentes')}",
					identificador: "nombre"
				});
			}
			else{
				limpiarModal()
				$("#rowIndex").val('')
				$('#tituloModal').html('Nueva Clase');
				$('#btnActualizar').hide();
				$('#btnCrear').show();
				$('#btnBorrar').hide();
			}
			$('#disponibilidadId').val(data ? data?.id : '')
			$('#desde').val(data ? data.desde : '')
			$('#hasta').val(data ? data.hasta : '')
			$('#modalClase').modal('show');
		}

		function limpiarModal(){
			$('#disponibilidadId').val('')
			$('#desde').val('')
			$('#hasta').val('')
		}
	</script>
</body>

</html>
