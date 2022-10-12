<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<ul>
    <li><a href="${pageContext.request.contextPath}/index.html">Home</a></li>
    <li><a href="${pageContext.request.contextPath}/meals?action=create">Create meal</a></li>
</ul>
<table>
    <caption>Meals</caption>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <c:forEach var="mealTo" items="${mealTos}">
        <tr class="${mealTo.excess ? 'red' : 'green'}">
            <td>${f:formatLocalDateTime(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=update&id=${mealTo.id}">Update</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
