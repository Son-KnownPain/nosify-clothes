<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:admin_layout>
    <jsp:attribute name="title">
        Orders
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/order/index.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Orders Management</h1>
            <p class="page-description">Interact with orders</p>

            <div class="interact-box">
                <div class="interact-actions">
                    <!-- <a href="${pageContext.request.contextPath}/admin/order/add" class="interact-btn interact-add">Add</a> -->
                    <form class="interact-search-form" action="${pageContext.request.contextPath}/admin/order" method="GET">
                        <input class="interact-search-input" type="text" name="q" placeholder="Enter id of order">
                        <button class="interact-search-btn">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </button>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${data.size() gt 0}">
                        <div class="orders">
                            <c:forEach items="${data}" var="dataItem">
                                <div class="order-item">
                                    <div class="row">
                                        <div class="col-6">
                                            <h3 class="title">
                                                <i class="fa-solid fa-hashtag"></i>
                                                ID: ${dataItem.orderID}
                                            </h3>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-regular fa-user"></i>
                                                Customer: ${dataItem.userID.fullname}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-solid fa-phone"></i>
                                                Phone: ${dataItem.phone}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-solid fa-location-dot"></i>
                                                Address: ${dataItem.address}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-regular fa-calendar"></i>
                                                Order Date: ${dataItem.orderDate}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-regular fa-calendar"></i>
                                                Deliveried Date: ${dataItem.deliveriedDate}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <i class="fa-solid fa-toggle-off"></i>
                                                Status: ${dataItem.status}
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="line">
                                                <fmt:formatNumber var="priceFormatted" value="${dataItem.totalPrice}" type="number" pattern="###,###" />
                                                <c:set var="perfectPriceFormatted" value="${fn:replace(priceFormatted, ',', '.')}"/>
                                                <i class="fa-solid fa-money-bills"></i>
                                                Total price: ${perfectPriceFormatted} VNĐ
                                            </p>
                                        </div>

                                        <div class="col-12">
                                            <div class="actions">
                                                <a href="${pageContext.request.contextPath}/admin/order/edit/${dataItem.orderID}" class="action" style="color: #ffa200;">
                                                    <i class="fa-solid fa-pencil"></i>
                                                    Update Status
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/order/delete/${dataItem.orderID}" class="action" style="color: #c00000;">
                                                    <i class="fa-regular fa-trash-can"></i>
                                                    Delete
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/order/detail/${dataItem.orderID}" class="action" style="color: #0066ff;">
                                                    <i class="fa-solid fa-circle-info"></i>
                                                    Detail
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <!-- <table class="interact-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Customer</th>
                                    <th>Phone</th>
                                    <th>Status</th>
                                    <th>Order Date</th>
                                    <th>Deliveried Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${data}" var="dataItem">
                                    <tr>
                                        <td>${dataItem.orderID}</td>
                                        <td>${dataItem.userID.fullname}</td>
                                        <td>${dataItem.phone}</td>
                                        <td>${dataItem.status}</td>
                                        <td>${dataItem.orderDate}</td>
                                        <td>${dataItem.deliveriedDate}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/order/edit/${dataItem.orderID}" class="interact-btn interact-edit">
                                                <i class="fa-solid fa-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/order/delete/${dataItem.orderID}" class="interact-btn interact-delete">
                                                <i class="fa-regular fa-trash-can"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/order/detail/${dataItem.orderID}" class="interact-btn interact-delete">
                                                <i class="fa-solid fa-circle-info"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> -->
                    </c:when>
                    <c:otherwise>
                        <p class="table-no-data-text">No data here. You can add the first data by clicking the add button!</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>