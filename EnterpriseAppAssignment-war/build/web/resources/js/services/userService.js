const userService = {
    prefix: `${window.APP_NAME}/api/user`,
    postSignUp(data) {
        const _this = this;
        return fetch(
            `${_this.prefix}/sign-up`,
            {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            }
        );
    },
    postSignIn(data) {
        const _this = this;
        return fetch(
            `${_this.prefix}/sign-in`,
            {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            }
        );
    },
    getUserInfo() {
        const _this = this;
        return fetch(`${_this.prefix}/info`); 
    },
    changeUserInfo(data, callback = (res) => { console.log(res); }) {
        const _this = this;
        const formData = new FormData();
        const [avatar] = data.avatar;
        if (avatar) {
            formData.append('avatar', avatar);
        }
        const fullname = data.fullname;
        formData.append('data', 
            new Blob(
                [
                    JSON.stringify({
                        fullname
                    })
                ], 
                {
                    type: 'application/json'
                }
            )
        );
        const requestHeader = {
            method: 'POST',
            credentials: 'same-origin',
            body: formData
        }
        fetch(`${_this.prefix}/change-info`, requestHeader)
            .then(res => res.json())
            .then(res => {
                if (res.success) {
                    callback(res);
                }
            })

    },
    signOut(callback) {
        const _this = this;
        fetch(`${_this.prefix}/sign-out`)
            .then(res => res.json())
            .then(res => {
                if (res.success) {
                    callback(res);
                }
            });
    },
    start() {
        
    }
}

userService.start();