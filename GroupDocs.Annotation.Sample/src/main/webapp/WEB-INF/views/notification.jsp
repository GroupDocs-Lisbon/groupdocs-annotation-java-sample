<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test = "${Notification != null}">
    <div id="NotificationBox" class="${NotificationCSS}" style="display: none; position: absolute">
        ${Notification}
    </div>
</c:if>