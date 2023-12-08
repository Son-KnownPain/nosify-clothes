import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class NameComponent extends LitElement {
    static properties = {
        
    }

    constructor() {
        super();
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            
        `;
    }
}

customElements.define('nos-name', NameComponent);