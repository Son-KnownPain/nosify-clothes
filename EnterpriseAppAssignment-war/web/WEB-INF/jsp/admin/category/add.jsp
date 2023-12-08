<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Add Category
    </jsp:attribute>
    <jsp:attribute name="styles">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/form.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin/css/common.css" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script>
            const fileInput = document.getElementById("thumbnailFile");
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
            <h1 class="page-title">Add new category</h1>
            <p class="page-description">Interact with categories</p>

            <div class="interact-form-box">
                <form:form class="interact-form" action="${pageContext.request.contextPath}/admin/category/store" method="POST" modelAttribute="categoryAdd" enctype="multipart/form-data">
                    <div class="interact-form-group">
                        <form:label path="thumbnail" class="interact-form-label">Thumbnail</form:label>
                        <label for="thumbnailFile" class="interact-form-file-label">
                            <i class="fa-solid fa-upload"></i>
                            Upload Image
                        </label>
                        <div class="interact-form-image-preview" style="display: none;"></div>
                        <input id="thumbnailFile" type="file" name="thumbnailFile" style="display: none;" />
                        <form:errors path="thumbnail" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="name" class="interact-form-label">Category name</form:label>
                        <form:input path="name" class="interact-form-input" />
                        <form:errors path="name" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="description" class="interact-form-label">Description</form:label>
                        <form:textarea path="description" class="interact-form-textarea" rows="10" />
                        <form:errors path="description" class="interact-form-error" />
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