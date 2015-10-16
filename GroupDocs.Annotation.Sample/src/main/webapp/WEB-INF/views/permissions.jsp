<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Users</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>

    </head>
    <body>  
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <title>Manage permissions</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li>
                        <h2>Permissions</h2>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>
        <c:if test = "${permissions == null}">
            <div class="row">
                <div class="col-md-3">There no permissions in the storage</div>
            </div>
        </c:if>
                
        <div class="row col-sm-offset-1">
            <c:forEach var="permission" items="${permissions}">
            <div class="form-group">
                <input type="text" name="Permission" id="Permission" value="${permission}" readonly />
                <a href="${pageContext.request.contextPath}/deletePermission?permission=${permission}">
                    <span class="glyphicon glyphicon-remove"></span>
                </a>
            </div>
            </c:forEach>
        </div>
        <form action="${pageContext.request.contextPath}/createPermission" class="form-horizontal" method="post">        <nav class="navbar navbar-default">
                <h2>Create new permission</h2>
            </nav>
            <div class="row col-sm-offset-1">
                <div class="form-group" style="margin-left: 0">
                    <input type="text" name="permission" valuse="" />
                    <div class="input-group">
                        <span class="input-group-btn">
                            <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Save</button>
                        </span>
                    </div>
                </div>
            </div>
        </form>

        <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>