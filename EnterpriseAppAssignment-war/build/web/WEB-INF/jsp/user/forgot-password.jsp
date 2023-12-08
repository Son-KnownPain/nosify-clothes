<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:main_layout>
    <jsp:attribute name="title">
        Forgot Password
    </jsp:attribute>
    <jsp:attribute name="styles">
        <!--Page CSS-->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user/forgot-password.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="module" src="${pageContext.request.contextPath}/resources/js/lit/components/nos-forgot-password.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="flx-all-center">
            <nos-forgot-password></nos-forgot-password>
        </div>
    </jsp:body>
</t:main_layout>