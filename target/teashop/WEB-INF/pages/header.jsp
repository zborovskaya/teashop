<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<html>
<head>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    <title>Главная</title>
</head>
<body>
<header style="
    font: 400 70px/1.3 'Lobster Two', Helvetica, sans-serif;
    color: #0bda51;
    text-shadow: 1px 1px 0px #ededed, 4px 4px 0px rgba(0, 0, 0, 0.15);">
    <a style="text-decoration: none; color: #2b2b2b" href="${pageContext.servletContext.contextPath}/main">
        <img style=" max-width: 64px" src="${pageContext.servletContext.contextPath}/images/green_tea_cup.png"/>
        GoodTea
    </a>
    <c:if test="${user.userRole eq 'CLIENT'}">
        <a href="${pageContext.servletContext.contextPath}/cart">
            <img style="max-width: 64px;
        float: right; margin-top: 15px;margin-right: 50px;"
                 src="${pageContext.servletContext.contextPath}/images/blackCart.png" alt="cart_image"/>
        </a>
    </c:if>
</header>
<nav class="navbar navbar-expand-lg navbar-light" style="height: 50px;background-color:#0bda51;">
    <div class="container-fluid" style="height: 100px;">
        <%--        <a class="navbar-brand" href="#">--%>
        <%--            &lt;%&ndash;            <img src="${absolutePath}/images/green_tea_cup.png" style="width: 40px;margin: 5px 20px;"/>&ndash;%&gt;--%>
        <%--            &lt;%&ndash;                        GoodTea&ndash;%&gt;--%>
        <%--        </a>--%>
        <%--        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDarkDropdown"--%>
        <%--                aria-controls="navbarNavDarkDropdown" aria-expanded="false" aria-label="Toggle navigation">--%>
        <%--            <span class="navbar-toggler-icon"></span>--%>
        <%--        </button>--%>
        <div class="collapse navbar-collapse" id="navbarNavDarkDropdown">
            <ul class="navbar-nav">
                <%--                <li class="nav-item"><a class="nav-link active" href="${absolutePath}/jsp/pages/home.jsp">Главная</a>--%>
                <%--                </li>--%>
                <li class="dropdown nav-item">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDarkDropdownMenu" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        Категории</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item"
                               href="${absolutePath}/main">Все</a>
                        </li>
                        <c:forEach var="item" items="${applicationScope.categoryList}">
                            <li><a class="dropdown-item"
                                   href="${absolutePath}/main?command=find_all_products_by_category&category_id=${item.id}">${item.categoryName}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
                <c:if test="${user.userRole eq 'ADMIN'}">
<%--                    <li class="dropdown nav-item">--%>
<%--                        <a class="nav-link dropdown-toggle" href="#" id="navbarDarkDropdownUsers" role="button"--%>
<%--                           data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--                            Пользователи</a>--%>
<%--                        <ul class="dropdown-menu">--%>
<%--                            <li><a class="dropdown-item"--%>
<%--                                   href="${absolutePath}/controller?command=find_all_users">Клиенты</a>--%>
<%--                            </li>--%>
<%--                            <li><a class="dropdown-item" href="${absolutePath}/controller?command=find_all_admins">Администраторы</a>--%>
<%--                            </li>--%>
<%--                        </ul>--%>
<%--                    </li>--%>
                    <%--                    <li class="nav-item">--%>
                    <%--                        <a class="nav-link"--%>
                    <%--                           href="${absolutePath}/jsp/pages/registration.jsp">Добавить администратора</a>--%>
                    <%--                    </li>--%>
                </c:if>
                <c:if test="${user.userRole eq 'ADMIN' or user.userRole eq 'CLIENT'}">
<%--                    <li class="nav-item dropdown">--%>
<%--                        <a class="nav-link dropdown-toggle"--%>
<%--                           href="#"--%>
<%--                           id="navbarDarkDropdownMenuLink" role="button"--%>
<%--                           data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--                            Профиль</a>--%>
<%--                        <ul class="dropdown-menu">--%>
<%--                            <li><a class="dropdown-item"--%>
<%--                                   href="#">Кабинет</a></li>--%>
<%--                            <li>--%>
<%--                                <hr class="dropdown-divider">--%>
<%--                            </li>--%>
<%--                            <li><a class="dropdown-item"--%>
<%--                                   href="#">Настройки</a>--%>
<%--                            </li>--%>
<%--                        </ul>--%>
<%--                    </li>--%>
                    <li class="nav-item">
                        <a class="nav-link"
                           href="${pageContext.servletContext.contextPath}/order/Overview?command=getOrdersForUser">Заказы</a>
                    </li>
                </c:if>
<%--                <form class="d-flex">--%>
<%--                    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">--%>
<%--                    <button class="btn btn-outline-success" type="submit">Search</button>--%>
<%--                </form>--%>

                <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
                        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
                        crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
                        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
                        crossorigin="anonymous"></script>

            </ul>
        </div>
        <div>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${user.userRole eq 'ADMIN' or user.userRole eq 'CLIENT'}">
                        <li class="nav-item"><a class="nav-link" href="${absolutePath}/sign_out">Выход</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <%--                        <li class="nav-item"><a class="nav-link" href="${absolutePath}/jsp/pages/signIn.jsp">Вход</a>--%>
                        <%--                        </li>--%>
                        <form class="d-flex" action="/signin">
                            <button class="btn btn-outline-success" type="submit">Вход</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
