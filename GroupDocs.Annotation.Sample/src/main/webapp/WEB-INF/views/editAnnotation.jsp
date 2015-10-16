<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html style="background: white">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width" />
        <title>Document annotations</title>
        <%@ include file="/WEB-INF/views/styles.jsp" %>
    </head>
    <body>
        <%@ include file="/WEB-INF/views/menuPanel.jsp" %>
        <title>Create annotation</title>
        <nav class="navbar navbar-default">
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav"><li><h2>Edit annotation</h2></li></ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${pageContext.request.contextPath}/editAnnotationReplies?fileId=${fileId}&AMP;annotationId=${annotation.getId()}">Edit annotation replies</a></li>
                    <li><a href="${pageContext.request.contextPath}/documentAnnotations?fileId=${fileId}">Back to the annotations</a></li>
                    <li><a href="${pageContext.request.contextPath}/documents">Back to the main page</a></li>
                </ul>
            </div>
        </nav>

        <form:form commandName="annotation" action="saveEditedAnnotation?fileId=${fileId}" class="form-horizontal" method="post">        
            <div class="input-group col-sm-offset-1">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="Id">Id:</label>
                        <form:input type="text" path="Id" id="Id" readonly="true"/>
                    </div>
                    <div class="form-group">
                        <label for="Type">Type:</label>
                        <form:select class="form-control" path="Type" id="Type" style="width: 300px;">
                            <option value="Text">Text</option>
                            <option value="Area">Area</option>
                            <option value="Point">Point</option>
                            <option value="TextStrikeout">Text strikeout</option>
                            <option value="Polyline">Polyline</option>
                            <option value="TextField">TextField</option>
                            <option value="Arrow">Arrow</option>
                            <option value="TextUnderline">Text Underline</option>
                            <option value="Distance">Distance</option>
                        </form:select>
                    </div>
                    <div class="form-group">
                        <label for="PositionX">PositionX:</label>
                        <form:input type="text" path="PositionX" id="PositionX"/>
                    </div>
                    <div class="form-group">
                        <label for="PositionY">PositionY:</label>
                        <form:input type="text" path="PositionY" id="PositionY"/>
                    </div>
                    <div class="form-group">
                        <label for="Width">Width:</label>
                        <form:input type="text" path="Width" id="Width"/>
                    </div>
                    <div class="form-group">
                        <label for="Height">Height:</label>
                        <form:input type="text" path="Height" id="Height"/>
                    </div>
                    <div class="form-group">
                        <label for="SvgPath">SvgPath:</label>
                        <form:input type="text" path="SvgPath" id="SvgPath"/>
                    </div>
                    <div class="form-group">
                        <label for="TextPosition">TextPosition:</label>
                        <form:input type="text" path="TextPosition" id="TextPosition"/>
                    </div>
                    <div class="form-group">
                        <label for="TextLength">TextLength:</label>
                        <form:input type="text" path="TextLength" id="TextLength"/>
                    </div>
                    <div class="form-group">
                        <label for="FieldText">FieldText:</label>
                        <form:input type="text" path="FieldText" id="FieldText"/>
                    </div>
                    <div class="form-group">
                        <label for="FontFamily">FontFamily:</label>
                        <form:input type="text" path="FontFamily" id="FontFamily"/>
                    </div>
                    <div class="form-group">
                        <label for="FontSize">FontSize:</label>
                        <form:input type="text" path="FontSize" id="FontSize"/>
                    </div>
                    <div class="form-group">
                        <label for="FontColor">FontColor:</label>
                        <form:input type="text" path="FontColor" id="FontColor"/>
                    </div>
                    <div class="form-group">
                        <label for="PageNumber">PageNumber:</label>
                        <form:input type="text" path="PageNumber" id="PageNumber"/>
                    </div>
                    <div class="form-group">
                        <label for="Row">Row:</label>
                        <form:input type="text" path="Row" id="Row" />
                    </div>
                    <div class="form-group">
                        <label for="Column">Column:</label>
                        <form:input type="text" path="Column" id="Column" />
                    </div>
                    <div class="form-group">
                        <label for="PenColor">PenColor:</label>
                        <form:input type="text" path="PenColor" id="PenColor"/>
                    </div>
                    <div class="form-group">
                        <label for="PenWidth">PenWidth:</label>
                        <form:input type="text" path="PenWidth" id="PenWidth"/>
                    </div>
                    <div class="form-group">
                        <label for="PenStyle">PenStyle:</label>
                        <form:input type="text" path="PenStyle" id="PenStyle"/>
                    </div>
                    <div class="form-group">
                        <label for="BackgroundColor">BackgroundColor:</label>
                        <form:input type="text" path="BackgroundColor" id="BackgroundColor"/>
                    </div>
                </div>
            </div>
            <div class="input-group col-sm-offset-1">
                <span class="input-group-btn">
                    <button class="btn btn-primary" type="submit" style="width: 300px; margin-top: 5px;">Save</button>
                </span>
            </div>
        </form:form>

        <footer style="background: white"></footer>
            <%@ include file="/WEB-INF/views/scripts.jsp" %>
    </body>
</html>
