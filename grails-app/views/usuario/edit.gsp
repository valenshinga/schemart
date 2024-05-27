<!DOCTYPE html>
<html lang="es">

<head>
    <meta name="layout" content="main" />
</head>

<body>
    <div style="display:block; margin-left: 50px; padding-top: 10px; padding-bottom: 10px">
        <h3>Editar Usuario</h3>
    </div>
    <div class="d-flex justify-content-start">
        <div class="card ml-5" style="width: 720px; border-radius: 0;">
            <div class="m-3">
                <g:form controller="usuario" action="update">
                    <g:hiddenField name="id" value="${userCommand.id}" />
                    <g:render template="form" />

                    <div style="padding-left: 0; padding-right: 0;">
                        <div class="row">
                            <div class="col-3">
                                <label for="accountLocked">Cuenta Bloqueada:</label>
                            </div>
                            <div class="col-9">
                                <input type="radio" id="accountLockedTrue" name="accountLocked" value="true" />
                                <label for="accountLockedTrue">Si</label>

                                <input type="radio" id="accountLockedFalse" name="accountLocked" value="false" />
                                <label for="accountLockedFalse">No</label>
                            </div>
                        </div>
                    </div>

                    <div class="center-content">
                        <div class="container-box d-flex justify-content-end">
                            <g:link controller="usuario" class="btn btn-volver" action="list"
                                style="margin-left: 5px; margin-right: 5px;">Volver</g:link>
                            <button class="btn btn-success" type="submit">Guardar</button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        jQuery(document).ready(function () {
            const accountLockedValue = '${userCommand.accountLocked}';

            if (accountLockedValue === 'true') {
                $('#accountLockedTrue').prop('checked', true);
            } else {
                $('#accountLockedFalse').prop('checked', true);
            }
        });
    </script>
</body>

</html>