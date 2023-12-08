import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class HeaderSearchItem extends LitElement {
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
        description: {
            type: String,
            reflect: true,
        },
        quantityInStock: {
            type: Number,
            reflect: true,
        },
        thumbnail: {
            type: String,
            reflect: true,
        },
    }

    constructor() {
        super();
    }

    _formatMoney(number) {
        // Sử dụng Intl.NumberFormat để định dạng số tiền
        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0,
        });

        // Sử dụng hàm replace để loại bỏ ký tự đơn vị tiền tệ
        return formatter.format(number).replace('₫', 'đ');
    }

    _handleClickItem() {
        window.dispatchEvent(new CustomEvent(window.myEvents.showProductDetail, { detail: this.productID }));
        this.dispatchEvent(new CustomEvent('clear', {  }));
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <li @click=${this._handleClickItem}>
                <a class="search-itemLink">
                    <div class="row">
                        <div class="col-2">
                            <img width="100%" src="${window.APP_NAME}/resources/images/product/${this.thumbnail}" alt="Thumbnail">
                        </div>
                        <div class="col-7">
                            <div class="onepercent-height flx-all-center">
                                <h4 class="item-name">
                                    ${this.name}
                                </h4>
                            </div>
                        </div>
                        <div class="col-3">
                            <div class="onepercent-height flx-all-center">
                                <h5 class="item-price">
                                    ${this._formatMoney(this.price * (1 - this.discount))}
                                </h5>
                            </div>
                        </div>
                    </div>
                </a>
            </li>
        `;
    }
}

customElements.define('nos-header-search-item', HeaderSearchItem);