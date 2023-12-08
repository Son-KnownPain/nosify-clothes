import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CateProductItem extends LitElement {
    static properties = {
        productID: {
            type: Number,
            reflect: true,
        },
        name: {
            type: String,
            reflect: true,
        },
        price: {
            type: Number,
            reflect: true,
        },
        discount: {
            type: Number,
            reflect: true,
        },
        // description: {
        //     type: String,
        //     reflect: true,
        // },
        quantityInStock: {
            type: Number,
            reflect: true,
        },
        thumbnail: {
            type: String,
            reflect: true,
        },
        _showDetail: {
            state: true,
        }
    }

    constructor() {
        super();

        this._showDetail = false;
    }

    _handleClickViewDetail() {
        this._showDetail = true;
    }

    _handleClose() {
        this._showDetail = false;
    }

    _handleClickAddToCart() {
        window.myCart.addToCart(this.productID);
        toast({
            title: 'Cart',
            message: 'Successfully add to cart',
            type: 'success',
        });
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            ${this._showDetail ? html`<nos-product-detail @close=${this._handleClose} .productID=${this.productID}></nos-product-detail>` : ''}
            <div class="product-item-of-cate">
                <div class="row">
                    <div class="col-6">
                        <img
                            class="product-item-of-cate--img"
                            width="100%"
                            alt="Product Image"
                            src="${window.APP_NAME}/resources/images/product/${this.thumbnail}" 
                        />
                    </div>
                    <div class="col-6">
                        <div class="product-item-of-cate--body">
                            <h2 class="product-item-of-cate--name">
                                ${this.name}
                            </h2>
                            <!-- Origin price -->
                            <p class="product-item-of-cate--oprice"> 
                                ${window.myFormatMoney(this.price)}
                            </p>
                            <!-- Reduced price -->
                            <p class="product-item-of-cate--rprice"> 
                                ${window.myFormatMoney(this.price * (1- this.discount))}
                            </p>
                            <div class="product-item-of-cate--actions">
                                <a @click=${this._handleClickViewDetail} class="product-item-of-cate--action-item">View Detail</a>
                                ${
                                    this.quantityInStock <= 0 ?
                                    html`
                                        <p class="product-item-of-cate--action-soldOut">Sold out</p>
                                    ` :
                                    html`
                                        <a @click=${this._handleClickAddToCart} class="product-item-of-cate--action-item">Add To Cart</a>
                                    `
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-cate-product-item', CateProductItem);