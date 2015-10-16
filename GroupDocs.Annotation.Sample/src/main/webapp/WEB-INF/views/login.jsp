<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Login</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>
    </head>
    <%@ include file="/WEB-INF/views/notification.jsp" %>
    <body>
        <h2 class="col-sm-offset-1">Login</h2>
        <form action="${pageContext.request.contextPath}/signin" method="post">   
            <div class="row">
                <div class="col-md-5 col-sm-offset-1">
                    <div><label for="Username">Username</label></div>
                    <div><input class="inptFld" data-val="true" data-val-required="The Username field is required." id="userid" name="login" type="text" value="" /></div>
                    <div><label for="Password">Password</label></div>
                    <div><input class="inptFld" data-val="true" data-val-required="The Password field is required." id="passwordid" name="password" type="password" /></div>
                    <button type="submit" class="btn btn-default" value="Login" style="margin-top: 10px;">Log In</button>
                    <a class="btn btn-default" href="${pageContext.request.contextPath}/register" style="margin-top: 10px;">Sign Up</a>
                </div>
            </div>
        </form>
        <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>
