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
        <title>Manage collaborators</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li>
                        <h2>Document Collaborators</h2>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>

        <c:if test = "${collaborators == null}">
            <div class="row">
                <div class="col-md-3">The document has no collaborators.</div>
            </div>
        </c:if>

        <c:forEach var="collaborator" items="${collaborators}">
            <form action="${pageContext.request.contextPath}/editDocumentCollaborator?fileId=${fileId}" class="form-horizontal" method="post">          
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="UserId">User Id:</label>
                            <input type="text" name="UserId" id="UserId" value="${collaborator.getValue().getUserId()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="UserName">User login:</label>
                            <input type="text" id="UserName" value="${collaborator.getKey()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="Id">Collaborator Id:</label>
                            <input type="text" name="Id" id="Id" value="${collaborator.getValue().getId()}" readonly />
                        </div>
                        <div class="form-group">
                            <label for="Role">Role:</label>
                            <select name="Role" id="Role" class="form-control" style="width: 300px">
                                <c:forEach var="role" items="${roles}">
                                    <c:choose>
                                        <c:when test="${role.getName() == collaborator.getValue().getRole()}">
                                            <option selected="selected" value="${role.getName()}">
                                                ${role.getName()}
                                            </option>
                                        </c:when>    
                                        <c:otherwise>
                                            <option value="${role.getName()}">
                                                ${role.getName()}
                                            </option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 5px">Save</button>
                    </span>
                </div>
            </form>
            <form action="${pageContext.request.contextPath}/deleteDocumentCollaborator?fileId=${fileId}&amp;collaboratorId=${collaborator.getValue().getId()}" class="form-horizontal" method="post">     
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 20px">Delete</button>
                    </span>
                </div>
            </form>    
        </c:forEach>
        <hr/>

        <form action="${pageContext.request.contextPath}/addDocumentCollaborator?fileId=${fileId}" class="form-horizontal" method="post">        <nav class="navbar navbar-default">
                <h2>Add new document collaborator</h2>
            </nav>
            <div class="input-group col-sm-offset-1">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="UserId" class="col-sm-2 control-label">User:</label>
                        <select name="UserId" id="UserId" class="form-control" style="width: 300px">
                            <c:forEach var="user" items="${users}">
                                <option value="${user.getId()}">
                                    ${user.getName()} ${user.getId()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="Role" class="col-sm-2 control-label">Role:</label>
                        <select name="Role" id="Role" class="form-control" style="width: 300px">
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
                <span class="input-group-btn">
                    <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 20px">Add</button>
                </span>
            </div>
        </form>

        <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>