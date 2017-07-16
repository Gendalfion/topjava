<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <jsp:useBean id="mealsWithExceed" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsWithExceed}" var="mealsWithExceed">
        <tr style="color: <c:out value="${mealsWithExceed.exceed ? 'red' : 'green'}"/>">
            <td><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${mealsWithExceed.date}"/></td>
            <td><c:out value="${mealsWithExceed.description}"/></td>
            <td><c:out value="${mealsWithExceed.calories}"/></td>
            <td><a href="meals?action=edit&mealId=<c:out value="${mealsWithExceed.id}"/>">Изменить</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${mealsWithExceed.id}"/>">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
