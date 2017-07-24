<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>

    <link rel="stylesheet" type="text/css" href="css/form.css">
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meal list</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <form method="get" action="meals">
        <div class="block">
            <dl>
                <dt>From date:</dt>
                <dd><input type="date" value="${sessionScope.startDate}" name="startDate"></dd>
            </dl>
            <dl>
                <dt>To date:</dt>
                <dd><input type="date" value="${sessionScope.endDate}" name="endDate"></dd>
            </dl>
        </div>
        <div class="block">
            <dl>
                <dt>From time:</dt>
                <dd><input type="time" value="${sessionScope.startTime}" name="startTime"></dd>
            </dl>
            <dl>
                <dt>To time:</dt>
                <dd><input type="time" value="${sessionScope.endTime}" name="endTime"></dd>
            </dl>
        </div>
        <br/>

        <button type="submit">Apply filter</button>
        <button type="reset"><a href="meals?action=clear_filter">Clear filter</a></button>
    </form>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>