<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://topjava.javawebinar.ru/functions" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="${pageContext.request.contextPath}/meals?action=create">Add Meal</a>
<br><br>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${requestScope.meals}">
        <tr class="${meal.excess == true ? "excess" : "norm"}">
            <td><c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd hh:mm')}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=update&mealId=${meal.id}">Update</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
