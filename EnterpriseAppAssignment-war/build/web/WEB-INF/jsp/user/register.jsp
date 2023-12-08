<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:main_layout>
    <jsp:attribute name="title">
        Sign Up
    </jsp:attribute>
    <jsp:attribute name="styles">
        <!--Page CSS-->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user/sign-in.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
            Validator({
                form: '#sign-up-form',
                formGroup: '.login-form-gr',
                errorSelector: '.login-form-message',
                rules: [
                    Validator.isRequired('#email', 'Please enter your email'),
                    Validator.isEmail('#email', 'Please enter exactly email format'),
                    Validator.isRequired('#password', 'Please enter password'),
                    Validator.minLength('#password', 8, 'Enter at least 8 characters'),
                    Validator.isRequired('#passwordConfirm', 'Please enter password confirm'),
                    Validator.isConfirmed('#passwordConfirm', () => document.getElementById('password').value, 'Password confirm is not match'),
                ],
                onSubmit: function(data) {
                    document.getElementById('sign-up-annouce').style.display = 'none';
                    showLoader();
                    userService.postSignUp(data)
                        .then(res => res.json())
                        .then(data => {
                            hideLoader();
                            if (data.success) {
                                document.getElementById('sign-up-annouce').style.display = 'block';
                                document.getElementById('sign-up-form').querySelectorAll('[name]').forEach(input => input.value = '');
                            } else if (!data.success && data.invalid) {
                                if (data.errors) {
                                    data.errors.forEach(err => toast({
                                        title: 'Error',
                                        message: err,
                                        type: 'error',
                                    }));
                                }
                            } else {
                                console.log(data);
                            }
                        })
                }
            });
            passwordEye(
                {
                    idSelectors: [],
                    wrapperSelector: '.password-eye-wrapper',
                }
            );
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="flx-all-center">
            <div id="login-container">
                <h3 class="title no-mb">
                    Sign Up
                </h3>
                <h4 id="sign-up-annouce" style="display: none;">
                    <i class="fa-solid fa-circle-check"></i>
                    Create new account success!
                    <br>
                    <p style="color: #333; font-size: 1.4rem; margin-top: 8px;;">
                        <i class="fa-solid fa-envelope"></i>
                        Please check your email and click verify!
                    </p>
                </h4>
                <form action="/" method="POST" class="login-form" id="sign-up-form">
                    <div class="login-form-gr">
                        <label for="email" class="login-form-label">Email</label>
                        <input name="email" id="email" type="email" class="login-form-input">
                        <span class="login-form-message"></span>
                    </div>
                    <div class="login-form-gr">
                        <label for="password" class="login-form-label">Password</label>
                        <div class="password-eye-wrapper">
                            <input name="password" id="password" type="password" class="login-form-input">
                        </div>
                        <span class="login-form-message"></span>
                    </div>
                    <div class="login-form-gr">
                        <label for="passwordConfirm" class="login-form-label">Confirm Password</label>
                        <div class="password-eye-wrapper">
                            <input name="passwordConfirm" id="passwordConfirm" type="password" class="login-form-input">
                        </div>
                        <span class="login-form-message"></span>
                    </div>
                    <div style="display: flex; justify-content: right;">
                        <button class="form-btn">Sign up</button>
                    </div>
                </form>
                <div class="login-other">
                    <p class="login-text" style="text-align: center;">
                        If you have a user account, click <a href="${pageContext.request.contextPath}/user/sign-in" class="login-text-link">here</a> to sign in.
                    </p>
                </div>
            </div>
        </div>
    </jsp:body>
</t:main_layout>