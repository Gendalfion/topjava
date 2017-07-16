<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Подсчет калорий</title>

    <style>
        th {
            padding: 10px;
        }
        td {
            padding: 5px;
        }
        tbody > tr:hover {
            background-color: beige;
        }
        thead > tr {
            background-color: burlywood;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Моя еда</h2>

<jsp:useBean id="mealsWithExceed" scope="request" type="java.util.List"/>
<c:if test="${mealsWithExceed.size() > 0}">
<table border=0>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan=2>Изменить/Удалить</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${mealsWithExceed}" var="meal">
        <tr style="color: <c:out value="${meal.exceed ? 'red' : 'green'}"/>">
            <td><javatime:format value="${meal.dateTime}" style="MS"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Изменить</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:if>
<c:if test="${mealsWithExceed.size() == 0}">
    <h3>Нет данных для отображения...</h3>
</c:if>

<%--@elvariable id="mealToEdit" type="ru.javawebinar.topjava.model.Meal"--%>
<c:if test="${not empty mealToEdit}">
    <jsp:useBean id="mealToEdit" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <h3>Добавление/удаление еды:</h3>
    <form action="meals?action=edit_add" method="post">
        <table>
            <tbody>
            <tr>
                <td>Описание:</td>
                <td><input title="Описание" type="text" name="description" value="<c:out value="${mealToEdit.description}"/>"></td>
            </tr>
            <tr>
                <td>Калории:</td>
                <td><input title="Калории" type="text" name="calories" value="<c:out value="${mealToEdit.calories}"/>"></td>
            </tr>
            </tbody>
        </table>

        <button type="submit" formaction="meals" title="Отмена изменений">Отмена</button>
    </form>
</c:if>

</body>
</html>
