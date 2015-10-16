<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/WEB-INF/views/notification.jsp" %>
<nav class="navbar navbar-default">
    <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
            <li>
                <a href="${pageContext.request.contextPath}/documents">
                    <h2>GroupDocs.Annotation Demo</h2>
                </a>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right" style="margin-right: 10px; margin-top: 40px">
            <li>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        ${userLogin}<span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <c:if test="${userLogin != 'Guest'}">
                            <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                            <li class="divider"></li>
                                <c:if test="${userLogin == 'admin'}">
                                <li><a href="${pageContext.request.contextPath}/users">Users</a></li>
                                <li><a href="${pageContext.request.contextPath}/roles">Roles</a></li>
                                <li><a href="${pageContext.request.contextPath}/permissions">Permissions</a></li>
                                </c:if>
                            <li class="divider"></li>
                            </c:if>
                        <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>

                    </ul>
                </div>
            </li>
        </ul>
    </div>
</nav>
