<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<style>
    th {
        padding: 10px;
    }
    tbody > tr:hover {
        background-color: beige;
    }
    thead > tr {
        background-color: burlywood;
    }
</style>

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

</body>
</html>
