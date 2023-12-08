<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Edit Product
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Edit order</h1>
            <p class="page-description">Interact with orders</p>

            <div class="interact-form-box">
                <form:form class="interact-form" action="${pageContext.request.contextPath}/admin/order/update" method="POST" modelAttribute="orderEdit">
                    <input type="hidden" name="_method" value="PUT" />
                    <form:hidden path="orderID" />
                    <div class="interact-form-group">
                        <form:label path="status" class="interact-form-label">Choose Status</form:label>
                        <form:select path="status" class="interact-form-select">
                            <form:options items="${statusOptions}" itemValue="key" itemLabel="value" />
                        </form:select>
                        <form:errors path="status" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <button class="interact-form-submit-btn">
                            Update
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>