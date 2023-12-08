import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class ForgotPassword extends LitElement {

    static properties = {
        submitBtnContent: {
            state: true,
        },
        stageNumber: {
            state: true,
        },
        email: {
            state: true,
        },
        forgotPasswordCode: {
            state: true,
        },
        newPassword: {
            state: true,
        },
        newPasswordConfirm: {
            state: true,
        },
    }

    constructor() {
        super();
        // Define States
        this.submitBtnContent = 'Send';
        this.stageNumber = 1;
        this.email = '';
        this.forgotPasswordCode = '';
        this.newPassword = '';
        this.newPasswordConfirm = '';
    }

    createRenderRoot() {
        return this;
    }

    _validateAndPasswordEye() {
        const _this = this;
        let rules = []
        switch(this.stageNumber) {
            case 1:
                rules = [
                    Validator.isRequired('#email', 'Please enter your email'),
                    Validator.isEmail('#email', 'Please enter exactly email format'),
                ]
                break;
            case 2:
                rules = [
                    Validator.isRequired('#forgotPasswordCode', 'Please enter code'),
                ]
                break;
            case 3:
                rules = [
                    Validator.isRequired('#newPassword', 'Please enter newPassword'),
                    Validator.minLength('#newPassword', 8, 'Enter at least 8 characters'),
                    Validator.isRequired('#newPasswordConfirm', 'Please enter new password confirm'),
                    Validator.minLength('#newPasswordConfirm', 8, 'Enter at least 8 characters'),
                    Validator.isConfirmed('#newPasswordConfirm', () => document.getElementById('newPassword').value, 'Password confirm is not match'),
                ]
                break;
            default:
        }
        Validator({
            form: '#fpForm',
            formGroup: '.fp-form-gr',
            errorSelector: '.fp-form-message',
            rules,
            onSubmit: function(data) {
                showLoader();
                const prefix = `${window.APP_NAME}/api/user`;
                function callApi(data) {
                    fetch(`${prefix}/forgot-password`, {
                        method: 'POST',
                        credentials: 'same-origin',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(data)
                    })
                        .then(res => res.json())
                        .then(data => {
                            if (data.success) {
                                _this.stageNumber++;
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
                            hideLoader();
                        })
                }
                switch(_this.stageNumber) {
                    case 1:
                        callApi({
                            email: _this.email,
                            forgotPasswordCode: null,
                            newPassword: null,
                            newPasswordConfirm: null,
                        });
                        break;
                    case 2:
                        callApi({
                            email: _this.email,
                            forgotPasswordCode: _this.forgotPasswordCode,
                            newPassword: null,
                            newPasswordConfirm: null,
                        });
                        break;
                    case 3:
                        callApi({
                            email: _this.email,
                            forgotPasswordCode: _this.forgotPasswordCode,
                            newPassword: _this.newPassword,
                            newPasswordConfirm: _this.newPasswordConfirm,
                        });
                        break;
                    default:
                        console.log('Default case');
                }
            }
        });
        passwordEye(
            {
                idSelectors: [],
                wrapperSelector: '.password-eye-wrapper',
            }
        );
    }

    updated(props) {
        this._validateAndPasswordEye();
    }

    render() {
        return html`
            <div id="fp-container">
                ${
                    [1, 2, 3].includes(this.stageNumber) ?
                    html`
                        <h3 class="title">Forgot Password</h3>
                        <div class="notice">
                            <p>
                                Enter email and we will send you a code. Enter code and change your password.
                            </p>
                        </div>
                        <form id="fpForm" class="fp-form">
                            ${
                                this.stageNumber === 1 ? 
                                html`
                                <div class="fp-form-gr">
                                    <label for="email" class="fp-form-label">Email</label>
                                    <input .value=${this.email} @change=${e => this.email = e.target.value} name="email" id="email" type="email" class="fp-form-input">
                                    <span class="fp-form-message"></span>
                                </div>
                                ` 
                                : ''
                            }
                            ${
                                this.stageNumber === 2 ? 
                                html`
                                <div class="fp-form-gr">
                                    <label for="forgotPasswordCode" class="fp-form-label">Forgot Password Code</label>
                                    <input .value=${this.forgotPasswordCode} @change=${e => this.forgotPasswordCode = e.target.value} name="forgotPasswordCode" id="forgotPasswordCode" type="text" class="fp-form-input">
                                    <span class="fp-form-message"></span>
                                </div>
                                ` 
                                : ''
                            }
                            ${
                                this.stageNumber === 3 ? 
                                html`
                                <div class="fp-form-gr">
                                    <label for="newPassword" class="fp-form-label">New Password</label>
                                    <div class="password-eye-wrapper">
                                        <input .value=${this.newPassword} @change=${e => this.newPassword = e.target.value} name="newPassword" id="newPassword" type="password" class="fp-form-input">
                                    </div>
                                    <span class="fp-form-message"></span>
                                </div>
                                <div class="fp-form-gr">
                                    <label for="newPasswordConfirm" class="fp-form-label">New Password Confirm</label>
                                    <div class="password-eye-wrapper">
                                        <input .value=${this.newPasswordConfirm} @change=${e => this.newPasswordConfirm = e.target.value} name="newPasswordConfirm" id="newPasswordConfirm" type="password" class="fp-form-input">
                                    </div>
                                    <span class="fp-form-message"></span>
                                </div>
                                ` 
                                : ''
                            }
                            
                            <div style="display: flex; justify-content: right;">
                                <button class="form-btn">${this.submitBtnContent}</button>
                            </div>
                        </form>
                    `
                    : ''
                }
                ${
                    this.stageNumber === 4 ?
                    html`
                        <div class="success-box">
                            <p class="icon"><i class="fa-regular fa-circle-check"></i></p>
                            <p class="text">
                                Successfully change  your password! You can sign in right now.
                            </p>
                            <a href="${window.APP_NAME}/user/sign-in" class="sign-in-link">Sign in now</a>
                        </div>
                    ` : ''
                }
            </div>
        `;
    }

}

customElements.define('nos-forgot-password', ForgotPassword);