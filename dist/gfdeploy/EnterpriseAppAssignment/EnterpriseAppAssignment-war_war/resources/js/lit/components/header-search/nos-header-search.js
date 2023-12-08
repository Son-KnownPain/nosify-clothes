import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class HeaderSearch extends LitElement {

    static properties = {
        _searchKeyword: {
            state: true,
        },
        _showList: {
            state: true,
        },
        _result: {
            state: true,
        },
        _processData: {
            state: true,
        },
        _showLoader: {
            state: true,
        },
        _categories: {
            state: true,
        },
        _categoryID: {
            state: true,
        },
        _orderByPriceList: {
            state: true,
        },
        _orderByPrice: {
            state: true,
        },
    }

    constructor() {
        super();
        // Define States
        this._searchKeyword = '';
        this._showList = false;
        this._result = [];
        this._showLoader = false;
        this._categories = [];
        this._categoryID = -1;
        this._orderByPriceList = [
            {
                value: 'none',
                text: 'No sorting price'
            },
            {
                value: 'lth',
                text: 'Price from low to high'
            },
            {
                value: 'htl',
                text: 'Price from high to low'
            },
        ];
        this._orderByPrice = 'none';

        function debounce(func, timeout = 300){
            let timer;
            return (...args) => {
              clearTimeout(timer);
              timer = setTimeout(() => { func.apply(this, args); }, timeout);
            };
        }
        this._processData = debounce(() => {
            this._showLoader = true;
            fetch(`${window.APP_NAME}/api/product/by-name?name=${this._searchKeyword}`)
                .then(response => response.json())
                .then(data => {
                    this._result = data;
                    this._showLoader = false;
                })
        })
    }

    firstUpdated() {
        fetch(`${window.APP_NAME}/api/category/all`)
            .then(response => response.json())
            .then(data => {
                this._categories = data;
            })
    }

    _handleShowAutocomplete() {
        this._showList = true;
    }

    _handleHideAutocomplete() {
        this._showList = !!this._searchKeyword;
    }

    _handleTypingKeyword(e) {
        this._searchKeyword = e.target.value;
        if (this._searchKeyword.length !== 0) {
            this._processData();
        } else {
            this._result = [];
        }
    }

    _handleClear() {
        this._showList = false;
    }

    _renderResultList() {
        const result = this._result.filter(item => this._categoryID == -1 ? true : item.categoryID == this._categoryID);

        switch(this._orderByPrice) {
            case 'htl':
                result.sort((a, b) => {
                    const aRPrice = a.price * (1 - a.discount);
                    const bRPrice = b.price * (1 - b.discount);

                    return bRPrice - aRPrice;
                })
                break;
            case 'lth':
                result.sort((a, b) => {
                    const aRPrice = a.price * (1 - a.discount);
                    const bRPrice = b.price * (1 - b.discount);

                    return aRPrice - bRPrice;
                })
                break;
            default:
        }

        if (result.length === 0 && this._searchKeyword.length !== 0) {
            return html`
                <h4 style="text-align: center; margin-bottom: 0; font-weight: 400;">No result for that.</h4>
            `;
        } else if (result.length === 0 && this._searchKeyword.length === 0) {
            return html`
                <h4 style="text-align: center; margin-bottom: 0; font-weight: 400;">Let typing a name of product to search.</h4>
            `;
        }

        return html`
            ${result.map(item => html`
                <nos-header-search-item 
                    @clear=${this._handleClear}
                    .productID=${item.productID}
                    .name=${item.name}
                    .price=${item.price}
                    .discount=${item.discount}
                    .description=${item.description}
                    .quantityInStock=${item.quantityInStock}
                    .thumbnail=${item.thumbnail}
                ></nos-header-search-item>
            `)}
        `;
    }

    _renderCategoriesSelector() {
        return html`
            <select @change=${e => this._categoryID = parseInt(e.target.value)} class="search-autocomplete--select">
                <option value="-1">All categories</option>
                ${this._categories.map(c => html`<option ?selected=${this._categoryID == c.categoryID} value="${c.categoryID}">${c.name}</option>`)}
            </select>
        `
    }

    _renderPriceFilterSelector() {
        return html`
            <select @change=${e => this._orderByPrice = e.target.value} class="search-autocomplete--select">
                ${this._orderByPriceList.map(item => html`<option ?selected=${this._orderByPrice == item.value} value="${item.value}">${item.text}</option>`)}
            </select>
        `
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div class="search">
                <form class="saerch-form" class="flx-all-center" action="">
                    <input 
                        @blur=${this._handleHideAutocomplete} 
                        @focus=${this._handleShowAutocomplete} 
                        @input=${this._handleTypingKeyword}
                        autocomplete="off" 
                        placeholder="Search..." 
                        class="search-input" 
                        type="text" 
                        name="keyword" 
                        id="keyword"
                    >

                    ${
                        this._showLoader ? 
                        html`
                            <span class="search-load-icon">
                                <i class="fa-solid fa-spinner"></i>
                            </span>
                        ` : ''
                    }

                    ${
                        this._showList ? 
                        html`
                            <div class="search-autocomplete">
                                ${
                                    this._searchKeyword.length !== 0 ?
                                    html`
                                        <div class="search-autocomplete--filter">
                                            ${this._renderCategoriesSelector()}
                                            ${this._renderPriceFilterSelector()}
                                        </div>
                                    ` : ''
                                }
                                <ul>
                                    ${this._renderResultList()}
                                </ul>
                            </div>
                        ` : ''
                    }
                </form>
            </div>
        `;
    }

}

customElements.define('nos-header-search', HeaderSearch);