import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class ProductDetailComponent extends LitElement {
    static properties = {
        _show: {
            state: true,
        },
        _productID: {
            state: true,
        }
    }

    constructor() {
        super();
        this._show = false;
        this._productID = null;
    }

    connectedCallback() {
        super.connectedCallback();
        window.addEventListener(window.myEvents.showProductDetail, e => {
            this._productID = e.detail;
            this._show = true;
        })
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener(window.myEvents.showProductDetail)
    }

    _handleClose() {
        this._show = false;
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            ${
                this._show ? html`<nos-product-detail @close=${this._handleClose} .productID=${this._productID}></nos-product-detail>`
                : ''
            }
        `;
    }
}

customElements.define('nos-product-detail-wrapper', ProductDetailComponent);