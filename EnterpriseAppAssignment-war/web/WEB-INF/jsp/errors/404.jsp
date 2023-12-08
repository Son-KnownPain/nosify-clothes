<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:main_layout>
    <jsp:attribute name="title">
        404
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/error.css"/>
    </jsp:attribute>
    <jsp:body>
        <section class="page_404">
            <div class="container">
                <div class="row">	
                    <div class="col-sm-12 ">
                        <div class="col-sm-12 col-sm-offset-1  text-center">
                            <div class="four_zero_four_bg">
                                <h1 class="text-center ">404</h1>
                            </div>

                            <div class="contant_box_404">
                                <h3 class="h2">
                                    Look like you're lost
                                </h3>

                                <p class="p-text">the page you are looking for not avaible!</p>

                                <a href="${pageContext.request.contextPath}" class="link_404">Go to Home</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:main_layout>