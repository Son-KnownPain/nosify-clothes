<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Add User
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
            const fileInput = document.getElementById("avatarFile");
            fileInput.onchange = () => {
                const [file] = fileInput.files;
                if (file) {
                    const divPreviewer = document.querySelector('.interact-form-image-preview');
                    divPreviewer.style.display = "block";
                    divPreviewer.style.backgroundImage = "url(" + URL.createObjectURL(file) + ")";
                }
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="content-main">
            <h1 class="page-title">Add new user</h1>
            <p class="page-description">Interact with users</p>

            <div class="interact-form-box">
                <form:form class="interact-form" action="${pageContext.request.contextPath}/admin/user/store" method="POST" modelAttribute="userAdd" enctype="multipart/form-data">
                    <div class="interact-form-group">
                        <label for="avatarFile" class="interact-form-label">Avatar</label>
                        <label for="avatarFile" class="interact-form-file-label">
                            <i class="fa-solid fa-upload"></i>
                            Upload Image
                        </label>
                        <div class="interact-form-image-preview" style="display: none;"></div>
                        <input id="avatarFile" type="file" name="avatarFile" style="display: none;" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="email" class="interact-form-label">Email</form:label>
                        <form:input path="email" class="interact-form-input" />
                        <form:errors path="email" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="fullname" class="interact-form-label">Fullname</form:label>
                        <form:input path="fullname" class="interact-form-input" />
                        <form:errors path="fullname" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="password" class="interact-form-label">Password</form:label>
                        <form:password path="password" class="interact-form-input" />
                        <form:errors path="password" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="role" class="interact-form-label">Role</form:label>
                        <form:select path="role" class="interact-form-select">
                            <form:options items="${roles}" />
                        </form:select>
                        <form:errors path="role" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <button class="interact-form-submit-btn">
                            Create
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:admin_layout>