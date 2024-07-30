<!DOCTYPE html>
<html lang="es">
<%@ page import="com.schemart.User" %>
  <%@ page import="com.schemart.ItemMenu" %>
    <%@ page import="com.schemart.Role" %>
      <g:set var="userLogged" value="${User.get(session.SPRING_SECURITY_CONTEXT?.authentication?.principal?.id)}" />

      <head>
        <title>Schemart</title>
        <!-- Meta -->
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="description" content="#" />
        <meta name="keywords"
          content="Admin , Responsive, Landing, Bootstrap, App, Template, Mobile, iOS, Android, apple, creative app" />
        <meta name="author" content="#" />
        <!-- Favicon icon -->
        <asset:link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
        <!-- Google font-->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,800" rel="stylesheet" />
        <asset:stylesheet src="application" />
        <asset:javascript src="application" />

        <g:layoutHead />
        <style>
          .toggle-button-cover {
            display: table-cell;
            position: relative;
            width: 200px;
            height: 140px;
            box-sizing: border-box;
          }

          .button-cover {
            height: 100px;
            margin: 20px;
            background-color: #fff;
            box-shadow: 0 10px 20px -8px #c5d6d6;
            border-radius: 4px;
          }

          .button-cover:before {
            counter-increment: button-counter;
            content: counter(button-counter);
            position: absolute;
            right: 0;
            bottom: 0;
            color: #d7e3e3;
            font-size: 12px;
            line-height: 1;
            padding: 5px;
          }

          .button-cover,
          .knobs,
          .layer {
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
          }

          .button {
            position: relative;
            top: 50%;
            width: 74px;
            height: 36px;
            margin: -20px auto 0 auto;
            overflow: hidden;
          }

          .button.r,
          .button.r .layer {
            border-radius: 100px;
          }

          .button.b2 {
            border-radius: 2px;
          }

          .checkbox {
            position: relative;
            width: 100%;
            height: 100%;
            padding: 0;
            margin: 0;
            opacity: 0;
            cursor: pointer;
            z-index: 3;
          }

          .knobs {
            z-index: 2;
          }

          .layer {
            width: 100%;
            background-color: #ebf7fc;
            transition: 0.3s ease all;
            z-index: 1;
          }

          /* Button 4 */
          #button-4 .knobs:before,
          #button-4 .knobs:after {
            position: absolute;
            top: 15%;
            left: 8px;
            width: 24px;
            height: 24px;
            color: #fff;
            font-size: 16px;
            font-weight: bold;
            text-align: center;
            line-height: 0.25;
            padding: 9px 4px;
            background-color: #084C61;
            border-radius: 50%;
            transition: 0.3s cubic-bezier(0.18, 0.89, 0.35, 1.15) all;
          }

          #button-4 .knobs:before {
            content: "I";
            font-family: "Open Sans";
          }

          #button-4 .knobs:after {
            content: "S";
          }

          #button-4 .knobs:after {
            top: -28px;
            right: 4px;
            left: auto;
            background-color: #0C7797;
          }

          #button-4 .checkbox:checked+.knobs:before {
            top: -28px;
          }

          #button-4 .checkbox:checked+.knobs:after {
            top: 15%;
          }

          #button-4 .checkbox:checked~.layer {
            background-color: #eaffa5;
          }

          .dataTables_wrapper .dataTables_filter::before {
            content: "\eded";
            font-family: "IcoFont";
            font-size: 18px;
          }
        </style>
      </head>

      <body>

        <g:hiddenField id="flashMessage" name="flashMessage" value="${flash.message}" />
        <g:hiddenField id="flashError" name="flashError" value="${flash.error}" />
        <!-- Pre-loader start -->
        <div class="theme-loader" id="loaderGeneral">
          <div class="ball-scale">
            <div class="contain">
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
              <div class="ring">
                <div class="frame"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- Validation js -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>

        <!-- Pre-loader end -->
        <div id="pcoded" class="pcoded">
          <div class="pcoded-overlay-box"></div>
          <div class="pcoded-container navbar-wrapper">
            <nav class="navbar header-navbar pcoded-header">
              <div class="navbar-wrapper">
                <div class="navbar-logo">
                  <a class="mobile-menu" id="mobile-collapse" href="#">
                    <i class="ti-menu"></i>
                  </a>
                  <a class="mobile-search morphsearch-search" href="#">
                    <i class="ti-search"></i>
                  </a>
                  <g:link controller="start" action="index">
                    <img class="img-fluid" src="${resource(dir: 'assets/guru/assets/images', file: 'schemartLogo.png')}"
                      alt="Theme-Logo" style="height: 50px" />
                  </g:link>
                </div>

                <div class="navbar-container container-fluid">
                  <ul class="nav-right">
                    <li>
                      <span style="padding: 5px 15px; border-radius: 15px; box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.2);">
                        ${userLogged?.nombre} 
                      </span>
                    </li>

                    <li>
                      <g:link controller="logout" action="index" class="ti-power-off"
                        style="color: #084C61; font-size: 1.5em"></g:link>
                    </li>
                  </ul>
                </div>
              </div>
            </nav>

            <div class="pcoded-main-container">
              <div class="pcoded-wrapper">
                <nav class="pcoded-navbar" id="menuPrincipal" style="padding-top: 20px">
                  <div class="sidebar_toggle">
                    <a href="#"><i class="icon-close icons"></i></a>
                  </div>
                  <div class="pcoded-inner-navbar main-menu">
                    <ul class="pcoded-item pcoded-left-item">
                      <g:each in="${ItemMenu.getNodos(userLogged)}" var="itemMenu" status="i">
                        <li id=""
                          class="${itemMenu.hijos ? 'pcoded-hasmenu' : 'pcoded'} ${(controllerName == itemMenu.controller && (!itemMenu.action || actionName == itemMenu.action))?'active pcoded-trigger':''}">
                          <a href='${itemMenu.hijos ? "#" : "/$itemMenu.controller/$itemMenu.action"}'>
                            <span class="pcoded-micon"><i class="${itemMenu.icono}"></i></span>
                            <span class="pcoded-mtext" data-i18n="nav.basic-components.main">${itemMenu.nombre}</span>
                            <span class="pcoded-mcaret"></span>
                          </a>
                          <ul class="pcoded-submenu">
                            <g:each in="${itemMenu.getHijos(userLogged)}" var="itemMenuHijo" status="j">
                              <li id="li${itemMenuHijo.controller}${itemMenuHijo.action}" class="">
                                <a href='${"/$itemMenuHijo.controller/$itemMenuHijo.action"}'>
                                  <span id="span${itemMenuHijo.controller}${itemMenuHijo.action}" class="pcoded-mtext"
                                    data-i18n="nav.dash.main">
                                    ${itemMenuHijo.nombre}
                                  </span>
                                </a>
                              </li>
                            </g:each>
                          </ul>
                        </li>
                      </g:each>
                    </ul>
                  </div>
                </nav>

                <div class="pcoded-content">
                  <div class="pcoded-inner-content">
                    <g:layoutBody />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
        <script type="text/javascript">
          $(document).ready(function () {
            // if(($("#menuPrincipal").css('margin-left') == "0px") && ($("#perfil").val() == "esconder_lateral")){
            // 	$('#mobile-collapse').trigger("click");
            // 	$('#mobile-collapse').hide();
            // }

            // var url = "${request.getRequestURL()}"
            // if(url.includes("notificacion/list"))
            // 	$('#badgeNotificaciones').hide();
            $("#li${controllerName}${actionName}")
              .parent("ul")
              .parent("li")
              .addClass("active pcoded-trigger");
            $("#span${controllerName}${actionName}").css({ "font-weight": "bold" });

            $("#menu-perfil").click(function () {
              $("#menu-desplegable").slideToggle("slow");
              $("#flecha-menu").toggleClass("ti-angle-down");
            });
          });
        </script>
      </body>

</html>