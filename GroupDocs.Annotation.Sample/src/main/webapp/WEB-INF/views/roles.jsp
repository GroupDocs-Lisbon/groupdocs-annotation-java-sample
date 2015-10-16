<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>Manage roles</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li>
                        <h2>Roles</h2>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>
        <c:if test = "${permissions == null}">
            <div class="row">
                <div class="col-md-3">There no roles in the storage</div>
            </div>
        </c:if>

        <c:forEach var="role" items="${roles}">
            <form action="${pageContext.request.contextPath}/addRolePermission?roleName=${role.getName()}" class="form-horizontal" method="post">       
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="Id">Name:</label>
                            <input type="text" name="Id" id="Id" value="${role.getName()}" readonly />
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label>Permissions:</label>
                            <c:forEach var="permission" items="${role.getPermissions()}">
                                <div class="input-group col-md-6" style="width: 300px">
                                    <input type="text" class="form-control" value="${permission}">
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" type="button" onclick="location.href = '${pageContext.request.contextPath}/deleteRolePermission?roleName=${role.getName()}&amp;deletedPermission=${permission}'">
                                            <span class=" glyphicon glyphicon-remove"> </span></button>
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="newPermission">Add:</label>
                            <select class="form-control" name="newPermission" id="newPermission" style="width: 300px">
                                <c:forEach var="existingPermission" items="${permissions}">
                                    <option value = "${existingPermission}">
                                        ${existingPermission}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Save</button>
                    </span>
                </div>
            </form>  
            <form action="${pageContext.request.contextPath}/deleteRole?role=${role.getName()}" class="form-horizontal" method="post">          
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 20px">Delete</button>
                    </span>
                </div>
            </form>     
            <hr />
        </c:forEach>



        <form action="${pageContext.request.contextPath}/createRole" class="form-horizontal" method="post">   
            <nav class="navbar navbar-default">
                <h2>Create new role</h2>
            </nav>
            <div class="input-group col-sm-offset-1">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="Name" class="col-sm-2 control-label">Name:</label>
                        <div class="col-sm-10">
                            <input type="text" name="newRole" id="Name" value="" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="input-group col-sm-offset-1">
                <span class="input-group-btn col-md-5">
                    <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Create</button>
                </span>
            </div>
        </form>
        <br>

        <%@ include file="/WEB-INF/views/scripts.jsp" %>

    </body>
</html>