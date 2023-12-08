<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:main_layout>
    <jsp:attribute name="title">
        Home
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home/index.css">
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <!-- Lit Element Import -->
        <script type="module" src="${pageContext.request.contextPath}/resources/js/lit/components/home-page/nos-home-page.js"></script>
        <script type="module" src="${pageContext.request.contextPath}/resources/js/lit/components/home-page/nos-category-home-page.js"></script>
        <script type="module" src="${pageContext.request.contextPath}/resources/js/lit/components/home-page/nos-cate-product-item.js"></script>
    </jsp:attribute>
    <jsp:body>
        <nos-home-page></nos-home-page>
    </jsp:body>
</t:main_layout>