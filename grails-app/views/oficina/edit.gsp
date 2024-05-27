<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div style="display:block; margin-left: 50px; padding-top: 10px; padding-bottom: 10px">
        <h3>Editar Oficina</h3>
    </div>
    <div class="d-flex justify-content-start">
        <div class="card ml-5" style="width: 720px; border-radius: 0;">
            <div class="m-3">
                <g:form controller="oficina" action="update">
                    <g:hiddenField name="id" value="${oficinaCommand.id}" />
                    <g:render template="form" />
                    <div class="center-content">
                        <div class="container-box d-flex justify-content-end ">
                            <g:link controller="oficina" class="btn btn-volver " action="list">Volver</g:link>
                            <a onclick="borrarSwal()" id="${oficinaCommand.id}" class="btn btn-primary "
                                href="javascript:;"
                                style="color: aliceblue;margin-left: 5px;margin-right: 5px;">Borrar</a>
                            <button class="btn btn-success " type="submit">Guardar</button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>



    <script>
        function borrarSwal() {
            Swal.fire({
                title: '¿Estás seguro?',
                text: "Se borrará la oficina ${oficinaCommand.nombre} y todas las personas y busquedas asociadas a esta oficina.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: 'Borrar',
                cancelButtonText: 'Cancelar',
                cancelButtonColor: '#f2f2f2',
                confirmButtonColor: '#f29705',
                reverseButtons: true,
                customClass: {
                    confirmButton: 'btn btn-primary ',
                    cancelButton: 'btn btn-volver '
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "${createLink(action:'delete',)}/" + '${oficinaCommand.id}';
                }
            })
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>

</html>