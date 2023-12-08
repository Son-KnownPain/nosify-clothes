<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Categories
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Categories Management</h1>
            <p class="page-description">Interact with categories</p>

            <div class="interact-box">
                <div class="interact-actions">
                    <a href="${pageContext.request.contextPath}/admin/category/add" class="interact-btn interact-add">Add</a>
                </div>

                <c:choose>
                    <c:when test="${categories.size() gt 0}">
                        <table class="interact-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${categories}" var="category">
                                    <tr>
                                        <td>${category.categoryID}</td>
                                        <td>${category.name}</td>
                                        <td>${category.description}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/category/edit/${category.categoryID}" class="interact-btn interact-edit">
                                                <i class="fa-solid fa-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/category/delete/${category.categoryID}" class="interact-btn interact-delete">
                                                <i class="fa-regular fa-trash-can"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="table-no-data-text">No data here. You can add the first data by clicking the add button!</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>