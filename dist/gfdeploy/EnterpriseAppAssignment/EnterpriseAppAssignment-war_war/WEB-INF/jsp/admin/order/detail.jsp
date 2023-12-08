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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/order/detail.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
            function copyID() {
                // Get the text field
                var copyText = document.getElementById("toCopy");

                // Select the text field
                copyText.select();
                copyText.setSelectionRange(0, 99999); // For mobile devices

                // Copy the text inside the text field
                navigator.clipboard.writeText(copyText.value);

                toast({
                    title: 'Copy',
                    message: 'Successfully copy order id',
                    type: 'info',
                    duration: 1000,
                })
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Order Detail</h1>
            <p class="page-description">View information detail of order</p>

            <div class="order-detail">
                <div class="info">
                    <div class="row">
                        <div class="col-6">
                            <h3 class="strong-line">
                                <i class="fa-solid fa-hashtag"></i>
                                ID: ${order.orderID}
                                <input style="display: none;" id="toCopy" value="${order.orderID}">
                                <span onclick="copyID()" style="cursor: pointer;">
                                    <i class="fa-regular fa-copy"></i>
                                </span>
                            </h3>
                        </div>
                        <div class="col-6">
                            <h3 class="strong-line">
                                <i class="fa-regular fa-user"></i>
                                Customer: ${order.userID.fullname}
                            </h3>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <i class="fa-solid fa-phone"></i>
                                Phone: ${order.phone}
                            </p>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <i class="fa-solid fa-location-dot"></i>
                                Address: ${order.address}
                            </p>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <i class="fa-regular fa-calendar"></i>
                                Order Date: ${order.orderDate}
                            </p>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <i class="fa-regular fa-calendar"></i>
                                Deliveried Date: ${order.deliveriedDate}
                            </p>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <i class="fa-solid fa-toggle-off"></i>
                                Status: ${order.status}
                            </p>
                        </div>
                        <div class="col-6">
                            <p class="line">
                                <fmt:formatNumber var="priceFormatted" value="${order.totalPrice}" type="number" pattern="###,###" />
                                <c:set var="perfectPriceFormatted" value="${fn:replace(priceFormatted, ',', '.')}"/>
                                <i class="fa-solid fa-money-bills"></i>
                                Total price: ${perfectPriceFormatted} VNĐ
                            </p>
                        </div>
                    </div>
                </div>

                <div class="products">
                    <div class="row">
                        <c:forEach items="${order.productsOfOrder}" var="item">
                            <div class="col-6">
                                <div class="product-item">
                                    <div class="row">
                                        <div class="col-3">
                                            <img width="100%" src="${pageContext.request.contextPath}/resources/images/product/${item.thumbnail}" alt="Product" />
                                        </div>
                                        <div class="col-9">
                                            <div class="product-body">
                                                <h4 class="product-item--name">
                                                    ${item.name}
                                                </h4>
                                                <p class="product-item--quantity">
                                                    <span>${item.orderQuantity}</span>
                                                </p>
                                                <p class="product-item--unitPrice">
                                                    <fmt:formatNumber var="priceFormatted1" value="${item.orderUnitPrice}" type="number" pattern="###,###" />
                                                    <c:set var="perfectPriceFormatted1" value="${fn:replace(priceFormatted1, ',', '.')}"/>
                                                    ${perfectPriceFormatted1} VNĐ
                                                </p>
                                                <p class="product-item--totalPrice">
                                                    <fmt:formatNumber var="priceFormatted2" value="${item.orderUnitPrice * item.orderQuantity}" type="number" pattern="###,###" />
                                                    <c:set var="perfectPriceFormatted2" value="${fn:replace(priceFormatted2, ',', '.')}"/>
                                                    ${perfectPriceFormatted2} VNĐ
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>