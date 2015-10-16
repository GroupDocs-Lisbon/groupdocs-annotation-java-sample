<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="background: white">
    <head>
        <title>Document annotations</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>

    </head>
    <body>
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav"><li>
                        <h2>"${documentName}"</h2></li></ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/documentAnnotations?fileId=${fileId}">Back to the annotations</a></li>
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>

        <c:forEach var="annotation" items="${annotations}">    
            <div class="row">
                <div class="col-md-8 col-sm-offset-2">
                    <textarea rows="30" style="width: 100%" readonly>
                        <c:out value="${annotation}"></c:out>
                    </textarea>
                </div>
                <br />
            </div>
    </c:forEach>


        <footer style="background: white"></footer>
            <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>
