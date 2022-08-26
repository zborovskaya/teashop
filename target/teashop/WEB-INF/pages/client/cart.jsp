<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="by.zborovskaya.final_project.entity.Cart" scope="request"/>
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
    <title>Корзина</title>
</head>
<body>
<div class="page">
    <header>
        <%@include file="/WEB-INF/pages/header.jsp" %>
    </header>
    <div class="container-fluid justify-content-center">
        <div class="text-center">
            <p id="product-description-text">
            <h4>Содержимое корзины</h4>
            </p>
            <c:if test="${not empty param.message}">
                <div class="success">
                    <h5>Обновление произошло успешно!</h5>
                </div>
            </c:if>
            <c:if test="${not empty errors}">
                <div class="error">
                    <h5>Ошибка!</h5>
                </div>
            </c:if>
            <c:if test="${empty cart.items}">
                <p id="product-description-text">
                <h5>Ваша корзина пуста!<a href="${pageContext.servletContext.contextPath}/main">Добавить продукты?</a>
                </h5>
                </p>
            </c:if>
        </div>
        <c:if test="${not empty cart.items}">
            <form action="${pageContext.servletContext.contextPath}/checkout">
                <div style="float: right;margin-right: 50px;">
                    <button type="submit" class="btn btn-light">
                        <img style=" max-width: 64px" src="${pageContext.servletContext.contextPath}/images/ok2.png"/>
                        Оформить заказ
                    </button>
                </div>
            </form>
            <form method="post" action="${pageContext.servletContext.contextPath}/cart">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Картинка</th>
                        <th scope="col">Название</th>
                        <th scope="col">Цена</th>
                        <th scope="col">Количество</th>
                        <th scope="col"></th>
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
                                <div class="row">
                                        <fmt:formatNumber value="${cart.items.get(status.index).quantity}"
                                                          var="quantity"/>
                                        <c:set var="error" value="${errors[product.id]}"/>
                                        <%--                                    <div class="col-6"></div>--%>
                                    <div class="col-6">
                                        <input name="quantity"
                                               value="${not empty error ? quantityError[product.id] :quantity}"
                                               class="form-control"
                                               type="number"
                                               min="1"
                                               max="100">
                                        <input name="productId" value="${product.id}" type="hidden">
                                        <c:if test="${not empty error}">
                                            <div class="error">
                                                    ${error}
                                            </div>
                                        </c:if>
                                    </div>
                            </td>
                            <td>
                                <button type="submit" class="btn btn-danger" form="deleteCartItem"
                                        formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${product.id}">
                                    Удалить
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>Общая цена: ${cart.totalCost}</td>
                        <td>Общее количество: ${cart.totalQuantity}</td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
                <br>
                <div class="text-center">
                    <p>
                        <button type="submit" class="btn btn-warning">Обновить</button>
                    </p>
                </div>
            </form>
            <form id="deleteCartItem" method="post">
            </form>
        </c:if>
    </div>
    <div class="text-center" style="background-color:#0bda51">
        <footer>
            © 2021-2022 Copyright by Zborovskaya Anna
        </footer>
    </div>
</div>
</body>
</html>

