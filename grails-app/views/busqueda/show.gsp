<!DOCTYPE html>
<html lang="en">

<head>
    <meta name="layout" content="main" />
</head>

<body>
     <div class="main-body">
        <div class="page-wrapper" style="height:4em;">
            <div class="page-header card">
                <div class="row align-items-end">
                    <div class="page-header-title col-sm-9">
                        <h4>Información acerca de la búsqueda</h4>
                    </div>
                </div>
            </div>
        </div>
        <div class="page-body m-3"> 
            <div class="card" style="width:100%; border-radius: 0;">
                    <div class="card-body">
                        <div class="form-group row" style="margin: 0; padding: 0;">
                            <div class="col-sm-12" id="inputs">
                                <div class="row" style="height: 25px;">
                                    <label class="col-sm-2 text-left" style="font-weight: 600; padding: 0;">Responsable:</label>
                                    <label id="responsable" name="responsable" class="col-sm-4 text-left">${busqueda?.responsable.username}</label>
                                    <label class="col-sm-2 text-left" style="font-weight: 600; padding-left:15px;">Inputs:</label>
                                    <label id="Input" name="Input" class="col-sm-4 text-left">${busqueda?.inputs ? busqueda.inputs.join(', ') : ''}</label>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="form-group row" style="margin: 0; padding: 0;">
                            <div class="col-sm-12" id="inputs">
                                <div class="row" style="height: 25px;">
                                    <label class="col-sm-2 text-left" style="font-weight: 600; padding: 0;">Fecha y hora:</label>
                                    <label readonly id="fechaHora" name="fechaHora" class="text-left col-sm-4">${busqueda?.fechaHora ? busqueda.fechaHora.format('dd/MM/yyyy - HH:mm') : ''}</label>
                                    <label class="col-sm-2 text-left" style="font-weight: 600; padding-left:15px;">Sitios Visitados: </label>
                                    <label readonly id="sitiosVisitados" name="sitiosVisitados" class="text-left col-sm-4">${busqueda?.sitiosVisitados ?busqueda.sitiosVisitados.join(', ') : '-'}</label>
                                </div>
                            </div>
                        </div>      
                            </div>                                    
                        </div>
                    </div>  
            </div>
            <div class="page-body m-3">
            <div class="card" style="border-radius: 0;">
                <div class="card-block">
                    <div class="dt-responsive table-responsive">
                        <table id="listPersonas" class="table table-striped table-bordered nowrap" style="cursor: pointer">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Segundo Nombre</th>
                                    <th>Apellido Padre</th>
                                    <th>Apellido Madre</th>
                                    <th>Documento</th>
                                    <th>Teléfonos</th>
                                    <th>Domicilio Principal</th>
                                    <th>Domicilio Anterior</th>
                                    <th>Año de Nacimiento</th>
                                    <th>Población de Nacimiento</th>
                                    <th>Oficina</th>
                                </tr>
                                <tr id="filterRow">
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>  
                                </tr>
                            </thead>    
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>         
        </div>
        </div>
    </div>  
      
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
var tabla;
var isSuperAdmin = '${userRoles}'.includes('ROLE_SUPER_ADMIN');
function onKeyUp(e, input, colIdx) {
    var api = tabla;
    e.stopPropagation();
    // Get the search value
    $(input).attr('title', $(input).val());
    var regexr = '({search})'; //$(input).parents('th').find('select').val();
    var cursorPosition = input.selectionStart;
    // Search the column for that value
    api.column(colIdx).search(
        input.value != ''
            ? regexr.replace('{search}', '(((' + input.value + ')))')
            : '',
        input.value != '',
        input.value == ''
    )
    api.draw();
    $(input)
        .focus()[0]
        .setSelectionRange(cursorPosition, cursorPosition);
}

jQuery(document).ready(function () {
        tabla = $('#listPersonas').DataTable({
            initComplete: function () {
                var api = this.api();
                api.columns().eq(0).each(function (colIdx) {
                    // Set the header cell to contain the input element
                    var cellFilter = $('#filterRow th').eq(
                        $(api.column(colIdx).header()).index()
                    );
                    var cell = $('th').eq(
                        $(api.column(colIdx).header()).index()
                    );
                    var title = $(cell).text();
                    if (title != "") {
                        $(cellFilter).html('<input type="text" style="width:100%;" placeholder="' + title + '" onkeyup="onKeyUp(event, this,' + colIdx + ')" />');
                    }
                });
            },
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).off('click').on('click', function () {
                    window.location.href = ('${createLink(controller:"persona", action:"show")}') + '/' + aData['id'];
                });
                    },
            //bAutoWidth: false,
            //bSortCellsTop: true,
            //BProcessing: true,
            "ordering": true,
            "searching": true,
            oLanguage: {
                sProcessing: "Buscando...",
                sSearch: "",
                sLengthMenu: "_MENU_",
                sZeroRecords: "No se encontraron registros",
                sInfo: "_START_ - _END_ de _TOTAL_",
                sInfoFiltered: "${message(code: 'default.datatable.infoFiltered', default: '(filtrado de MAX registros en total)')}",
                sInfoPostFix: "",
                sUrl: "",
                sInfoEmpty: "${message(code: 'default.datatable.infoEmpty', default: '0 de 0')}",
                oPaginate: {
                    "sFirst": "${message(code: 'default.datatable.paginate.first', default: 'Primero')}",
                    "sPrevious": "<",
                    "sNext": ">",
                    "sLast": "${message(code: 'default.datatable.paginate.last', default: '&Uacute;ltimo')}"
                }
            },
            aaSorting: [
                [0, 'desc']
            ],
            aoColumnDefs: [{
                "aTargets": [0],
                "mData": "primernombre"
            },{
                "aTargets": [1],
                "mData": "segundonombre",
            },{
                "aTargets": [2],
                "mData": "apellidopadre",
            },{
                "aTargets": [3],
                "mData": "apellidomadre",
            },{
                "aTargets": [4],
                "mData": "documento",
            },{
                "aTargets": [5],
                "mData": "telefonos",
            },{
                "aTargets": [6],
                "mData": "domicilioprincipal",
            },{
                "aTargets": [7],
                "mData": "domicilioanterior",
            },{
                "aTargets": [8],
                "mData": "anonacimiento",
            },{
                "aTargets": [9],
                "mData": "poblacionnacimiento",
            },{
                "aTargets": [10],
                "mData": "oficinanombre",
                "visible": isSuperAdmin,
            }],
            buttons: [],
            sPaginationType: 'simple',
            sDom: '<"row"<"col-4 row m-l-0"l><"offset-1 col-7"Bf>>t<"row"<"col-6"i><"col-6"p>>',
        });
        llenarDatosListPersona();
    });

    function llenarDatosListPersona() {
        tabla.clear().draw();
        $.ajax("${createLink(controller:'persona', action:'ajaxGetPersonasPorBusqueda')}/" + '${busqueda?.id}',{
            dataType: "json",
            data: {
            }
        }).done(function (data) {
            $("#loaderGrande").fadeOut("slow");
            tabla.rows.add(data)
            tabla.draw();
        });
    }
</script>
</body>
</html>