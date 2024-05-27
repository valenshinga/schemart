<!DOCTYPE html>
<html>

<head>
    <title>Resultados de Búsqueda</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fff;
            color: #000;
            padding: 20px;
        }

        .container {
            background-color: #f2f2f2;
            padding: 15px;
            border-radius: 10px;
            text-align: center;
        }

        h1 {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
        }

        .resultados {
            text-align: left;
            margin-top: 10px;
            border-top: 1px solid #ddd;
            padding-top: 10px;
        }

        .resultados h2 {
            font-size: 20px;
            color: #555;
        }

        .resultados p {
            font-size: 16px;
            color: #666;
        }
    </style>
</head>

<body>
    <div class="container">
        <h1>Resultados de Tu Búsqueda</h1>
        <div class="resultados">
            <h2>Inputs de Búsqueda:</h2>
            <p>${busqueda.inputs.join(",")}</p>
            <g:if test="${mensajeError != 'Ya hay resultados para la persona buscada'}">
                <h2>Cantidad de resultados: <strong>${cantResultados}</strong></h2>
                <p></p>
                <a href="${createLink(absolute: true, controller: 'busqueda', action: 'show', params: [id: busqueda.id])}">Click aquí para ver más info.</a>
                <h2>Fuentes:</h2>
                <p>${raw(busquedaResultados?.sitiosVisitados?.join("<br />"))}</p>
            </g:if>
            <g:else>
                <h2><strong>${mensajeError}</strong></h2>
                <p></p>
                <a href="${createLink(absolute: true, controller: 'persona', action: 'show', params: [id: idDePersona])}">Click aquí para ver más detalles de esta persona.</a>
            </g:else>    
        </div>
    </div>
</body>

</html>