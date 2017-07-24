<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Users</title>

    <link rel="stylesheet" type="text/css" href="css/form.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>
<hr/>

<jsp:useBean id="loggedUser" class="ru.javawebinar.topjava.model.User" scope="request"/>
<dl>
    <dt>Logged user name:</dt>
    <dd>${loggedUser.name}</dd>
</dl>
<dl>
    <dt>User e-mail:</dt>
    <dd>${loggedUser.email}</dd>
</dl>
</body>
</html>