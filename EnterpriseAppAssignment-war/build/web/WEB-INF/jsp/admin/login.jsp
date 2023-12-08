<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <style>
            body {
                background-color: #444;
                color: #fff;
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .form-group {
                margin-top: 12px;
            }
            .form-label {
                display: block;
            }
            .form-input {
                padding: 4px 6px;
                outline: none;
                width: 200px;
            }
            .form-errors {
                color: #ff4444;
                display: block;
            }
            .form-submit-btn {
                outline: none;
                margin-top: 12px;
                position: relative;
                left: 50%;
                transform: translateX(-50%)
            }
        </style>
    </head>
    <body>
        <form:form action="login-check" method="POST" modelAttribute="accountLogin">
            <div class="form-group">
                <form:label class="form-label" path="email">Email</form:label>
                <form:input class="form-input" path="email" />
                <form:errors class="form-errors" path="email" />
            </div>
            <div class="form-group">
                <form:label class="form-label" path="password">Password</form:label>
                <form:password class="form-input" path="password" />
                <form:errors class="form-errors" path="password" />
            </div>
            <button class="form-submit-btn">Login</button>
        </form:form>
    </body>
</html>
