<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="margin-left: 20px; opacity: 0.9">
    <button class="btn btn-default dropdown-toggle" type="button" id="menu1" data-toggle="dropdown">
        ${pageContext.response.locale}
        <span class="caret"></span></button>
    <ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
        <li role="presentation"><a role="menuitem" tabindex="-1" href="?locale=ru">Русский</a></li>
        <li role="presentation"><a role="menuitem" tabindex="-1" href="?locale=en">English</a></li>
    </ul>
</div>
