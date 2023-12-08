
const userProfile = {
    activeEdit(isActive, fullnameActive = false) {
        const userProfileContentElm = document.getElementById('user-profile-content');
        if (isActive) {
            userProfileContentElm.classList.add('active-edit')
        } else {
            userProfileContentElm.classList.remove('active-edit')
        }
        if (fullnameActive) {
            userProfileContentElm.classList.add('active-fullname-edit')
        } else {
            userProfileContentElm.classList.remove('active-fullname-edit')
        }
    },
    signOut() {
        userService.signOut(function(res) {
            window.location.reload();
        });
    },
    listenEvents() {
        const myProfileBtn = document.getElementById('myProfileBtn');
        const userProfileContainer = document.getElementById('user-profile-container');
        const userProfileContent = document.getElementById('user-profile-content');
        const closeBtn = document.querySelector('#user-profile-content .close-btn');

        const changeNameBtn = document.querySelector('.change-name-btn');
        const avatarInputElm = document.getElementById('avatar');
        const cancelEditBtn = document.querySelector('#user-profile-content .action.cancel');

        const signOutBtn = document.querySelector('#signOutBtn');

        avatarInputElm.oninput = e => {
            const [file] = e.target.files;
            if (file) {
                this.activeEdit(true);
                const imgElm = document.querySelector('#user-profile-form .img');
                imgElm.style.backgroundImage = `url('${URL.createObjectURL(file)}')`;
            }
        }

        changeNameBtn.onclick = () => {
            this.activeEdit(true, true);
        }

        cancelEditBtn.onclick = () => {
            this.activeEdit(false, false);
        }

        myProfileBtn.onclick = () => {
            userProfileContainer.classList.add('show')
            userProfileContent.classList.add('show')
        }
        closeBtn.onclick = () => {
            userProfileContent.classList.remove('show')
            userProfileContainer.classList.remove('show')
        }

        signOutBtn.onclick = () => {
            this.signOut();
        }
    },
    renderUserProfile({fullname, avatar, email}) {
        const avatarElm = document.querySelector('#user-profile-content .img');
        const fullnameElm = document.querySelector('#user-profile-content .fullname .value');
        const emailElm = document.querySelector('#user-profile-content .email');
        const fullnameInputElm = document.querySelector('#fullname');

        avatarElm.style.backgroundImage = `url('${window.APP_NAME}/resources/images/user/${avatar}')`;
        fullnameElm.textContent = fullname;
        fullnameInputElm.value = fullname;
        emailElm.textContent = email;
    },
    fetchUserInfo() {
        userService.getUserInfo()
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    this.renderUserProfile(data.user)
                    localStorage.setItem('user_logged_in', JSON.stringify(true));
                } else {
                    localStorage.removeItem('user_logged_in');
                }
                handleIsUserLogin();
            });
    },
    validateUserProfile() {
        const _this = this;
        Validator({
            form: '#user-profile-form',
            formGroup: '.input-gr',
            errorSelector: '.user-profile-form-message',
            rules: [
                Validator.isRequired('#fullname', 'Please enter your fullname'),
            ],
            onSubmit: function(data) {
                userService.changeUserInfo(data, () => {
                    _this.activeEdit(false, false);
                    _this.fetchUserInfo();
                })
            }
        });
    },
    start() {
        this.listenEvents();
        this.fetchUserInfo();
        this.validateUserProfile();
    }
}

userProfile.start();