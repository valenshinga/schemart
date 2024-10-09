<g:hiddenField id="id" name="id" value="${command?.id}" />
<g:hiddenField id="estadoId" name="estadoId" value="${command?.estadoId}" />
<g:hiddenField id="disponibilidades" name="disponibilidades" value="" />
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Nombre*</label>
	<div class="col-sm-10">
		<input id="nombre" name="nombre" class="form-control-primary form-control" value="${command?.nombre}" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Apellido*</label>
	<div class="col-sm-10">
		<input id="apellido" name="apellido" class="form-control-primary form-control" value="${command?.apellido}" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">DNI*</label>
	<div class="col-sm-10">
		<input id="dni" name="dni" class="form-control-primary form-control" value="${command?.dni}" style="align-self: left;" type="number"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">CUIT</label>
	<div class="col-sm-10">
		<input id="cuit" name="cuit" class="form-control-primary form-control" value="${command?.cuit}" style="align-self: left;" type="number"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Calle</label>
	<div class="col-sm-10">
		<input id="calle" name="calle" class="form-control-primary form-control" value="${command?.calle}" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Número</label>
	<div class="col-sm-10">
		<input id="numero" name="numero" class="form-control-primary form-control" value="${command?.numero}" style="align-self: left;" type="number"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Correo electrónico</label>
	<div class="col-sm-10">
		<input id="email" name="email" class="form-control-primary form-control" value="${command?.email}" style="align-self: left;" type="email"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Teléfono</label>
	<div class="col-sm-10">
		<input id="telefono" name="telefono" class="form-control-primary form-control" value="${command?.telefono}" style="align-self: left;" type="number"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Nacimiento*</label>
	<div class="col-sm-10">
		<input id="fechaNacimiento" name="fechaNacimiento" placeholder="aaaa-mm-dd" class="form-control-primary form-control" value="${command?.fechaNacimiento}" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Cargo*</label>
	<div class="col-sm-10">
		<input id="cargo" name="cargo" class="form-control-primary form-control" value="${command?.cargo}" style="align-self: left;"></input>
	</div>
</div>
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Idiomas*</label>
	<div class="col-sm-10">
		<select id="cbIdiomas" class="form-control" name="idiomas" style="border-color: #084c61; color: #084c61;" multiple="multiple"></select>
	</div>
</div> 
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Disponibilidad*</label>
	<div class="col-sm-9">
		<div class="dt-responsive table-responsive">
			<table id="listDisponibilidades" class="table table-striped table-bordered nowrap compact" style="cursor: pointer; margin: 0px !important;">
				<thead>
					<tr>
						<th></th>
						<th>Dia</th>
						<th>Desde</th>
						<th>Hasta</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="col-sm-1">
		<button id="btnAddDisponibilidad" type="button" class="form-control btn btn-primary" onclick="showModalDisponibilidad()">
			<i class="icofont icofont-ui-add"></i>
		</button>
	</div>
</div> 
<div class="form-group row">
	<label class="col-sm-2 col-form-label">Campos obligatorios*</label>
</div>
<div class="modal fade" id="modalAgregarDisponibilidad" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-lg modal-dialog-center" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="tituloModal"></h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body" style="padding: 0px">
				<g:hiddenField id="rowIndex" name="rowIndex"/>
				<g:hiddenField id="disponibilidadId" name="disponibilidadId"/>
				<div class="card-block">
					<div class="form-group row" style="margin-bottom: 10px;">
						<label class="col-sm-2 col-form-label">Dia</label>
						<div class="col-sm-10">
							<select id="cbDias" class="form-control" name="dia" style="border-color: #084c61; color: #084c61;"></select>
						</div>
					</div>
					<div class="form-group row" style="margin-bottom: 10px;">
						<label class="col-sm-2 col-form-label">Desde</label>
						<div class="col-sm-10">
							<input id="desde" name="desde" placeholder="hh:mm" class="form-control-primary form-control" style="align-self: left;"></input>
						</div>
					</div>
					<div class="form-group row" style="margin-bottom: 10px;">
						<label class="col-sm-2 col-form-label">Hasta</label>
						<div class="col-sm-10">
							<input id="hasta" name="hasta" placeholder="hh:mm" class="form-control-primary form-control" style="align-self: left;"></input>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer d-flex justify-content-start">
				<button id="buttonBorrar" type="button" class="btn btn-primary "
				onclick="borrarDisponibilidad()">Borrar</button>
				<button id="buttonActualizar" type="button" class="btn btn-success "
				onclick="actualizarDisponibilidad()">Guardar</button>
				<button id="buttonCrear" type="button" class="btn btn-success "
				onclick="agregarDisponibilidad()">Guardar</button>
				<button id="buttonLocalVolver" type="button" class="btn btn-volver" data-dismiss="modal">
					Volver</button>
			</div>
		</div>
	</div>
</div>
<script>
		var dias = [
			{
				id: 0,
				nombre: 'Lunes'
			},
			{
				id: 1,
				nombre: 'Martes'
			},
			{
				id: 2,
				nombre: 'Miércoles'
			},
			{
				id: 3,
				nombre: 'Jueves'
			},
			{
				id: 4,
				nombre: 'Viernes'
			},
			{
				id: 5,
				nombre: 'Sábado'
			},
			{
				id: 6,
				nombre: 'Domingo'
			}
		];
	$(document).ready(function () {
		tablaDisponibilidades = $('#listDisponibilidades').DataTable({
			"ordering": false,
			"paging": false,  
			"info": false, 
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
							"mData": "dia",
						},{
							"aTargets": [2],
							"mData": "desde"
						},{
							"aTargets": [3],
							"mData": "hasta",
						}],
			buttons: [],
			sPaginationType: 'simple',
			sDom: '<"row"<"col-4"l><"col-8"Bf>>t<"row"<"col-6"i><"col-6"p>>',
			fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				// Row click
				$(nRow).off('click').on('click', function() {
					ultimaRow = nRow
					showModalDisponibilidad($('#listDisponibilidades').dataTable().fnGetPosition(nRow), aData)
				});
			}
		});

		llenarDatoslistDisponibilidades()

		$('#loaderGrande').fadeOut('slow', function() {
			$(this).hide();
		});
		$("#cbTipoCurso").select2({
			placeholder: 'Seleccione un tipo de curso',
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

		$("#cbIdiomas").select2({
			placeholder: 'Seleccione un idioma',
			formatNoMatches: function() {
				return 'No hay elementos';
			},
			formatSearching: function() {
				return 'Buscando...';
			},
			minimumResultsForSearch: 1,
			formatSelection: function(item) {
				return item.text;
			},
		});
		var lista = "${idiomas}"
		llenarCombo({
			comboId : "cbIdiomas",
			ajaxLink : "${createLink(controller: 'idioma', action: 'ajaxGetIdiomas')}",
			idDefault : lista,
			identificador: "id",
			atributo: "idioma"
		});

		$("#cbDias").select2({
			placeholder: 'Seleccione un día',
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
			comboId : "cbDias",
			data: dias,
			atributo: "nombre",
			idDefault: "nombre",
			identificador: "nombre"
		});

		$('#fechaNacimiento').on('input', function() {
			// Obtén el valor del input sin guiones
			let fechaNacimiento = $(this).val().replace(/-/g, '');

			// Formatea la fecha en 'aaaa-mm-dd'
			if (fechaNacimiento.length > 4 && fechaNacimiento.length <= 6) {
				fechaNacimiento = fechaNacimiento.slice(0, 4) + '-' + fechaNacimiento.slice(4);
			} else if (fechaNacimiento.length > 6) {
				fechaNacimiento = fechaNacimiento.slice(0, 4) + '-' + fechaNacimiento.slice(4, 6) + '-' + fechaNacimiento.slice(6, 8);
			}

			// Actualiza el valor del input con el formato correcto
			$(this).val(fechaNacimiento);
		});

		$('#desde').on('input', function() {
			const maxLength = 4;
			let desde = $(this).val().replace(/:/g, ''); 

			if (desde.length > maxLength) {
				desde = desde.slice(0, maxLength);
			}

			if (desde.length > 2) {
				desde = desde.slice(0, 2) + ':' + desde.slice(2);
			}

			$(this).val(desde);
		});

		$('#hasta').on('input', function() {
			const maxLength = 4;
			let hasta = $(this).val().replace(/:/g, ''); // Remover cualquier ':' existente

			// Limitar el número máximo de caracteres
			if (hasta.length > maxLength) {
				hasta = hasta.slice(0, maxLength);
			}

			// Formatear como "hh:mm" solo si se tiene más de 2 caracteres
			if (hasta.length > 2) {
				hasta = hasta.slice(0, 2) + ':' + hasta.slice(2);
			}

			$(this).val(hasta);
		});

		$('#dni').on('input', function() {
			const maxLength = 8;
			if ($(this).val().length > maxLength) {
				$(this).val($(this).val().slice(0, maxLength));
			}
		});
	});

	function limpiarModal(){
		$('#disponibilidadId').val('')
		$('#desde').val('')
		$('#hasta').val('')
	}

	function showModalDisponibilidad(index=null,data=null) {
		if (index!=null){
			limpiarModal()
			$("#rowIndex").val(index)
			$('#tituloModal').html('Editar disponibilidad horaria');
			$('#buttonCrear').hide();
			$('#buttonActualizar').show();
			$('#buttonBorrar').show();
			$('#disponibilidadId').val(data?.id)
			$('#desde').val(data.desde)
			$('#hasta').val(data.hasta)
			llenarCombo({
				comboId : "cbDias",
				data: dias,
				atributo: "nombre",
				idDefault: data.dia,
				identificador: "nombre"
			});
		}
		else{
			limpiarModal()
			$("#rowIndex").val('')
			$('#tituloModal').html('Nueva disponibilidad horaria');
			$('#buttonActualizar').hide();
			$('#buttonCrear').show();
			$('#buttonBorrar').hide();
		}
		$('#disponibilidadId').val(data ? data?.id : '')
		$('#desde').val(data ? data.desde : '')
		$('#hasta').val(data ? data.hasta : '')
		$('#modalAgregarDisponibilidad').modal('show');
	}

	function llenarDatoslistDisponibilidades(){
		tablaDisponibilidades.clear().draw();
		$.ajax("${createLink(controller:'disponibilidad', action: 'ajaxGetDisponibilidad')}", {
			dataType: "json",
			data: {
				esAlumno: false,
				id: '${command?.id}'
			}
		}).done(function(data) {
			tablaDisponibilidades.rows.add(data).draw()
			$("#disponibilidades").val(JSON.stringify($('#listDisponibilidades').dataTable().fnGetData()))
		});
	}

	function agregarDisponibilidad(){
		if ($('#cbDias').val() && $('#desde').val() && $('#hasta').val()){
			$('#modalAgregarDisponibilidad').modal('hide');
			tablaDisponibilidades.row.add({
				id: '',
				dia: $('#cbDias').val(),
				desde: $('#desde').val(),
				hasta: $('#hasta').val()
			}).draw()
			$("#disponibilidades").val(JSON.stringify($('#listDisponibilidades').dataTable().fnGetData()))
			limpiarModal()
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

	function borrarDisponibilidad(){
		$('#modalAgregarDisponibilidad').modal('hide');
		var index = $("#rowIndex").val()
		tablaDisponibilidades.row(index).remove().draw()
		$("#disponibilidades").val(JSON.stringify($('#listDisponibilidades').dataTable().fnGetData()))
	}

	function actualizarDisponibilidad(){
		$('#modalAgregarDisponibilidad').modal('hide');
		var index = $("#rowIndex").val()
		tablaDisponibilidades.row(index).data({
			"id": $('#disponibilidadId').val(),
			"dia": $('#cbDias').val(),
			"desde": $('#desde').val(),
			"hasta": $('#hasta').val()
		}).draw()
		$("#disponibilidades").val(JSON.stringify($('#listDisponibilidades').dataTable().fnGetData()))
	}
</script>
