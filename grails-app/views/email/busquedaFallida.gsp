<!DOCTYPE html>
<head>
    <title>Búsqueda fallida</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fff;
            color: #000;
            padding: 20px
        }

        .container {
            background-color: #fff;
            padding: 15px;
            border-radius: 10px;
            text-align: center
        }

        p {
            font-size: 35px;
            color: #000;
            margin-bottom: 30px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #f29705;
            color: #FFFFFF;
            font-size: 20px ;
            border-radius: 5px;
            text-decoration: none;
        }

        .button:hover {
            opacity: 50%;
        }
    </style>
</head>

<body>
    <div class="container"> 
        <img style="max-width: 60%; margin-top: 15px;" src="https://tecnofind.es/assets/auth/logo-dark-cbaa484c32ef19745ff206da8627367e.png"/>
        <p>Ha ocurrido un error en la búsqueda con los siguientes datos:</p>
        <p>${inputs}</p>
    </div>
</body>
</html>