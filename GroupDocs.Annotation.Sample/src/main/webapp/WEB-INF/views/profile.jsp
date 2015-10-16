<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Profile</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>
    </head>
    <body>
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <title>Profile</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav"><li><h2>Profile</h2></li></ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>
        <div class="row">
            <form action="${pageContext.request.contextPath}/editProfile" class="form-horizontal" method="post">          
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="Id">User Id:</label>
                            <input type="text" readonly name="Id" id="Id" value="${user.getId()}" />
                        </div>
                        <div class="form-group">
                            <label for="Login">Login:</label>
                            <input type="text" name="Login" id="Login" value="${user.getLogin()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="Name">User name:</label>
                            <input type="text" name="Name" id="Name" value="${user.getName()}" />
                        </div>
                        <div class="form-group">
                            <label for="Password">Password:</label>
                            <input type="text" name="Password" id="Password" value="${user.getPassword()}" />
                        </div>
                        <div class="form-group">
                            <label>Roles:</label>
                            <c:forEach var="role" items="${roles}">
                                <input type="text" name="Roles" value="${role}" readonly /><br>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 20px">Save</button>
                    </span>
                </div>
            </form>   
        </div>

        <footer style="background: white"></footer>
            <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>