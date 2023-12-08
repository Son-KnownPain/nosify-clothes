import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class HomePage extends LitElement {
    static properties = {
        categoriesWithThumbnail: {
            state: true,
        },
        categoriesWithoutThumbnail: {
            state: true,
        },
        categories: {
            state: true,
        },
    }

    constructor() {
        super();
        this.categoriesWithThumbnail = [];
        this.categoriesWithoutThumbnail = [];
        this.categories = [];
    }

    firstUpdated() {
        fetch(`${window.APP_NAME}/api/category/all`)
            .then(response => response.json())
            .then(data => {
                this.categories = data;
            });
    }

    createRenderRoot() {
        return this;
    }

    _handleClickShowCategory(id) {
        return () => {
            window.dispatchEvent(new CustomEvent(window.myEvents.showCategoryDetail, { detail: id }));
        }
    }

    _renderCategoriesWithThumbnail() {
        return html`
            ${this.categories.filter(c => c.thumbnail != null).map(category => html`<nos-category-home-page .category=${category}></nos-category-home-page>`)}
        `;
    }

    _renderCategoriesWithoutThumbnail() {
        return html`
            ${this.categories.filter(c => c.thumbnail == null).map(category => 
                html`<a @click=${this._handleClickShowCategory(category.categoryID)} class="other-categories--item">${category.name}</a>`
            )}
        `;
    }

    render() {
        return html`
            <div class="container">
                ${this._renderCategoriesWithThumbnail()}
                <div class="other-categories">
                    <h2 class="other-categories--heading">
                        Other Categories
                    </h2>
                    <div class="other-categories--content">
                        ${this._renderCategoriesWithoutThumbnail()}
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-home-page', HomePage);