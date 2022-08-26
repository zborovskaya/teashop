<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="order" type="by.zborovskaya.final_project.entity.Order" scope="request"/>
<html>

<head>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${absolutePath}/CSS/style.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript">
        window.history.forward();

        function noBack() {
            window.history.forward();
        }
    </script>
    <title>Заказы</title>
</head>
<body>
<div class="page">
    <header>
        <%@include file="/WEB-INF/pages/header.jsp" %>
    </header>
    <div class="container-fluid justify-content-center">
        <div class="text-center">
            <br>
            <h4>Оформление заказа</h4>
            <c:if test="${not empty param.message}">
                <%--                <div class="success">--%>
                <%--                    <h5>Обновление произошло успешно!</h5>--%>
                <%--                </div>--%>
            </c:if>
            <c:if test="${not empty errors}">
                <div class="error">
                    <h5>Ошибка!</h5>
                </div>
            </c:if>
        </div>
        <br>
        <div class="row gx-5">
            <div class="col">
                <div class="p-3 border bg-light">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Картинка</th>
                            <th scope="col">Название</th>
                            <th scope="col">Цена</th>
                            <th scope="col">Количество</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="product" items="${products}" varStatus="status">
                            <tr>
                                <td>
                                    <img class="product-tile"
                                         src="http://bestea.by/image/cachewebp/catalog/product/black/b51-700x700.webp"/>
                                </td>
                                <td>
                                        ${product.name}
                                </td>
                                <td class="price">
                                        ${product.price}
                                </td>
                                <td>
                                        ${order.items.get(status.index).quantity}
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="price"> Общее количество</td>
                            <td>${order.totalQuantity}</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="price">Общая цена</td>
                            <td>${order.totalCost}</td>
                        </tr>
                        </tbody>
                    </table>

                </div>
            </div>
            <div class="col">
                <div class="p-3 border bg-light">
                    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
                        <div class="form-group">
                            <div class="form-group">
                                <label class="form-label">Адрес</label>
                                <input type="text" name="deliveryAddress" value="${param.deliveryAddress}"
                                       class="form-control"
                                       placeholder="Введите адрес" required pattern=".+">
                                <div class="invalid-feedback">
                                    Необходимо ввести адрес!
                                </div>
                            </div>
                            <%--                        <label class="form-label">Метод оплаты</label>--%>
                            <%--                        <select class="form-select" name="payment" required>--%>
                            <%--                            <c:forEach var="paymentMethod" items="${paymentMethods}">--%>
                            <%--                        <c:if test="${order.paymentMethod eq paymentMethod.name()}">--%>
                            <%--                            <option selected>${paymentMethod}</option>--%>
                            <%--                        </c:if>--%>
                            <%--                        <c:if test="${not (order.paymentMethod eq paymentMethod.name())}">--%>
                            <%--                            <option>${paymentMethod}</option>--%>
                            <%--                        </c:if>--%>
                            <%--                            </c:forEach>--%>
                            </select>
                            <label class="form-label">Метод оплаты</label>
                            <select class="form-select" name="paymentMethod" required>
                                <c:forEach var="paymentMethod" items="${paymentMethods}">
                                    <c:if test="${order.paymentMethod eq paymentMethod.name()}">
                                        <option selected>${paymentMethod}</option>
                                    </c:if>
                                    <c:if test="${not (order.paymentMethod eq paymentMethod.name())}">
                                        <option>${paymentMethod}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <br>
                        <br>
                        <div class="text-center">
                            <button type="submit" class="btn btn-light">
                                <img style=" max-width: 64px"
                                     src="${pageContext.servletContext.contextPath}/images/ok2.png"/>
                                Оформить заказ
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
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