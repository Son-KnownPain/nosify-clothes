<%@tag description="Main layout" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="styles" fragment="true" %>
<%@attribute name="scripts" fragment="true" %>
<%@attribute name="title" fragment="true" %>

<%-- any content can be specified here e.g.: --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="my-app-name" content="${pageContext.request.contextPath}">
    <title><jsp:invoke fragment="title" /> | Admin</title>
    <%@include file="../../../admin-resources/includes/includes-css.jsp" %>
    <jsp:invoke fragment="styles" />
</head>
<body>
    <div id="app">
        <%@include file="../../../admin-resources/partials/header.jsp" %>
        
        <div id="content">
            <%@include file="../../../admin-resources/partials/sidebar.jsp" %>
            <div id="layout-content">
                <jsp:doBody />
                <%--<%@include file="../../../admin-resources/partials/footer.jsp" %>--%>
            </div>
        </div>
        
        
    </div>
    
    <%@include file="../../../admin-resources/includes/includes-script.jsp" %>
    <jsp:invoke fragment="scripts" />
</body>
</html>