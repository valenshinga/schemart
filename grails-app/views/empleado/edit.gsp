<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div class="main-body">
        <div class="page-wrapper" style="height:4em">
            <div class="page-header card" style="width:96%;">
                <div class="row align-items-end">
                    <div class="page-header-title col-sm-9">
                        <h4>Editar empleado</h4>
                    </div>
                </div>
            </div>
            <div class="page-body">
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-block">
                                <h4 class="sub-title">Datos Empleado</h4>
                                <g:uploadForm action="update">
                                    <g:render template="form"/>
                                    <div class="row">
                                        <div class="col-sm-10">
                                            <button type="submit" class="btn btn-primary m-b-0">Aceptar</button>
                                            <g:link class="btn btn-inverse m-b-0" action="list">Volver</g:link>
                                            <a onclick="borrarSwal()" id="${command.id}" class="btn btn-danger "
                                            href="javascript:;"
                                            style="color: aliceblue;margin-left: 5px;margin-right: 5px;">Eliminar</a>
                                        </div>
                                    </div>
                                </g:uploadForm>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        function borrarSwal() {
            Swal.fire({
                title: '¿Estás seguro?',
                text: "Se eliminará el empleado.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: 'Eliminar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true,
                iconColor: '#fd93a8',
                customClass: {
                    confirmButton: 'btn btn-primary ',
                    cancelButton: 'btn btn-volver '
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "${createLink(action:'delete',)}/" + '${command.id}';
                }
            })
        }
    </script>
</body>
</html>