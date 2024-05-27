<div style="padding-left: 0; padding-right: 0;">
    <div class="row">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="username">Email:</label>
        </div>
        <div class="col-9">
            <input type="text" class="form-control" name="username" id="username" value="${userCommand?.username}">
        </div>
    </div>
    
    <div class="row" style="margin-top: 10px; margin-bottom: 10px;">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="nombre">Nombre:</label>
        </div>
        <div class="col-9">
            <input type="text" class="form-control" name="nombre" id="nombre" value="${userCommand?.nombre}">
        </div>
    </div>

    <div class="row" style="margin-top: 10px; margin-bottom: 10px;">
        <div class="col-3" style="padding: 0; padding-left: 15px;padding-top: 7px;">
            <label for="rol" >Rol:</label>
        </div>
        <div class="col-9">           
            <select id="cbRol" class="form-control" name="rolId"></select>
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
            idDefault : '${userCommand?.oficinaId}',
            atributo: "nombre"
        });

        $("#cbRol").select2({
            placeholder: 'Seleccione el rol',
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
            comboId : "cbRol",
            ajaxLink : "${createLink(controller: 'usuario', action: 'ajaxGetRoles')}",
            idDefault : '${userCommand?.rolId}',
            atributo: "authority"
        });
    });
</script>