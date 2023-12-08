import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class CategoryWrapperComponent extends LitElement {
    static properties = {
        _categoryID: {
            state: true,
        },
        _show: {
            state: true,
        }
    }

    constructor() {
        super();
        this._categoryID = 0;
        this._show = false;
    }

    connectedCallback() {
        super.connectedCallback();
        window.addEventListener(window.myEvents.showCategoryDetail, e => {
            this._categoryID = e.detail;
            this._show = true;
        })
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener(window.myEvents.showCategoryDetail)
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
                this._show ?
                html`<nos-category @close=${this._handleClose} categoryID=${this._categoryID}></nos-category>` : ''
            }
        `;
    }
}

customElements.define('nos-category-wrapper', CategoryWrapperComponent);