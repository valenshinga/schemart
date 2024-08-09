<g:hiddenField id="id" name="id" value="${command?.id}" />
<g:hiddenField id="estadoId" name="estadoId" value="${command?.estadoId}" />
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
    <label class="col-sm-2 col-form-label">Campos obligatorios*</label>
</div>

<!-- <g:if test="${command?.id}">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Estado</label>
        <div class="col-sm-10">
            <select id="cbEstado" class="form-control" name="estadoId"></select>
        </div>
    </div> 
</g:if> -->
<script>
    $(document).ready(function () {
        // $("#cbEstado").select2({
        //     placeholder: 'Seleccione un estado',
        //     formatNoMatches: function() {
        //         return 'No hay elementos';
        //     },
        //     formatSearching: function() {
        //         return 'Buscando...';
        //     },
        //     minimumResultsForSearch: 1,
        //     formatSelection: function(item) {
        //         return item.text;
        //     }
        // });

        // llenarCombo({
        //     comboId : "cbEstado",
        //     ajaxLink : "${createLink(controller: 'oficina', action: 'ajaxGetOficinas')}",
        //     idDefault : '${userCommand?.oficinaId}',
        //     atributo: "nombre"
        // });
    });
</script>
