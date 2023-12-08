<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Index
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/index.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="index-page">
            <i class="fa-solid fa-hammer"></i>
            <h1>Welcome to Nosify Clothes admin page</h1>
            <p>As a adminstrator you can view, add, edit, delete data here.</p>
        </div>
    </jsp:body>
</t:admin_layout>