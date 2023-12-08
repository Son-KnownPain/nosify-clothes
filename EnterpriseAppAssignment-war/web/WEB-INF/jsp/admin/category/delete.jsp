<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Delete Category
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Delete Category</h1>
            <p class="page-description">Interact with categories</p>

            <div class="interact-form-delete">
                <p class="interact-delete-ask">
                    Are you sure you want to delete this. <br>
                    Deleting it may cause some other data to be lost as well.
                </p>
                
                <form:form action="${pageContext.request.contextPath}/admin/category/delete" method="POST" modelAttribute="categoryDelete">
                    <input type="hidden" name="_method" value="DELETE" />
                    <form:hidden path="categoryID" />
                    <a href="${pageContext.request.contextPath}/admin/category" class="interact-form-cancel-btn">Cancel</a>
                    <button class="interact-form-delete-btn">
                        Delete
                    </button>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>