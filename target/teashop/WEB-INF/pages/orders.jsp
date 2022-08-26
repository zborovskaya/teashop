<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<jsp:useBean id="orders" type="java.util.ArrayList" scope="request"/>
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
            <div>
                <h1>История заказов</h1>
            </div>
            <div>
                <c:if test="${empty orders}">
                    <p id="product-description-text">
                    <h5>Заказы отсутствуют!
                    </h5>
                    </p>
                </c:if>
            </div>
        </div>
        <br>
    <c:if test="${not empty orders}">
        <h2 style="color:#0bda51">История заказов</h2>
        <br>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">Добавлено</th>
                <th scope="col">Статус</th>
                <th scope="col">Цена</th>
                <th scope="col"></th>
                <c:if test="${user.userRole eq 'ADMIN'}">
                    <th scope="col"></th>
                </c:if>
            </tr>
            </thead>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>
                            ${order.deliveryDate}
                    </td>
                    <td>
                        <c:if test="${order.status eq 'waiting'}">
                            Ожидание
                        </c:if>
                        <c:if test="${order.status eq 'accepted'}">
                            Принят
                        </c:if>
                        <c:if test="${order.status eq 'rejected'}">
                            Отклонён
                        </c:if>
                    </td>
                    <td>
                            ${order.totalCost}
                    </td>
                    <td>
                        <form action="${pageContext.servletContext.contextPath}/order/Overview?command=getDetails">
                            <button type="submit" class="btn btn-light">Детали заказа</button>
                            <input name="orderId" value="${order.id}" type="hidden">
                            <input name="command" value="getDetails" type="hidden">
                        </form>
                    </td>
                    <c:if test="${user.userRole eq 'ADMIN' and order.status eq 'waiting'}">
                        <td>
                            <form action="/order/Overview" method="post">
                                <button type="submit" class="btn btn-danger">Принять</button>
                                <input name="status" value="accepted" type="hidden">
                                <input name="orderId" value="${order.id}" type="hidden">
                            </form>
                        </td>
                        <td>
                            <form action="/order/Overview" method="post">
                                <button type="submit" class="btn btn-danger">Отклонить</button>
                                <input name="status" value="rejected" type="hidden">
                                <input name="orderId" value="${order.id}" type="hidden">
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            <tbody>
            </tbody>
        </table>
    </c:if>
        <br>
    </div>
    <div class="text-center" style="background-color:#0bda51">
        <footer>
            © 2021-2022 Copyright by Zborovskaya Anna
        </footer>
    </div>
</div>
</body>
</html>