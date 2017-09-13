<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <h3><spring:message code="meal.title"/></h3>


            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8 panel panel-body panel-default">
                    <form method="post" action="meals/filter" class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="startDate"><spring:message
                                    code="meal.startDate"/>:</label>
                            <div class="col-sm-4">
                                <input class="form-control" type="date" name="startDate" id="startDate"
                                       value="${param.startDate}">
                            </div>

                            <label class="control-label col-sm-3" for="startTime"><spring:message
                                    code="meal.startTime"/>:</label>
                            <div class="col-sm-2">
                                <input class="form-control" type="time" name="startTime" id="startTime"
                                       value="${param.startTime}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="endDate"><spring:message
                                    code="meal.endDate"/>:</label>
                            <div class="col-sm-4">
                                <input class="form-control" id="endDate" type="date" name="endDate"
                                       value="${param.endDate}">
                            </div>

                            <label class="control-label col-sm-3" for="endTime"><spring:message
                                    code="meal.endTime"/>:</label>
                            <div class="col-sm-2">
                                <input class="form-control" id="endTime" type="time" name="endTime"
                                       value="${param.endTime}">
                            </div>
                        </div>
                        <button type="submit"><spring:message code="meal.filter"/></button>
                    </form>
                </div>
                <div class="col-sm-2"></div>
            </div>

            <div class="row text-center">
                <a onclick="add()" class="btn btn-primary"><spring:message code="meal.add"/></a>
            </div>

            <table class="table table-striped display" id="datatable">
                <thead>
                <tr>
                    <th><spring:message code="meal.dateTime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                    <%--<th colspan="2"><spring:message code="common.actions"/></th>--%>
                </tr>
                </thead>
                <c:forEach items="${meals}" var="meal">
                    <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                    <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                        <td>
                                <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                                <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                                ${fn:formatDateTime(meal.dateTime)}
                        </td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                        <td><a><span class="glyphicon glyphicon-remove delete" aria-hidden="true"
                                     id="${meal.id}"></span></a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><spring:message code="meal.add"/></h2>
                <form method="post" id="detailsForm" class="form-horizontal">
                    <input value="" name="id" hidden title=""/>
                    <div class="form-group text-center">
                        <label class="control-label col-sm-3" for="dateTime"><spring:message
                                code="meal.dateTime"/>:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="datetime-local" name="dateTime" id="dateTime"/>
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <label class="control-label col-sm-3" for="description"><spring:message
                                code="meal.description"/>:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="text" name="description" id="description"/>
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <label class="control-label col-sm-3" for="calories"><spring:message
                                code="meal.calories"/>:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="number" name="calories" id="calories"/>
                        </div>
                    </div>
                    <div class="row text-center">
                        <button type="submit" class="btn btn-primary"><spring:message code="common.save"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>