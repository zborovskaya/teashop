<%--
  Created by IntelliJ IDEA.
  User: zav
  Date: 01.04.2022
  Time: 19:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${user.userRole eq 'CLIENT'}">
${sessionScope.user}
</c:if>
</body>
</html>
