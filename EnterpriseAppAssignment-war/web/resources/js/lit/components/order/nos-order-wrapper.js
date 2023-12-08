import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class OrdersListWrapperComponent extends LitElement {
    static properties = {
        _show: {
            state: true,
        },
    }

    constructor() {
        super();
        this._show = false;
    }

    connectedCallback() {
        super.connectedCallback();
        window.addEventListener(window.myEvents.showOrderList, e => {
            this._show = true;
        })
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener(window.myEvents.showOrderList)
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
                this._show
                ?
                html`
                    <nos-order @close=${this._handleClose}></nos-order>
                `
                :
                ''
            }
        `;
    }
}

customElements.define('nos-order-wrapper', OrdersListWrapperComponent);