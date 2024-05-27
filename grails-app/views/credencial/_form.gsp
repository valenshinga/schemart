
<div style="padding-left: 0; padding-right: 0;">
    <div class="row">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="usuario" >Usuario:</label>
        </div>
        <div class="col-9">
            <input type="text" class="form-control" name="usuario" id="usuario" value="${credencialCommand?.usuario}">
        </div>
    </div>

    <div class="row" style="margin-top: 10px; margin-bottom: 10px;">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="password" >Password:</label>
        </div>
        <div class="col-9">
            <input type="text" class="form-control" name="password" id="password" value="${credencialCommand?.password}">
        </div>
    </div> 

    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
        <div class="row" style="margin-top: 10px; margin-bottom: 10px;">
            <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
                <label for="oficina" >Oficina:</label>
            </div>
            <div class="col-9">           
                <select id="cbOficina" class="form-control" name="oficinaId"></select>
            </div>  
        </div>
    </sec:ifAnyGranted>
    
    <div class="row" style="margin-top: 10px; margin-bottom: 10px;">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="sitio" >Sitio:</label>
        </div>
        <div class="col-9">           
            <select id="cbSitio" class="form-control" name="sitioId"></select>
        </div>  
    </div>
</div>

<script>
    $(document).ready(function () {

        $("#cbOficina").select2({
            placeholder: 'Seleccione la oficina',
            formatNoMatches: function() {
                return '<g:message code="default.no.elements" default="No hay elementos"/>';
            },
            formatSearching: function() {
                return '<g:message code="default.searching" default="Buscando..."/>';
            },
            minimumResultsForSearch: 1,
            formatSelection: function(item) {
                return item.text;
            }
        });

        llenarCombo({
            comboId : "cbOficina",
            ajaxLink : "${createLink(controller: 'oficina', action: 'ajaxGetOficinas')}",
            idDefault : '${credencialCommand?.oficinaId}',
            atributo: "nombre"
        });

        $("#cbSitio").select2({
            placeholder: 'Seleccione el sitio',
            formatNoMatches: function() {
                return '<g:message code="default.no.elements" default="No hay elementos"/>';
            },
            formatSearching: function() {
                return '<g:message code="default.searching" default="Buscando..."/>';
            },
            minimumResultsForSearch: 1,
            formatSelection: function(item) {
                return item.text;
            }
        });

        llenarCombo({
            comboId : "cbSitio",
            ajaxLink : "${createLink(controller: 'credencial', action: 'ajaxGetSitios')}",
            idDefault : '${credencialCommand?.sitioId}',
            atributo: "nombre"
        });
    });
</script>