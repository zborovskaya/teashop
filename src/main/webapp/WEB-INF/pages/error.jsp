<%--
  Created by IntelliJ IDEA.
  User: zav
  Date: 24.04.2022
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript">
        window.history.forward();

        function noBack() {
            window.history.forward();
        }
    </script>
    <title>Error</title>
</head>
<body>
<div class="text-center">
    <h1 style="color: red">Произошла ошибка!</h1>
    <h2>
        <a href="${pageContext.servletContext.contextPath}/main">Вернутся на главную?</a>
    </h2>
</div>
</body>
</html>
