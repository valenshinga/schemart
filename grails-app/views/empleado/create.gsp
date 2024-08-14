<!DOCTYPE html>
<html lang="es">

<head>
	<meta name="layout" content="main" />
</head>

<body>
	<div class="main-body">
		<div class="page-wrapper">
			<div class="page-header card" style="width:96%;">
				<div class="row align-items-end">
					<div class="page-header-title col-sm-9">
						<h4>Crear empleado</h4>
					</div>
				</div>
			</div>
			<div class="page-body">
				<div class="row d-flex justify-content-center">
					<div class="col-12">
						<div class="card">
							<div class="card-block">
								<h4 class="sub-title">Datos Empleado</h4>
								<g:uploadForm action="save">
									<g:render template="form"/>
									<div class="row">
										<div class="col-sm-10">
											<button type="submit" class="btn btn-primary m-b-0">Guardar</button>
											<g:link class="btn btn-inverse m-b-0" action="list">Volver</g:link>
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
</body>

</html>