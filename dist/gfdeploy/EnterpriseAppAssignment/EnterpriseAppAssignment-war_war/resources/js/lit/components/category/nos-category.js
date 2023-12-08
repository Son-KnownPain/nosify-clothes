import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CategoryComponent extends LitElement {
    static properties = {
        categoryID: {
            reflect: true,
            type: Number,
        },
        _category: {
            state: true,
        },
        // Pagination
        _currentPage: {
            state: true,
        },
        _maxPage: {
            state: true,
        },
        _numberProductOfPages: {
            state: true,
        },
    }

    constructor() {
        super();
        this._category = {};

        // Pagination
        this._currentPage = 1;
        this._maxPage = 0;
        this._numberProductOfPages = 4;

    }

    firstUpdated() {
        fetch(`${window.APP_NAME}/api/category/by-id/${this.categoryID}`)
            .then(response => response.json())
            .then(res => {
                if (res.success) {
                    this._category = res.data;
                    this._maxPage = Math.ceil(res.data.productList.length / this._numberProductOfPages);
                }
            })
    }

    _handleCloseModal() {
        this.dispatchEvent(new CustomEvent('close', { }));
    }

    _handleClickProductDetail(id) {
        return () => {
            window.dispatchEvent(new CustomEvent(window.myEvents.showProductDetail, { detail: id }));
        }
    }

    _handleClickChangePage(page) {
        return () => {
            if (page > 0 && page <= this._maxPage && page !== this._currentPage) {
                this._currentPage = page;
            }
        }
    }

    _handleClickAddToCart(id) {
        return () => {
            window.myCart.addToCart(id);
            toast({
                title: 'Cart',
                message: 'Successfully add to cart',
                type: 'success',
            });
        }
    }

    _renderProductsCard() {
        if (this._category.productList) {
            if (this._category.productList.length === 0) {
                return html`<h3 class="have-not-products">Have not any products available</h3>`;
            }
            return html`
                ${this._category.productList.slice(this._numberProductOfPages * (this._currentPage - 1), this._numberProductOfPages * this._currentPage).map(product => html`
                    <div class="col-3">
                        <div class="product-item-card">
                            <div class="product-item-card--thumbnail">
                                <img
                                    width="100%"
                                    class="product-item-card--img"
                                    alt="Product"
                                    src="${window.APP_NAME}/resources/images/product/${product.thumbnail}" 
                                />

                                <div class="product-item-card--actions">
                                    <button @click=${this._handleClickProductDetail(product.productID)} class="product-item-card--action">
                                        View Detail
                                    </button>
                                    <button @click=${this._handleClickAddToCart(product.productID)} class="product-item-card--action">
                                        Add To Cart
                                    </button>
                                </div>
                            </div>
                            <div class="product-item-card--body">
                                <h3 class="product-item-card--name">${product.name}</h3>
                                <p class="product-item-card--price">
                                    ${window.myFormatMoney(product.price * (1  - product.discount))}
                                </p>
                            </div>
                        </div>
                    </div>
                `)}
            `;
        } else {
            return html``;
        }
    }

    _renderPaginationBox() {
        const pages = [];

        const numsPageForRender = 5;
        const currentPage = this._currentPage;
        const minPage = 1;
        const maxPage = this._maxPage;

        let startIndex = 0;

        if (currentPage <= maxPage && currentPage > maxPage - 2) {
            startIndex = maxPage - 5;
        } else if (currentPage > minPage + 1 && currentPage < maxPage - 1) {
            startIndex = currentPage - 2 - 1;
        }

        if (startIndex < 0) {
            startIndex = 0;
        }

        let endIndex = startIndex + numsPageForRender;
        if (endIndex > maxPage) {
            endIndex = maxPage;
        }

        for (let index = startIndex; index < endIndex; index++) {
            pages.push(
                html`
                    <span @click=${this._handleClickChangePage(index + 1)} class="product-pagination--page-number flx-all-center ${this._currentPage === index + 1 ? 'active' : ''}">
                        ${index + 1}
                    </span>
                `
            );
        }

        return html`
            <div class="products-pagination">
                <span @click=${this._handleClickChangePage(this._currentPage - 1)} class="product-pagination--change-btn flx-all-center">
                    <i class="fa-solid fa-chevron-left"></i>
                </span>
                ${pages}
                <span @click=${this._handleClickChangePage(this._currentPage + 1)} class="product-pagination--change-btn flx-all-center">
                    <i class="fa-solid fa-chevron-right"></i>
                </span>
            </div>
        `;
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div id="category-detail-modal-container">
                <div class="container">
                    <div id="category-detail-modal-content">
                        <div class="category-detail-close-box">
                            <button @click=${this._handleCloseModal} id="category-detail-close-btn">
                                <i class="fa-regular fa-rectangle-xmark"></i>
                            </button>
                        </div>
                        <h1 class="heading">
                            ${this._category.name}
                        </h1>
                        <div class="products-display">
                            <div class="row">
                                ${this._renderProductsCard()}
                            </div>
                        </div>
                        ${this._renderPaginationBox()}
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-category', CategoryComponent);