<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>

<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${absolutePath}/CSS/style.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script type="text/javascript">
        window.history.forward();

        function noBack() {
            window.history.forward();
        }
    </script>
    <title>Регистрация</title>

</head>
<body>
<div class="page">
    <header>
        <%@include file="header.jsp" %>
    </header>
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3">Регистрация</h3>
        <form role="form" action="${absolutePath}/registration" method="post" class="needs-validation" novalidate>
<%--            <input type="hidden" name="command" value="registration"/>--%>
            <div class="row gy-3">
                <div class="form-group">
                    <label class="form-label">Имя</label>
                    <input type="text" name="first_name" class="form-control" value="${param.first_name}"
                           placeholder="Иван" required pattern="^[A-ZА-Я][a-zа-я]+$">
                    <c:if test="${!empty invalid_first_name}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_first_name}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Неккоректно введено имя!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Фамилия</label>
                    <input type="text" name="last_name" class="form-control" value="${param.last_name}"
                           placeholder="Иванов" required pattern="^[A-ZА-Я][a-zа-я]+$">
                    <c:if test="${!empty invalid_last_name}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_last_name}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Некоррекктно введена фамилия!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" value="${param.email}" class="form-control form-control-sm"
                           placeholder="Ivan@mail.ru" required pattern="^[A-Za-z0-9\.]{1,30}@[a-z]{2,7}\.[a-z]{2,4}$">
                    <%--                    <div id="emailHelp" class="form-text">Здесь шаблон коррректного емайла</div>--%>
                    <c:if test="${!empty invalid_email}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_email}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Некорректный email!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Телефон</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text" id="basic-addon1">+375</span>
                        <input type="text" name="phone_number" value="${param.phone_number}" class="form-control"
                               placeholder="29*******" required pattern="(29|25|44|33)\d{7}">
                    </div>
                        <c:if test="${!empty invalid_phone_number}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_phone_number}"/>
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                           Телефон должен соответсвовать 29*******, 44*******, 25*******, 33*******
                        </div>
                    </div>
                <div class="form-group">
                    <label class="form-label">Адрес</label>
                    <input type="text" name="address" value="${param.address}" class="form-control"
                           placeholder="Введие адрес" required pattern=".+">
                    <c:if test="${!empty invalid_address}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_address}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Необходимо ввести адрес!
                    </div>
                </div>
                    <div class="form-group">
                        <label class="form-label">Логин</label>
                        <input type="text" name="login" value="${param.login}" class="form-control"
                               placeholder="Ivan15" required pattern="^[a-zA-Z_0-9]{4,20}$">
                        <c:if test="${!empty invalid_login}">
                            <div class="invalid-feedback-backend" style="color: red">
                                ${invalid_login}
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            Неккоректный логин! Можно использовать буквы, цифры и знак подчеркивания (4-16 символов).
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Пароль</label>
                        <input type="password" name="password" value="${param.password}"
                               class="form-control form-control-sm" placeholder="Введите пароль" required
                               pattern="^[a-zA-Z_0-9]{5,40}$">
                        <c:if test="${!empty invalid_password}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_password}"/>
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            Некорректный пароль! Можно использовать буквы, цифры и знаки подчёркивания (5-40 символов).
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Подтверждение пароля</label>
                        <input type="password" name="repeat_password" value="${param.repeat_password}"
                               class="form-control form-control-sm" placeholder="Повторите пароль" required
                               pattern="^[a-zA-Z_0-9]{5,40}$">
                        <c:if test="${!empty invalid_repeat_password}">
                            <div class="invalid-feedback-backend" style="color: red">
                                ${invalid_repeat_password}
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            Неправильно повторен пароль
                        </div>
                    </div>
                    <div class="text-center mb-3">
                        <button type="submit" class="btn btn-success">Зарегистрироваться</button>
                    </div>
                </div>
        </form>
    </div>
    <div class="text-center" style="background-color:#0bda51">
        <footer>
            © 2021-2022 Copyright by Zborovskaya Anna
        </footer>
    </div>
</div>
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
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>

