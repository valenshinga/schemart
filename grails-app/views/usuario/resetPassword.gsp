<!DOCTYPE html>
<html lang="es">

<head>
	<title>
		<g:message code='tecnofind.title' default='Tecnofind' />
	</title>
	<!-- Meta -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="description" content="#">
	<meta name="keywords"
		content="Admin , Responsive, Landing, Bootstrap, App, Template, Mobile, iOS, Android, apple, creative app">
	<meta name="author" content="#">
	<!-- Favicon icon -->
	<asset:link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<!-- Google font-->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,800" rel="stylesheet">
	<asset:stylesheet src="application" />
	<asset:javascript src="application" />
</head>

<body class="bg-primary">
	<g:hiddenField id="flashMessage" name="flashMessage" value="${flash.message}" />
	<g:hiddenField id="flashError" name="flashError" value="${flash.error}" />
	<section class="login p-fixed d-flex text-center bg-primary common-img-bg">
		<!-- Container-fluid starts -->
		<div class="container">
			<div class="row">
				<div class="col-sm-12">
					<!-- Authentication card start -->
					<div class="login-card auth-body mr-auto ml-auto">
						<g:form class="md-float-material" controller="usuario" action="editPassword">
							<div class="text-center">
								<asset:image style="max-width: 60%;" src="auth/logo-dark.png" />
							</div>
							<div class="auth-box">
								<div style="padding-left: 0; padding-right: 0;">
									<g:hiddenField name="username" value="${username}" />

									<div class="row">
										<div class="col-md-12">
											<input class="form-control form-control-alto" name="password1"
												id="password1" type="password" placeholder="Contraseña nueva"
												value="${password1}">
										</div>
									</div>
									<div class="row" style="margin-top: 15px;">
										<div class="col-md-12">
											<input class="form-control form-control-alto" name="password2"
												id="password2" type="password" placeholder="Repetir contraseña"
												value="${password2}">
										</div>
									</div>
									<div class="row m-t-30" style="margin-top: 15px;">
										<div class="col-md-12">
											<button
												class="btn btn-primary btn-md btn-block waves-effect text-center m-b-20"
												type="submit">Enviar</button>
										</div>
									</div>
								</div>
							</div>
						</g:form>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>

</html>