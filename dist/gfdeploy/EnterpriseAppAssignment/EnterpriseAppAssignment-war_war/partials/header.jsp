<header id="header">
    <div class="container header-inner" style="height: 100%;">
        <a href="${pageContext.request.contextPath}" class="header-logoLink">
            <img src="${pageContext.request.contextPath}/resources/images/nosify-logo.png" alt="Logo" class="header-logoImg">
        </a>
        <nos-header-search></nos-header-search>

        <div class="user-actions">
            <span class="user-actionBtn user-cart">
                <img class="svg-icon" src="${pageContext.request.contextPath}/resources/icons/svg/ShoppingCart.svg" alt="Cart">

                <nos-cart></nos-cart>
            </span>
            <a href="${pageContext.request.contextPath}/user/sign-up" class="user-actionBtn user-not-login-btn">Sign up</a>
            <a href="${pageContext.request.contextPath}/user/sign-in" class="user-actionBtn user-loginBtn user-not-login-btn">Sign in</a>
            <span class="user-actionBtn user-logo user-logged-in-btn">
                <img src="${pageContext.request.contextPath}/resources/icons/svg/User-2.svg" alt="User Icon" class="svg-icon">

                <div class="user-actionBox">
                    <ul class="user-action-list">
                        <li class="user-action-item" id="myProfileBtn">
                            <a class="user-action-link">
                                <img src="${pageContext.request.contextPath}/resources/icons/svg/User-Security-Card.svg" alt="Profile" class="svg-icon">
                                <span style="margin-left: 8px;">My Profile</span>
                            </a>
                        </li>
                        <li class="user-action-item" id="myProfileBtn" onclick="handlers.dispatchShowOrderList()">
                            <a class="user-action-link">
                                <img src="${pageContext.request.contextPath}/resources/icons/svg/Shopping-list.svg" alt="Profile" class="svg-icon">
                                <span style="margin-left: 8px;">My Orders</span>
                            </a>
                        </li>
                        <li class="user-action-item">
                            <a id="signOutBtn" class="user-action-link">
                                <img src="${pageContext.request.contextPath}/resources/icons/svg/Logout.svg" alt="Profile" class="svg-icon">
                                <span style="margin-left: 8px;">Sign out</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </span>
        </div>
    </div>
</header>

<div id="user-profile-container">
    <div id="user-profile-content">
        <div class="close-box">
            <span class="close-btn">
                <i class="fa-solid fa-xmark"></i>
            </span>
        </div>
        <form action="/" method="POST" id="user-profile-form" enctype="multipart/form-data">
            <div class="row">
                <div class="col-2">
                    <div class="img" style="background-image: url('${pageContext.request.contextPath}/resources/images/user/default-avatar.jpg');">
                        <label for="avatar" class="change-img-btn">
                            <i class="fa-solid fa-arrow-up-from-bracket"></i>
                        </label>
                        <input type="file" id="avatar" name="avatar" style="display: none;">
                    </div>
                </div>
                <div class="col-10">
                    <h2 class="fullname">
                        <span class="value">
                            Loading...
                        </span>
                        <span class="change-name-btn">
                            <i class="fa-regular fa-pen-to-square"></i>
                        </span>
                    </h2>
                    <div class="input-gr">
                        <input type="text" name="fullname" id="fullname" value="Nguyen Hong Son">
                        <span class="user-profile-form-message"></span>
                    </div>
                    <p class="email">
                        <i class="fa-regular fa-envelope"></i>
                        Loading...
                    </p>
    
                    <div class="actions">
                        <span class="action cancel" style="margin-right: 6px; background-color: #ccc; color: #000">
                            Cancel
                        </span>
                        <button class="action" id="changeUserInfoSubmitBtn">
                            Save
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>