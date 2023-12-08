import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CategoryHomePage extends LitElement {
    static properties = {
        category: {
            type: Object,
        }
    }

    constructor() {
        super();
    }

    createRenderRoot() {
        return this;
    }

    _handleClickThumbnailImg() {
        window.dispatchEvent(new CustomEvent(window.myEvents.showCategoryDetail, { detail: this.category.categoryID }));
    }

    _renderProductsOfCategory() {
        return html`
            ${this.category.productList.slice(0, 6).map(product =>  html`
                <div class="col-6">
                    <nos-cate-product-item
                        .productID=${product.productID}
                        .name=${product.name}
                        .price=${product.price}
                        .discount=${product.discount}
                        .thumbnail=${product.thumbnail}
                        .quantityInStock=${product.quantityInStock}
                    ></nos-cate-product-item>
                </div>
            `)}
        `;
    }

    render() {
        return html`
            <div class="cate-display">
                <div class="row">
                    <div class="col-xl-5">
                        <img
                            @click=${this._handleClickThumbnailImg}
                            width="100%" 
                            class="cate-display-thumbnail" 
                            alt="Category thumbnail" 
                            src="${window.APP_NAME}/resources/images/category/${this.category.thumbnail}" 
                        />
                    </div>
                    <div class="col-xl-7">
                        <div class="cate-display-products">
                            <div class="row">
                                ${this._renderProductsOfCategory()}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-category-home-page', CategoryHomePage);