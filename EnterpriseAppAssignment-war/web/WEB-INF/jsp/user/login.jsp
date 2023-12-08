<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>


<t:main_layout>
    <jsp:attribute name="title">
        Sign In
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user/sign-in.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
            Validator({
                form: '#sign-in-form',
                formGroup: '.login-form-gr',
                errorSelector: '.login-form-message',
                rules: [
                    Validator.isRequired('#email', 'Please enter your email'),
                    Validator.isEmail('#email', 'Please enter exactly email format'),
                    Validator.isRequired('#password', 'Please enter password'),
                    Validator.minLength('#password', 8, 'Enter at least 8 characters'),
                ],
                onSubmit: function(data) {
                    document.getElementById('not-verify-box').style.display = 'none';
                    showLoader();
                    userService.postSignIn(data)
                        .then(res => res.json())
                        .then(data => {
                            hideLoader();
                            if (data.success) {
                                // If success
                                document.getElementById('sign-in-form').querySelectorAll('[name]').forEach(input => input.value = '');
                                toast({
                                    title: 'Sign in successfully',
                                    message: data.message,
                                    type: 'success',
                                });
                                userProfile.fetchUserInfo();
                            }
                            else if (!data.success && data.notVerify) {
                                // If not verify
                                document.getElementById('not-verify-box').style.display = 'block';
                            } else if (!data.success && data.invalid) {
                                // If invalid
                                if (data.errors) {
                                    data.errors.forEach(err => toast({
                                        title: 'Error',
                                        message: err,
                                        type: 'error',
                                    }));
                                }
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
                    Sign In
                </h3>
                <div id="not-verify-box" style="display: none;">
                    <h5>Your email is not verified</h5>
                    <p>We sent verify email again. Please recheck and click verify!</p>
                </div>
                <form action="/" method="POST" class="login-form" id="sign-in-form">
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
                    <div style="display: flex; justify-content: right;">
                        <button class="form-btn">Sign in</button>
                    </div>
                </form>
                <div class="login-other">
                    <p class="login-text" style="text-align: center;">
                        If you don't have a user account, click <a href="${pageContext.request.contextPath}/user/sign-up" class="login-text-link">here</a> to sign up.
                    </p>
                    <p class="login-text" style="text-align: center;">
                        <a href="${pageContext.request.contextPath}/user/forgot-password" class="login-text-link login-forgot-btn">
                            Forgot password
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </jsp:body>
</t:main_layout>