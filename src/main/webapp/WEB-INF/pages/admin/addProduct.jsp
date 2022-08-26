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
    <title>Добавление продукта</title>

</head>
<body>
<div class="page">
    <header>
        <%@include file="/WEB-INF/pages/header.jsp" %>
    </header>
    <br>
    <c:if test="${not empty param.success}">
        <h4 style="text-align: center; color: green">
            Продукт добавлен успешно!
        </h4>
    </c:if>
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3">Добавить новый продукт</h3>
        <form role="form" action="${absolutePath}/products" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="add_new_product"/>
            <div class="row gy-3">
                <div class="form-group">
                    <label class="form-label">Название</label>
                    <input type="text" name="product_name" class="form-control" value="${param.product_name}" required
                           pattern=".+">
                    <div class="invalid-feedback">
                        Название не может быть пустым!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Сотстав</label>
                    <input type="text" name="product_composition" class="form-control" value="${param.product_composition}" required
                           pattern=".{0,200}">
                    <div class="invalid-feedback">
                        Состав не может превышать 200 символов!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Вес</label>
                    <input type="text" name="weight" value="${param.weight}" class="form-control"
                           required pattern="\d{1,6}(\.[0-9]{1,3})?">
                    <c:if test="${!empty param.invalid_weight}">
                        <div class="invalid-feedback-backend" style="color: red">
                            Некорректно введён вес!
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Некорректно введён вес! Вес должен быть положительным числом. Можно рописывать 3 знака после запятой.
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Время заваривания</label>
                    <input type="time" name="product_time" value="${param.product_time}" class="form-control" required>
                    <div class="invalid-feedback">
                        Должно быть введено предположительное время заваривания чая!
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Температура</label>
                    <input type="text" name="temperature" value="${param.temperature}" class="form-control"
                           required pattern="[0-9]{1,3}">
                    <c:if test="${!empty param.invalid_temperature}">
                        <div class="invalid-feedback-backend" style="color: red">
                            Некорректно введена температру. Должна быть от 0 до 100 градусов.
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Некорректно введена температру. Должна быть от 0 до 100 градусов.
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">Цена</label>
                    <input type="text" name="price" value="${param.price}" class="form-control"
                           required pattern="\d{1,6}(\.[0-9]{1,2})?">
                    <c:if test="${!empty param.invalid_price}">
                        <div class="invalid-feedback-backend" style="color: red">
                           Некорректно введена цена!
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        Некорректно введён цена! Вес должен быть положительным числом.
                    </div>
                </div>
                <div class="form-select-button">
                    <select class="form-select" name="product_category" required>
                        <c:forEach var="item" items="${applicationScope.categoryList}">
                            <option value="${item.id}">${item.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="text-center mb-3">
                    <button type="submit" class="btn btn-success">Добавить</button>
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

