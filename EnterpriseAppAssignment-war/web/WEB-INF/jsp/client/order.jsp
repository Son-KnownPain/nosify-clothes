<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="my-app-name" content="${pageContext.request.contextPath}">
    <title>Checkout | Nosify Clothes</title>
    <!-- Favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/nosify-logo.png"/>

    <!-- Fontawesome CDN -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

    <!-- Base CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/base.css"/>
    <!-- Reset CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/reset-css.css"/>
    <!--Toast Message-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/lib/toast/toast.css"/>
    <!--Loader-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/lib/loader/loader.css"/>
    <!-- Component -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/components/nos-checkout.css"/>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user/checkout.css"/>
</head>
<body>
    <div id="app">
        <div class="header">
            <h2 class="heading">
                Nosify Clothes | Checkout
            </h2>
            <p class="desc">
                If you don't want to make a payment, you can <a href="${pageContext.request.contextPath}/" class="link">back to home page</a>!
            </p>
        </div>
        <nos-checkout></nos-checkout>
    </div>
    



    <!--JS base-->
    <script src="${pageContext.request.contextPath}/resources/js/base.js"></script>
    
    <!--Toast-->
    <div id="toast"></div>
    <script src="${pageContext.request.contextPath}/resources/lib/toast/toast.js"></script>

    <!--Loader-->
    <div id="loader" style="display: none;"></div>
    <script src="${pageContext.request.contextPath}/resources/lib/loader/loader.js"></script>

    <!--Toast-->
    <div id="toast"></div>
    <script src="${pageContext.request.contextPath}/resources/lib/toast/toast.js"></script>
    
    <!--Validation-->
    <script src="${pageContext.request.contextPath}/resources/lib/validation/validation.js"></script>

    <!-- Component -->
    <script type="module" src="${pageContext.request.contextPath}/resources/js/lit/components/common/nos-checkout.js"></script>
</body>
</html>