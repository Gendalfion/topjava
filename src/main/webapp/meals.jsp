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

        a {
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .message {
            color: green;
            background-color: lightblue;
        }

        .error {
            color: red;
            background-color: #ffc9ba;
        }

        .button {
            font: bold 11px Arial;
            text-decoration: none;
            background-color: #EEEEEE;
            color: #333333;
            padding: 2px 6px 2px 6px;
            border-top: 1px solid #CCCCCC;
            border-right: 1px solid #333333;
            border-bottom: 1px solid #333333;
            border-left: 1px solid #CCCCCC;
            margin: 15px
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Моя еда</h2>

<%--@elvariable id="mealToEdit" type="ru.javawebinar.topjava.model.Meal"--%>
<c:if test="${empty mealToEdit}">
    <h4><a href="meals?action=add">Добавить еду...</a></h4>
</c:if>

<c:if test="${not empty param.message}">
    <div class="message">
        <h5>${param.message}</h5>
    </div>
</c:if>

<c:if test="${not empty param.error}">
    <div class="error">
        <h5>${param.error}</h5>
    </div>
</c:if>

<jsp:useBean id="mealsWithExceed" scope="request" type="java.util.List"/>
<c:if test="${mealsWithExceed.size() > 0}">
<table border=0>
    <thead>
    <tr>
        <th>Дата/время</th>
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

<c:if test="${not empty mealToEdit}">
    <jsp:useBean id="mealToEdit" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <c:set var ="isMealAddForm" scope = "request" value = "${mealToEdit.id < 0}"/>
    <h3><c:out value="${isMealAddForm ? 'Добавление': 'Изменение'}"/> еды:</h3>

    <form action="meals?action=update_add" method="post">
        <input name="id" value="<c:out value="${mealToEdit.id}"/>" type="hidden">
        <table>
            <tbody>
            <tr>
                <td>Дата/время:</td>
                <td><input title="Дата/время" type="datetime-local" name="dateTime" value="${mealToEdit.dateTime}"></td>
            </tr>
            <tr>
                <td>Описание:</td>
                <td><input title="Описание" type="text" name="description" placeholder="Описание еды..." value="${mealToEdit.description}"></td>
            </tr>
            <tr>
                <td>Калории:</td>
                <td><input title="Калории" type="text" name="calories" value="${mealToEdit.calories}"></td>
            </tr>
            </tbody>
        </table>

        <button type="submit" title="Подтвердить действие">
            <c:out value="${isMealAddForm ? 'Добавить': 'Изменить'}"/>
        </button>

        <a href="meals" class="button">Отмена</a>
    </form>

    <%--Jumping to the form if form is visible:--%>
    <a name="formJump"></a><script> window.location = '#formJump'; </script>
</c:if>
<c:if test="${empty mealToEdit}">
    <h4><a href="meals?action=add">Добавить еду...</a></h4>
</c:if>

</body>
</html>
