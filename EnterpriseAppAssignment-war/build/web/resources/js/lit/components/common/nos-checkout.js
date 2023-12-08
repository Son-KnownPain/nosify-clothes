import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CheckoutComponent extends LitElement {
    static properties = {
        _phone: {
            state: true,
        },
        _address: {
            state: true,
        },
        _userInfo: {
            state: true,
        },
        _products: {
            state: true,
        },
        _addressPicked: {
            state: true,
        },
        _addressSelectData: {
            state: true,
        },
        _addressSelector: {
            state: true,
        },
        _isLoaded: {
            state: true,
        },
        _isOrder: {
            state: true,
        },
    }

    constructor() {
        super();

        showLoader();

        this._phone = '';
        this._address = '';
        this._userInfo = null;

        this._products = [];

        this._addressPicked = '';

        this._addressSelectData = null;
        this._addressSelector = {
            p: 1,
            d: 1,
            w: 1,
        };

        this._isLoaded = false;
        this._isOrder = false;
    }

    firstUpdated() {
        fetch(`${window.APP_NAME}/api/user/info`)
            .then(response => response.json())
            .then(res => {
                if (res.success) {
                    this._userInfo = res?.user;
                }
            });

        fetch('https://provinces.open-api.vn/api/?depth=3')
            .then(res => res.json())
            .then(res => {
                const [firstProvince] = res;
                const [firstDistrict] = firstProvince.districts;
                const [firstWard] = firstDistrict.wards;

                this._addressSelectData = res;
                this._addressPicked = `${firstWard.name}, ${firstDistrict.name}, ${firstProvince.name}`;
            });

        this._fetchProductsData();
    }

    _fetchProductsData() {
        const cart = window.myCart.getCart();

        let ids = '';

        cart?.forEach(c => {
            if (typeof c.id === 'number' && c.checked) {
                ids += `${c.id},`
            }
        });

        fetch(`${window.APP_NAME}/api/product/by-list-id?ids=${ids}`)
            .then(response => response.json())
            .then(data => {
                this._products = data.map(
                    item => {
                        const cartItem = cart.find(c => c.id == item.productID);

                        return {
                            product: item,
                            quantity: cartItem.quantity,
                        }
                    }
                );

                this._isLoaded = true;
                hideLoader();
            });
    }

    _renderProducts() {
        return html`
            ${this._products.map(item => html`
                    <div class="product-item">
                        <div class="row">
                            <div class="col-2">
                                <div class="product-thumbnail" style="background-image: url('${window.APP_NAME}/resources/images/product/${item.product.thumbnail}');"></div>
                            </div>
                            <div class="col-7">
                                <h3 class="product-name">
                                    ${item.product.name}
                                </h3>
                                <p class="product-line">
                                    Quantity: ${item.quantity}
                                </p>
                                <p class="product-line">
                                    Unit price: ${window.myFormatMoney(item.product.price * (1 - item.product.discount))}
                                </p>
                            </div>
                            <div class="col-3">
                                <p class="product-result">
                                    ${window.myFormatMoney((item.product.price * (1 - item.product.discount)) * item.quantity)}
                                </p>
                            </div>
                        </div>
                    </div>
                `)
            }
        `;
    }

    _renderNoLogin() {
        return html`
            <div class="container">
                <div class="warning">
                    <p class="text">
                        You are not login. Please login before make a payment!
                    </p>
                    <p>
                        <a href="${window.APP_NAME}/user/sign-in">Login now</a>
                    </p>
                </div>
            </div>
        `;
    }

    _renderNoProducts() {
        return html`
            <div class="container">
                <div class="warning">
                    <p class="text">
                        Have no any product to pay!
                    </p>
                    <p>
                        <a href="${window.APP_NAME}/">Continue shopping</a>
                    </p>
                </div>
            </div>
        `
    }

    _renderCheckout() {
        return html`        
            <div id="checkout">
                <div class="container">
                    <div class="row">
                        <div class="col-6">
                            <h3 class="title">
                                Delivery Infomation
                            </h3>
                            <div class="user">
                                <div class="row">
                                    <div class="col-2">
                                        <div class="user-avatar" style="background-image: url('${window.APP_NAME}/resources/images/user/${this._userInfo?.avatar ?? 'default-avatar.jpg'}');"></div>
                                    </div>
                                    <div class="col-10">
                                        <div class="user-body">
                                            <h4 class="user-name">
                                                ${this._userInfo?.fullname}
                                            </h4>
                                            <p class="user-email">
                                                <i class="fa-regular fa-envelope"></i>
                                                ${this._userInfo?.email}
                                            </p>
                                            <p class="user-phone">
                                                <i class="fa-solid fa-phone"></i>
                                                ${this._phone?.length ? this._phone : 'We need your phone number!'}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="info">
                                <div class="gr">
                                    <input @input=${e => this._phone = e.target.value} type="text" class="input" placeholder="Phone" value=${this._phone} />
                                </div>
                                ${this._renderAddressSelector()}
                                <div class="gr">
                                    <input @input=${e => this._address = e.target.value} type="text" class="input" placeholder="Address Detail" value=${this._address} />
                                </div>
                            </div>

                            <p class="info-address">
                                <i class="fa-solid fa-location-dot"></i>
                                Address: ${this._address}${this._address.length ? ',' : ''} ${this._addressPicked}
                            </p>

                            <h3 class="title mt-5">
                                Delivery Method
                            </h3>
                            <p class="value">
                                Standard ( 40.000 đ )
                            </p>

                            <h3 class="title mt-5">
                                Payment Method
                            </h3>
                            <p class="value">
                                Cash on Delivery (COD)
                            </p>
                        </div>
                        <div class="col-6">
                            <div class="products">
                                ${this._renderProducts()}
                            </div>
                            <div class="price-box">
                                <p class="price-line">
                                    <span class="price-key">Subtotal</span>
                                    <span class="price-val">${this._calculateTotalPrice()}</span>
                                </p>
                                <p class="price-line">
                                    <span class="price-key">Delivery cost</span>
                                    <span class="price-val">40.000 đ</span>
                                </p>
                            </div>
                            <div class="price-box">
                                <p class="price-line-strong">
                                    <span class="price-key">Total</span>
                                    <span class="price-val total-price-val">${this._calculateTotalPrice(40000)}</span>
                                </p>
                            </div>

                            <div class="actions">
                                <button @click=${this._handleClickOrderComplete} class="action">
                                    Order complete
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    _renderAddressSelector() {
        if (this._addressSelectData) {
            const provinces = this._addressSelectData;
            const districts = provinces.find(p => p.code == this._addressSelector.p).districts;
            const district = districts.find(d => d.code == this._addressSelector.d);
            const wards = district ? district.wards : districts[0].wards;

            return html`
                <div class="address-box">
                    <div class="row">
                        <div class="col-4">
                            <select @change=${this._handleChangeProvince} class="address-select">
                                ${provinces.map(a => html`<option value=${a.code}>${a.name}</option>`)}
                            </select>
                        </div>
                        <div class="col-4">
                            <select @change=${this._handleChangeDistrict} class="address-select">
                                ${districts.map(d => html`<option value=${d.code}>${d.name}</option>`)}
                            </select>
                        </div>
                        <div class="col-4">
                            <select @change=${this._handleChangeWard} class="address-select">
                                ${wards.map(w => html`<option value=${w.code}>${w.name}</option>`)}
                            </select>
                        </div>
                    </div>
                </div>
            `;
        } else {
            return html``;
        }
    }

    _renderOrderSuccess() {
        return html`
            <div id="order-success">
                <div class="icon">
                    <i class="fa-solid fa-circle-check"></i>
                </div>
                <p class="annoucement">
                    We have received your order. Orders will be processed as soon as possible!
                </p>
                <a href="${window.APP_NAME}/" class="continue-shopping">Continue shopping</a>
            </div>
        `;
    }

    _calculateTotalPrice(shipCost = 0) {
        return window.myFormatMoney(this._products.reduce((acc, cur) => acc + cur.product.price * (1 - cur.product.discount), shipCost));
    }

    _handleChangeProvince(e) {
        this._produceFinalAddresses({p: e.target.value});
    }

    _handleChangeDistrict(e) {
        this._produceFinalAddresses({d: e.target.value});
    }

    _handleChangeWard(e) {
        this._produceFinalAddresses({w: e.target.value});
    }

    _handleClickOrderComplete() {
        let valid = true;
        if (this._phone?.length !== 10) {
            toast({
                title: 'Warning',
                message: 'Phone must be 10 numbers',
                type: 'warning',
            });
            valid = false;
        }
        if (`${this._address}${this._address.length ? ', ' : ''}${this._addressPicked}`.length === 0) {
            toast({
                title: 'Warning',
                message: 'Address invalid',
                type: 'warning',
            });
            valid = false;
        }

        if (!valid) return;

        fetch(`${window.APP_NAME}/api/order/checkout`, {
            method: 'POST',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                {
                    fullname: this._userInfo.fullname,
                    phone: this._phone,
                    address: `${this._address}${this._address.length ? ', ' : ''}${this._addressPicked}`,
                    productsData: this._products.map(item => {
                        return {
                            productID: item.product.productID,
                            quantity: item.quantity,
                        }
                    }),
                }
            ),
        })
        .then(res => res.json())
        .then(res => {
            if (res.success) {
                window.myCart.clearChecked();
                this._isOrder = true;
            } else {
                toast({
                    title: 'Warning',
                    message: res.message,
                    type: 'warning',
                });
            }
        })
            
    }

    _produceFinalAddresses({ p, d, w }) {
        const provinces = this._addressSelectData;
        let districts = provinces.find(p => p.code == this._addressSelector.p).districts;
        let district = districts.find(d => d.code == this._addressSelector.d);
        let wards = district ? district.wards : districts[0].wards;

        if (p) {
            districts = provinces.find(pItem => pItem.code == p).districts;
            district = districts.find(dItem => dItem.code == this._addressSelector.d);
            wards = district ? district.wards : districts[0].wards;
            this._addressSelector = {
                ...this._addressSelector,
                p: p,
                d: districts[0].code,
                w: wards[0].code,
            };
        }
        if (d) {
            district = districts.find(dItem => dItem.code == d);
            wards = district ? district.wards : districts[0].wards;
            this._addressSelector = {
                ...this._addressSelector,
                d: d,
                w: wards[0].code,
            };
        }
        if (w) {
            this._addressSelector = {
                ...this._addressSelector,
                w: w
            };
        }

        this._addressPicked = `${wards.find(item => item.code == this._addressSelector.w).name}, ${districts.find(item => item.code == this._addressSelector.d).name}, ${provinces.find(item => item.code == this._addressSelector.p).name}`;
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            ${
                this._isOrder
                ?
                html`${this._renderOrderSuccess()}`
                :
                (
                    this._isLoaded 
                    ?
                    (
                        this._products?.length 
                        ?
                        (
                            this._userInfo 
                            ?
                            html`
                                ${this._renderCheckout()}
                            ` 
                            :
                            html`
                                ${this._renderNoLogin()}
                            `
                        ) 
                        :
                        html`
                            ${this._renderNoProducts()}
                        `
                    )
                    :
                    html``
                )
            }
        `;
    }
}

customElements.define('nos-checkout', CheckoutComponent);