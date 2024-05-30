<!DOCTYPE html>
<html lang="es">

<head>
    <!-- Google Tag Manager -->
    <script>(function (w, d, s, l, i) { w[l] = w[l] || []; w[l].push({ 'gtm.start': new Date().getTime(), event: 'gtm.js' }); var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l=' + l : ''; j.async = true; j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl; f.parentNode.insertBefore(j, f); })(window, document, 'script', 'dataLayer', 'GTM-NPFKM22');
    </script>
    <!-- End Google Tag Manager -->

    <title>
        <g:message code='schemart.title' default='schemart' />
    </title>
    <!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 10]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
      <![endif]-->
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
    <asset:stylesheet src="login" />
    <asset:javascript src="login" />
</head>

<body class="fix-menu">
    <section class="login p-fixed d-flex text-center bg-primary common-img-bg">
        <!-- Container-fluid starts -->
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <!-- Authentication card start -->
                    <div class="login-card auth-body mr-auto ml-auto">
                        <form class="md-float-material" action='${postUrl}' method='POST' id="loginForm"
                            name="loginForm" autocomplete='off'>
                            <div class="text-center">
                                <asset:image style="max-width: 100%;" src="auth/schemartLogo2.png" />
                            </div>
                            <div class="auth-box">
                                <div class="input-group">
                                    <input name="username" id="username" type="email"
                                        class="form-control form-control-alto" placeholder="Email">
                                    <span class="md-line"></span>
                                </div>
                                <div class="input-group" style="margin-top:1.25em!important;">
                                    <input name="password" id="password" type="password"
                                        class="form-control form-control-alto" placeholder="Contraseña">
                                    <span class="md-line"></span>
                                </div>
                                <g:if test='${loginError!=null}'>
                                    <div class="alert alert-danger background-danger">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                            <i class="icofont icofont-close-line-circled text-white"></i>
                                        </button>
                                        <strong>¡Datos incorrectos!</strong>
                                    </div>
                                </g:if>
                                <g:if test='${nuevoRegistro!=null}'>
                                    <div class="alert alert-success background-success">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                            <i class="icofont icofont-close-line-circled text-white"></i>
                                        </button>
                                        <strong>¡Verifique la cuenta en su mail!</strong>
                                    </div>
                                </g:if>
                                <div class="row m-t-30">
                                    <div class="col-md-12">
                                        <button type="button"
                                            class="btn btn-primary btn-md btn-block waves-effect text-center m-b-20"
                                            onclick="document.forms.loginForm.submit();">
                                            Ingresar
                                        </button>
                                    </div>
                                </div>
                                <div class="row text-center">
                                    <div class="col-12">

                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script>
        $(document).ready(function () {

            $("#username").on('keydown', function (event) {
                if (event.key === "Enter") {
                    event.preventDefault();
                    document.forms.loginForm.submit();
                }
            });


            $("#password").on('keydown', function (event) {
                if (event.key === "Enter") {
                    event.preventDefault();
                    document.forms.loginForm.submit();
                }
            });

            $('#username').focus();
        });

    </script>
</body>

</html>