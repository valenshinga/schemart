<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div style="display:block; margin-left: 50px; padding-top: 10px;">
        <h3>Agregar Usuario</h3>
    </div>
    <div class="d-flex justify-content-start">
        <div class="card ml-5" style="width: 720px; border-radius: 0;">
            <div class="m-3">
                <g:form controller="usuario" action="save" useToken="true">
                    <g:render template="form" />
                    <div class="center-content">
                        <div class="container-box d-flex justify-content-end">
                            <g:link controller="usuario" class="btn btn-volver mr-2">Volver</g:link>
                            <button class="btn btn-success" type="submit">Guardar</button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</body>

</html>