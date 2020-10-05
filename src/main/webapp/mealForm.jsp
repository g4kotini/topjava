<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal</h2>
<form action="${pageContext.request.contextPath}/meals?action=save" method="post" class="meal-update-form">
    <input type="hidden" id="id" name="mealId" value="${meal.id}">
    <div class="meal-update-form-part">
        <label for="date">Date: </label>
        <input type="date" id="date" name="date" value="${meal.date}"/>
    </div>
    <div class="meal-update-form-part">
        <label for="time">Time: </label>
        <input type="time" id="time" name="time" value="${meal.time}"/>
    </div>
    <div class="meal-update-form-part">
        <label for="description">Description: </label>
        <input type="text" id="description" name="description" value="${meal.description}"/>
    </div>
    <div class="meal-update-form-part">
        <label for="calories">Calories: </label>
        <input type="number" id="calories" name="calories" value="${meal.calories}"/>
    </div>
    <div class="meal-update-form-part">
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </div>
</form>
</body>
</html>
