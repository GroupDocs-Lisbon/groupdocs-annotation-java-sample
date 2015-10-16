<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <title>Manage users</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li>
                        <h2>Users</h2>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>

        <c:forEach var = "user" items="${users}">
            <form action="${pageContext.request.contextPath}/addUserRole?userId=${user.getId()}" enctype="multipart/form-data" method="post">          
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="UserId">Id:</label>
                            <input type="text" name="Id" id="UserId" value="${user.getId()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="UserName">Name:</label>
                            <input type="text" name="Name" id="UserName" value="${user.getName()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="Login">Login:</label>
                            <input type="text" name="Login" id="Login" value="${user.getLogin()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="Password">Password:</label>
                            <input type="text" name="Password" id="Password"value="${user.getPassword()}" readonly />
                        </div>
                        <div class="form-group">
                            <label>Roles:</label>
                            <c:forEach var="userRole" items="${user.getRoles()}">
                                <div class="input-group col-md-6" style="width: 300px">
                                    <input type="text" class="form-control" value="${userRole}"/>
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" type="button" onclick="location.href = '${pageContext.request.contextPath}/deleteUserRole?roleName=${userRole}&amp;userId=${user.getId()}'">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </span>
                                </div>
                            </c:forEach> 
                        </div>
                        <div class="form-group">
                            <label for="roleName">Add:</label>
                            <select class="form-control" name="roleName" id="roleName" style="width: 300px">
                                <c:forEach var="role" items="${roles}">
                                    <option value="${role.getName()}">
                                        ${role.getName()}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn col-md-5">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Add role</button>
                    </span>
                </div>
            </form>        
            <hr />
        </c:forEach>

        <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>