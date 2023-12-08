import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CartComponent extends LitElement {
    static properties = {
        _data: {
            state: true,
        },
        _checkedAll: {
            state: true,
        },
    }

    constructor() {
        super();

        this._data = [];
        this._checkedAll = true;
    }

    connectedCallback() {
        super.connectedCallback();
        window.addEventListener(window.myEvents.reFetchCart, () => {
            this._fetchData();
        });
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener(window.myEvents.reFetchCart);
    }

    _fetchData() {
        const cart = window.myCart.getCart();

        let ids = '';

        let checkedAll = true;

        cart?.forEach(c => {
            if (typeof c.id === 'number') {
                ids += `${c.id},`
            }
            if (!c.checked) {
                checkedAll = false;
            }
        });

        this._checkedAll = checkedAll;

        ids.length !== 0 && fetch(`${window.APP_NAME}/api/product/by-list-id?ids=${ids}`)
            .then(response => response.json())
            .then(data => {
                this._data = data.map(
                    item => {
                        const cartItem = cart.find(c => c.id == item.productID);

                        return {
                            product: item,
                            quantity: cartItem.quantity,
                            checked: cartItem.checked,
                        }
                    }
                );
            });

        if (ids.length === 0) this._data  = [];
    }

    firstUpdated() {
        this._fetchData();
    }

    createRenderRoot() {
        return this;
    }

    _handleIncreaseQuantity(id) {
        return () => {
            window.myCart.addToCart(id);
        }
    }

    _handleDecreaseQuantity(id) {
        return () => {
            window.myCart.decreaseQuantity(id);
        }
    }

    _handleRemoveItem(id) {
        return () => {
            window.myCart.removeItem(id);
        }
    }

    _handleRemoveAllItems() {
        window.myCart.removeAllItem();
    }

    _handleCheckItem(id) {
        return () => {
            window.myCart.toggleChecked(id);
        }
    }

    _handleToggleAll() {
        window.myCart.toggleCheckedAll(!this._checkedAll);
    }

    _handleClickProductName(id) {
        return () => {
            window.dispatchEvent(new CustomEvent(window.myEvents.showProductDetail, { detail: id }));
        }
    }

    _renderTotalPrice() {
        const totalPrice = this._data.reduce((acc, cur) => {
            if (cur.checked) {
                return acc + (cur.product.price * (1 - cur.product.discount) * cur.quantity);
            } else {
                return acc;
            }
        }, 0);

        return window.myFormatMoney(totalPrice);
    }

    _renderProducts() {
        if (!this._data || this._data.length === 0) {
            return html`
                <h4 class="user-cart--no-products">
                    HAVE NOT ANY PRODUCTS IN YOUR CART
                </h4>
            `;
        }

        return html`
            ${this._data.map(data => html`
                <li class="user-cart-item">
                    <div class="row">
                        <div class="col-1">
                            <div class="user-cart-item--checkbox-wrapper">
                                ${
                                    data.checked ?
                                    html`<input @click=${this._handleCheckItem(data.product.productID)} type="radio" class="user-cart-item--checkbox" ?checked=${data.checked}>` :
                                    html`<input @click=${this._handleCheckItem(data.product.productID)} type="radio" class="user-cart-item--checkbox">`
                                }
                            </div>
                        </div>
                        <div class="col-2">
                            <img width="100%" src="${window.APP_NAME}/resources/images/product/${data.product.thumbnail}" alt="Thumbnail">
                        </div>
                        <div class="col-6">
                            <a @click=${this._handleClickProductName(data.product.productID)} class="product-name-link">
                                <h4 class="product-name">
                                    ${data.product.name}
                                </h4>
                            </a>
                            <ul class="product-actions">
                                <li @click=${this._handleIncreaseQuantity(data.product.productID)} class="product-action">
                                    <a class="product-action-link">
                                        <img width="18px" src="${window.APP_NAME}/resources/icons/svg/Add.svg" alt="Increase">
                                    </a>
                                </li>
                                <li @click=${this._handleDecreaseQuantity(data.product.productID)} class="product-action">
                                    <a class="product-action-link">
                                        <img width="18px" src="${window.APP_NAME}/resources/icons/svg/Remove.svg" alt="Decrease">
                                    </a>
                                </li>
                                <li @click=${this._handleRemoveItem(data.product.productID)} class="product-action">
                                    <a class="product-action-link">
                                        <img width="18px" src="${window.APP_NAME}/resources/icons/svg/Trash.svg" alt="Remove">
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="col-3">
                            <p class="product-price" title="Unit price: ${window.myFormatMoney(data.product.price * (1 - data.product.discount))}">
                                x${data.quantity} <br> ${window.myFormatMoney((data.product.price * (1 - data.product.discount)) * data.quantity)}
                            </p>
                        </div>
                    </div>
                </li>
            `)}
        `;
    }

    render() {
        return html`
            <div class="user-cartBox">
                <div class="user-cartBox-header">
                    <h4 class="user-cartBox-heading">
                        <img width="18px" src="${window.APP_NAME}/resources/icons/svg/ShoppingCart.svg" alt="Cart">
                        Cart
                    </h4>

                    <div class="user-cartBox--actions">
                        ${
                            this._data?.length === 0 ? '' : 
                            html`
                                <button @click=${this._handleToggleAll} class="user-cartBox--action">
                                    ${this._checkedAll ? 'Uncheck all' : 'Check all'}
                                </button>
                                <button @click=${this._handleRemoveAllItems} class="user-cartBox--action">
                                    Remove all
                                </button>
                            `
                        }
                    </div>
                </div>
                
                <ul class="user-cart-list">
                    ${this._renderProducts()}
                </ul>
                <div class="user-cartBox-footer">
                    <p class="user-cartBox-total">
                        Total price <span>${this._renderTotalPrice()}</span>
                    </p>
                    ${
                        this._data.length
                        ?
                        html`
                            <div class="row">
                                <div class="col-12">
                                    <a href="${window.APP_NAME}/user/checkout" class="user-cartBox-action">
                                        <i class="fa-solid fa-money-check-dollar"></i>
                                        Checkout
                                    </a>
                                </div>
                            </div>
                        `
                        :
                        html``
                    }
                </div>
            </div>
        `;
    }
}

customElements.define('nos-cart', CartComponent);