import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class ProductDetail extends LitElement {
    static properties = {
        productID: {
            type: Number,
            reflect: true,
        },
        _product: {
            state: true,
        },
        _relatedProducts: {
            state: true,
        },
        _relatedProductPage: {
            state: true,
        },
        _prevProductIDS: {
            state: true,
        },
        _quantity: {
            state: true,
        },
    }

    constructor() {
        super();

        this._product = {};
        this._relatedProducts = [];
        this._relatedProductPage = 1;
        this._prevProductIDS = [];
        this._quantity = 1;
    }

    firstUpdated() {
        fetch(`${window.APP_NAME}/api/product/detail/${this.productID}`)
            .then(response => response.json())
            .then(res => {
                if (res.success) {
                    this._product = res.data;
                }
            });
        fetch(`${window.APP_NAME}/api/product/related-products/${this.productID}`)
            .then(response => response.json())
            .then(data => {
                this._relatedProducts = data;
            });
    }

    updated(props) {
        if (props.has('productID') && props.get('productID') !== undefined) {
            showLoader();
            fetch(`${window.APP_NAME}/api/product/detail/${this.productID}`)
                .then(response => response.json())
                .then(res => {
                    if (res.success) {
                        this._product = res.data;
                        fetch(`${window.APP_NAME}/api/product/related-products/${this.productID}`)
                            .then(response => response.json())
                            .then(data => {
                                this._relatedProducts = data;
                            });
                    }
                    hideLoader();
                });
        }
    }

    _resetRelatedProductPage() {
        this._relatedProductPage = 1;
    }

    _handleClickBack() {
        this.productID = this._prevProductIDS[this._prevProductIDS.length - 1];
        this._prevProductIDS = this._prevProductIDS.slice(0, this._prevProductIDS.length - 1);
        this._resetRelatedProductPage();
    }

    _handleClickClose() {
        this.dispatchEvent(new CustomEvent('close', { detail: {} }));
    }

    _handleClickOtherProduct(id) {
        return () => {
            this._prevProductIDS = [...this._prevProductIDS,  this._product.productID]
            this.productID = id;
            this._resetRelatedProductPage();
        }
    }

    _handleClickViewMoreOther() {
        this._relatedProductPage++;
        
    }

    _handleClickShowCategory(id) {
        return () => {
            this._handleClickClose();
            window.dispatchEvent(new CustomEvent(window.myEvents.showCategoryDetail, { detail: id }));
        }
    }

    _handleClickAddToCart() {
        window.myCart.addToCart(this.productID, this._quantity);
        toast({
            title: 'Cart',
            message: 'Successfully add to cart',
            type: 'success',
        });
    }

    _renderOtherProducts() {
        const quantityPerPage = 3;
        if (this._relatedProducts.length !== 0) {
            return html`
                ${
                    this._relatedProducts.slice(0, quantityPerPage * this._relatedProductPage).map(item => {
                        return html`
                            <li @click=${this._handleClickOtherProduct(item.productID)} class="product-other--item">
                                <div class="row">
                                    <div class="col-4">
                                        <img width="100%" alt="Product" src="${window.APP_NAME}/resources/images/product/${item.thumbnail}" />
                                    </div>
                                    <div class="col-8">
                                        <div class="product-other--item-body">
                                            <h2 class="product-other--item-name">
                                                ${item.name}
                                            </h2>
                                            <!-- Origin price -->
                                            <p class="product-other--item-oPrice"> 
                                                ${window.myFormatMoney(item.price)}
                                            </p>
                                            <!-- Reduced price -->
                                            <p class="product-other--item-rPrice"> 
                                                ${window.myFormatMoney(item.price * (1- item.discount))}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        `;
                    })
                }
            `;
        } else {
            return html`
                <p class="product-other--no-more">
                    There are no related products suggested for you.
                </p>
            `;
        }
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div id="product-detail-modal-container">
                <div class="container">
                    <div id="product-detail-modal-content">
                        <div class="product-detail-close-box">
                            ${
                                this._prevProductIDS.length !== 0 ?
                                html`
                                    <button @click=${this._handleClickBack} id="product-detail-close-btn">
                                        <i class="fa-solid fa-arrow-left"></i>
                                    </button>
                                ` : ''
                            }
                            <button @click=${this._handleClickClose} id="product-detail-close-btn">
                                <i class="fa-regular fa-rectangle-xmark"></i>
                            </button>
                        </div>
                        <div class="row">
                            <div class="col-4">
                                <img width="100%" alt="Product" src="${window.APP_NAME}/resources/images/product/${this._product.thumbnail ?? ''}" />
                            </div>
                            <div class="col-5">
                                <div class="product-body">
                                    <h2 class="product-name">
                                        ${this._product.name ?? ''}
                                    </h2>
                                    <p class="product-category">
                                        Category:
                                        <a @click=${this._handleClickShowCategory(this._product.categoryID)} class="product-cateLink">
                                            ${this._product.categoryName}
                                        </a>
                                    </p>
                                    <span class="product-discount">- ${this._product.discount * 100}%</span>
                                    <p class="product-price">
                                        <span class="product-oPrice">
                                            ${window.myFormatMoney(this._product.price ?? 0)}
                                        </span>
                                        <span class="product-rPrice">
                                            ${window.myFormatMoney((1 - this._product.discount ?? 0) * this._product.price ?? 0)}
                                        </span>
                                    </p>

                                    ${
                                        this._product.quantityInStock <= 0 ? 
                                        html`
                                            <p class="product-soldOut">
                                                Sold out
                                            </p>
                                        `
                                        : html`
                                            <div class="product-interact-box">
                                                <span class="product-quantity">
                                                    ${this._quantity}
                                                </span>
                                                <span @click=${() => this._quantity != 1 ? this._quantity-- : null} class="product-quantity-change-btn">
                                                    <i class="fa-solid fa-minus"></i>
                                                </span>
                                                <span @click=${() => this._quantity++} class="product-quantity-change-btn">
                                                    <i class="fa-solid fa-plus"></i>
                                                </span>
                                                <button @click=${this._handleClickAddToCart} class="product-add-cart-btn">
                                                    <img src="${window.APP_NAME}/resources/icons/svg/Add-Cart.svg" alt="Cart" class="svg-icon">
                                                    Add To Cart
                                                </button>
                                            </div>
                                        `
                                    }
                                    <h4 class="product-desc--heading">Description</h4>
                                    <pre class="product-desc">${this._product.description ?? ''}</pre>
                                </div>
                            </div>
                            <div class="col-3">
                                <h4 class="product-other--title">
                                    Related Products
                                </h4>
                                <div class="product-other--content">
                                    <ul class="product-other--list">
                                        ${this._renderOtherProducts()}
                                    </ul>
                                    <div class="product-other--actions">
                                        ${
                                            this._relatedProducts.length <= 3 * this._relatedProductPage ?
                                            '' : 
                                            html`
                                                <button @click=${this._handleClickViewMoreOther} class="product-other--action">View More</button>
                                            `
                                        }
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-product-detail', ProductDetail);