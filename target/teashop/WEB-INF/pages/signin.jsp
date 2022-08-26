<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:set var="current_page" value="${pageContext.request.requestURI}" scope="session"/>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/CSS/style.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

    <script type="text/javascript">
        window.history.forward();

        function noBack() {
            window.history.forward();
        }
    </script>
    <title>Авторизация</title>
</head>
<body>
<div class="page">
    <header>
        <%@include file="header.jsp" %>
    </header>
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3">Вход</h3>
        <form name="LoginForm" method="post" action="/signin" novalidate>
            <input type="hidden" name="command" value="sign_in"/>
            </br>
            <div class="form-group" class="mb-3">
                <label class="form-label">Логин</label>
                <input type="text" name="login" class="form-control form-control-sm" value="${user.login}">
            </div>
            </br>
            <div class="form-group" class="mb-3">
                <label class="form-label">Пароль</label>
                <input type="password" name="password" class="form-control form-control-sm" value="${user.password}">
            </div>
            <c:if test="${not empty errorMessage}">
                <div style="color: red">
                        ${errorMessage}
                </div>
            </c:if>
            <c:if test="${! empty blocked}">
                <div style="color: red">
                        ${user_blocked}
                </div>
            </c:if>
            </br>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">Войти</button>
                <%--                <a class="btn btn-info" href="${absolutePath}/WEB-INF/pages/registration.jsp" role="button">Регистрация</a>--%>
                <%--                <button type="submit" class="btn btn-primary" formaction="/registration" formmethod="get">Регистрация</button>--%>
            </div>
        </form>
        <br>
        <form action="/registration">
            <div class="text-center">
                <button type="submit" class="btn btn-primary">Регистрация
                </button>
            </div>
        </form>
    </div>

    <div class="text-center" style="background-color:#0bda51">
        <footer>
            © 2021-2022 Copyright by Zborovskaya Anna
        </footer>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')

        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }

                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>
</body>
<hr/>
</html>
