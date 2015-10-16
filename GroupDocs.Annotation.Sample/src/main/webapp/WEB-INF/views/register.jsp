<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Sign Up</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>

    </head>
    <body>
        <%@ include file="/WEB-INF/views/notification.jsp" %>
        <h2 class="col-sm-offset-1">Sign Up</h2>

        <form action="${pageContext.request.contextPath}/signup" method="post">    
            <div class="row">
                <div class="col-md-5 col-sm-offset-1">
                    <div><label for="Username">Username</label></div>
                    <div><input class="inptFld" data-val="true" data-val-required="The Username field is required." id="userid" name="login" type="text" value="" required /></div>

                    <div><label for="Password">Password</label></div>
                    <div><input class="inptFld" data-val="true" data-val-required="The Password field is required." id="passowdid" name="password" type="password" required /></div>

                    <button type="submit" class="btn btn-default" value="Login" style="margin-top: 10px;">Sign Up</button>
                </div>

            </div>
        </form>
        <%@ include file="/WEB-INF/views/scripts.jsp" %>

    </body>
</html>