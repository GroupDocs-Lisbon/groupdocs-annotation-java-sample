<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Index</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>

    </head>
    <body>
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <div class="panel panel-default">
            <table class="table table-striped table-hover">
                <tr>
                    <th>Name</th>
                    <th>Document id</th>
                </tr>
                <c:forEach var="document" items = "${documents}">
                    <tr>
                        <td>
                            <c:out value = "${document.getName()}"  />
                        </td>
                        <td><c:out value = "${document.getDocumentId()}"  />
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/getPdfVersionOfDocument?fileId=${document.getDocumentId()}" enctype="multipart/form-data" method="post">
                                <button class="btn btn-default  btn-annotation" type="submit">Get Clear Document</button>
                            </form>                    
                            <form action="${pageContext.request.contextPath}/getAnnotatedDocument?fileId=${document.getDocumentId()}" enctype="multipart/form-data" method="post">                        
                                <button class="btn btn-default  btn-annotation" type="submit">Get Annotated Document</button>
                            </form>                
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/deleteDocument?fileId=${document.getDocumentId()}" enctype="multipart/form-data" method="post">                        
                                <button class="btn btn-default  btn-annotation" type="submit">Delete</button>
                            </form>                
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/documentAnnotations?fileId=${document.getDocumentId()}" enctype="multipart/form-data" method="post">                       
                                <button class="btn btn-default btn-annotation" type="submit"><i class="icon-"></i>Annotations</button>
                            </form>               
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/collaborators?fileId=${document.getDocumentId()}" enctype="multipart/form-data" method="post">                      
                                <button class="btn btn-default btn-annotation" type="submit"><i class="icon-"></i>Manage collaborators</button>
                            </form>                
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <form action="${pageContext.request.contextPath}/uploadFile" enctype="multipart/form-data" method="post">        
                <input type="file" class="file fileinput" name="file" data-show-preview="false"/>
            </form>
        </div>

        <footer style="background: white"></footer>
       <%@ include file="/WEB-INF/views/scripts.jsp" %>
        <script src="${pageContext.request.contextPath}/resources/fileinput.js"></script>
        <script src="${pageContext.request.contextPath}/resources/fileinput_locale_LANG.js"></script>
    </body>
</html>
