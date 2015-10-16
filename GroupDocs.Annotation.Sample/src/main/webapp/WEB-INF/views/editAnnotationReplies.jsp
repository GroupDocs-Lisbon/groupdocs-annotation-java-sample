<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Replies</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>

    </head>
    <body>
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <title>Annotation replies</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav"><li><h2>Annotation replies</h2></li></ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documentAnnotations?fileId=${fileId}">Back to the annotations</a></li>
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>
        <c:forEach var="reply" items="${replies}">
            <form action="${pageContext.request.contextPath}/editAnnotationReply?fileId=${fileId}&amp;annotationId=${annotationId}" class="form-horizontal" method="post">         
                <div class="input-group col-sm-offset-1">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label for="Id">Reply Id:</label>
                            <input type="text" Name="Id" id="Id" readonly="true" value="${reply.getId()}" />
                        </div>
                        <div class="form-group">
                            <label for="User">User:</label>
                            <input type="text" Name="User" id="User" value="${reply.getUser()}" readonly="true"/>
                        </div>
                        <div class="form-group">
                            <label for="Message">Message:</label>
                            <input type="text" Name="Message" id="Message"value="${reply.getMessage()}" />
                        </div>
                        <div class="form-group">
                            <label for="ParentReplyId">ParentReplyId:</label>
                            <input type="text" Name="ParentReplyId" id="ParentReplyId" readonly="true" value="${reply.getParentReplyId()}"/>
                        </div>
                    </div>
                </div>
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Save</button>
                    </span>
                </div>
            </form>
            <form action="${pageContext.request.contextPath}/deleteAnnotationReply?fileId=${fileId}&amp;annotationId=${annotationId}&amp;replyId=${reply.getId()}" class="form-horizontal" method="post">       
                <div class="input-group col-sm-offset-1">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 10px; ">Delete</button>
                    </span>
                </div>
            </form>
        </c:forEach>

        <form:form commandName="newReply" action="${pageContext.request.contextPath}/createNewAnnotationReply?fileId=${fileId}&annotationId=${annotationId}" class="form-horizontal" method="post">      
            <nav class="navbar navbar-default">
                <h2>Create new annotation reply</h2>
            </nav>
            <div class="input-group col-sm-offset-1">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="Message">Message:</label>
                        <form:input type="text" path="Message" id="Message" />
                    </div>
                    <div class="form-group">
                        <label for="ParentReplyId">ParentReplyId:</label>
                        <form:input type="text" path="ParentReplyId" id="ParentReplyId" />
                    </div>

                </div>
            </div>
            <div class="input-group col-sm-offset-1">
                <span class="input-group-btn">
                    <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px; margin-bottom: 10px; ">Add</button>
                </span>
            </div>
        </form:form>

        <footer style="background: white"></footer>
        <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>