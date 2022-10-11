<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:useBean id="create" scope="request" type="java.lang.Boolean"/>
    <title>${create ? 'Create a new meal' : 'Update an existing meal'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<ul>
    <li><a href="${pageContext.request.contextPath}/index.html">Home</a></li>
</ul>
<h3>${create ? 'Create a new meal' : 'Update an existing meal'}</h3>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form action="${pageContext.request.contextPath}/meals" method="POST">
    <ul>
        <li>
            <input type="hidden" name="id" value="${meal.id}">
        </li>
        <li>
            <label for="dateTime">Date & Time</label>
            <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}">
        </li>

        <li>
            <label for="description">Description</label>
            <textarea id="description" name="description">${meal.description}</textarea>
        </li>

        <li>
            <label for="calories">Calories</label>
            <input type="number" id="calories" name="calories" value="${meal.calories}">
        </li>

        <li class="button">
            <button type="submit">Send</button>
        </li>
    </ul>
</form>
</body>
</html>
