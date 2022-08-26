<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript">
        window.history.forward();

        function noBack() {
            window.history.forward();
        }
    </script>
</head>
<body>
<br>
<div class="text-center">
    <div class="row">
        <div class="col">
            <h6>Хотите добавить продукт?</h6>
            <form action="/products">
                <button type="submit" class="btn btn-danger">Добавить</button>
                <input name="command" value="add" type="hidden">
            </form>
        </div>
        <div class="col">
            <h6>Восстановить заблокированные продукты?</h6>
            <form action="/products">
                <button type="submit" class="btn btn-danger">Восстановить</button>
                <input name="command" value="restore" type="hidden">
            </form>
        </div>
        <div class="col">
            <h6>Загрузить продукт из xml?</h6>
        <form action="/UploadXml" method="post" enctype="multipart/form-data">
            <button type="submit" class="btn btn-warning">Загрузить</button>
            <input type="file" name="file" size="60" required />
        </form>
            <c:if test="${not empty param.error}">
                <div class="error">
                    <h5>Ошибка!</h5>
                </div>
            </c:if>
        </div>
    </div>
    <br>
    <h2>Список продуктов</h2>
    <br>
    <c:if test="${not empty error}">
        <div class="error">
            <h5>Ошибка!</h5>
        </div>
    </c:if>
    <br>
    <div class="row align-items-start">
        <div class="col">
        </div>
        <div class="col">
            <form class="d-flex"
                  action="${absolutePath}/main">
                <input class="form-control me-2" type="search" placeholder="Введите название продукта" aria-label="Search" name="search"
                       value="${param.search}">
                <%--                <input name="command" value="${requestScope.command}" type="hidden" >--%>
                <%--                <input name="command" value="${requestScope.category_id}" type="hidden" >--%>
                <button class="btn btn-outline-success" type="submit">Поиск</button>
            </form>
        </div>
        <div class="col">
        </div>
    </div>
</div>
<br>
<table class="table">
    <thead>
    <tr>
        <th scope="col">Картинка</th>
        <th scope="col">Название</th>
        <th scope="col">Описание</th>
        <th scope="col">Цена</th>
        <th scope="col">Состояние</th>
        <th scope="col"></th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${requestScope.productList}">
        <tr>
            <th width="150">
                <img class="product-tile"
                     src="http://bestea.by/image/cachewebp/catalog/product/black/b51-700x700.webp"/>
            </th>
            <td width="200">${item.name}</td>
            <td width="350">${item.composition}</td>
            <td>${item.price}</td>
            <td>${item.active}</td>
            <td>
                <form action="/products">
                    <button type="submit" class="btn btn-warning">Обновить</button>
                    <input name="productId" value="${item.id}" type="hidden">
                    <input name="command" value="update" type="hidden">
                </form>
            </td>
            <td>
                <form action="/products" method="post">
                    <button type="submit" class="btn btn-dark">Блокировать</button>
                    <input name="productId" value="${item.id}" type="hidden">
                    <input name="command" value="blocked" type="hidden">
                </form>
            </td>
            <td>
                <form action="/products" method="post">
                    <button type="submit" class="btn btn-warning">XML</button>
                    <input name="productId" value="${item.id}" type="hidden">
                    <input name="command" value="getXml" type="hidden">
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
