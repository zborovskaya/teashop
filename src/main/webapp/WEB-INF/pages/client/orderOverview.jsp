<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
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
    <title>Корзина</title>
</head>
<body>
<div class="page">
    <header>
        <%@include file="/WEB-INF/pages/header.jsp" %>
    </header>
    <div class="container-fluid justify-content-center">
        <div class="text-center">
            <br>
            <div class="success">
                <h1>Заказ оформлен!</h1>
            </div>
        </div>
        <div class="text-center">
            <div class="success">
                <h4>Ваш заказ будет рассмотрен администратором, скоро с вами свяжутся для уточнения деталей.</h4>
            </div>
        </div>
        <br>
        <h2 style="color:#0bda51">История заказов</h2>
        <br>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">Добавлено</th>
                <th scope="col">Статус</th>
                <th scope="col">Цена</th>
                <th scope="col"></th>
            </tr>
            </thead>
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
            </tr>
            <tbody>
            </tbody>
        </table>
        <br>
        <div class="text-center">
            <form action="/main">
                <button class="btn btn-warning" type="submit">Вернуться к списку продуктов</button>
            </form>
            <br>
            <br>
            <form action="${pageContext.servletContext.contextPath}/order/Overview">
                <button class="btn btn-success" type="submit">Посмотреть историю заказов</button>
                <input name="command" value="getOrdersForUser" type="hidden">
            </form>
        </div>

    </div>
    <div class="text-center" style="background-color:#0bda51">
        <footer>
            © 2021-2022 Copyright by Zborovskaya Anna
        </footer>
    </div>
</div>
</body>
</html>


