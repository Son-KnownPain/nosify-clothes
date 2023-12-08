<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/layouts/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:admin_layout>
    <jsp:attribute name="title">
        Add Product
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
            <h1 class="page-title">Add new product</h1>
            <p class="page-description">Interact with products</p>

            <div class="interact-form-box">
                <form:form class="interact-form" action="${pageContext.request.contextPath}/admin/product/store" method="POST" modelAttribute="productAdd" enctype="multipart/form-data">
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
                        <form:label path="name" class="interact-form-label">Product name</form:label>
                        <form:input path="name" class="interact-form-input" />
                        <form:errors path="name" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="categoryID" class="interact-form-label">Category</form:label>
                        <form:select path="categoryID" class="interact-form-select">
                            <form:options items="${categoriesSelect}" itemValue="categoryID" itemLabel="name" />
                        </form:select>
                        <form:errors path="categoryID" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="price" class="interact-form-label">Price (VND)</form:label>
                        <form:input path="price" class="interact-form-input" />
                        <form:errors path="price" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="discount" class="interact-form-label">Discount (0 - 100) %</form:label>
                        <form:input path="discount" class="interact-form-input" />
                        <form:errors path="discount" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="description" class="interact-form-label">Description</form:label>
                        <form:textarea path="description" class="interact-form-textarea" rows="10" />
                        <form:errors path="description" class="interact-form-error" />
                    </div>
                    <div class="interact-form-group">
                        <form:label path="quantityInStock" class="interact-form-label">Quantity in stock</form:label>
                        <form:input path="quantityInStock" class="interact-form-input" />
                        <form:errors path="quantityInStock" class="interact-form-error" />
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