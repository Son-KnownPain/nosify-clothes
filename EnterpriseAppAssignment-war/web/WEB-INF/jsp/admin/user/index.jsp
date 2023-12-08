<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:admin_layout>
    <jsp:attribute name="title">
        Users
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Users Management</h1>
            <p class="page-description">Interact with users</p>

            <div class="interact-box">
                <div class="interact-actions">
                    <a href="${pageContext.request.contextPath}/admin/user/add" class="interact-btn interact-add">Add</a>
                    <form class="interact-search-form" action="${pageContext.request.contextPath}/admin/user" method="GET">
                        <input class="interact-search-input" type="text" name="q" placeholder="Enter name of user">
                        <button class="interact-search-btn">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </button>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${data.size() gt 0}">
                        <table class="interact-table">
                            <thead>
                                <tr>
                                    <th>Avatar</th>
                                    <th>Fullname</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${data}" var="dataItem">
                                    <tr>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/resources/images/user/${dataItem.avatar}" alt="Avatar" width="100px">
                                        </td>
                                        <td>${dataItem.fullname}</td>
                                        <td>${dataItem.email}</td>
                                        <td>${dataItem.role}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/user/edit/${dataItem.userID}" class="interact-btn interact-edit">
                                                <i class="fa-solid fa-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/user/delete/${dataItem.userID}" class="interact-btn interact-delete">
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