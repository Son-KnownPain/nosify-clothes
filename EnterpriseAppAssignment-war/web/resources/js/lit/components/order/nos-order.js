import { LitElement, html } from 'https://cdn.jsdelivr.net/gh/lit/dist@2/all/lit-all.min.js';

class OrdersListComponent extends LitElement {
    static properties = {
        _orders: {
            state: true,
        },
        _orderIDCancelQuestion: {
            state: true,
        },
    }

    constructor() {
        super();

        this._orders = [];
        this._orderIDCancelQuestion = null;
    }

    _fetchOrdersData() {
        fetch(`${window.APP_NAME}/api/order/all`)
        .then(response => response.json())
        .then(res => {
            if (res.success) {
                this._orders = res.data;
            }
        });
    }

    firstUpdated() {
        this._fetchOrdersData();
    }

    _parseTimestamp(timestamp) {

        // Tạo một đối tượng Date từ timestamp
        const date = new Date(timestamp);

        // Lấy giờ, phút và giây từ đối tượng Date
        const hours = date.getHours();
        const minutes = date.getMinutes();
        const seconds = date.getSeconds();

        // Lấy ngày, tháng và năm từ đối tượng Date
        const day = date.getDate();
        const month = date.getMonth() + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
        const year = date.getFullYear();

        // Định dạng các giá trị để có dạng HH:mm:ss dd/MM/yyyy
        const formattedTime = `${hours}:${minutes}:${seconds}`;
        const formattedDate = `${day}/${month}/${year}`;

        // Kết hợp giờ và ngày để có định dạng cuối cùng
        return `${formattedTime} ${formattedDate}`;
    }

    _handleClickClose() {
        this.dispatchEvent(new CustomEvent('close', {}));
    }

    _handleClickCancel(id) {
        return () => {
            this._orderIDCancelQuestion = id;   
        }
    }

    _handleYesChoiceCancel() {
        if (this._orderIDCancelQuestion) {
            showLoader();
            fetch(`${window.APP_NAME}/api/order/cancel/${this._orderIDCancelQuestion}`, {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'X-HTTP-Method-Override': 'PUT'
                },
            })
            .then(res => res.json())
            .then(res => {
                if(res.success) {
                    toast({
                        title: 'Cancel Order',
                        message: res.message,
                        type: 'success',
                    });
                    this._fetchOrdersData()
                }
                hideLoader();
            })
        }
    }

    _renderOrders() {
        return html`
            ${
                this._orders.map(o => {
                    return html`
                        <div class="order-item ${o.canceled ? 'canceled' : ''}">
                            <div class="orderInfo">
                                <span class="orderPart">
                                    <i class="fa-regular fa-calendar"></i>
                                    ${this._parseTimestamp(o.orderDate)}
                                </span>
                                <span class="orderPart">
                                    <i class="fa-solid fa-truck"></i>
                                    ${o.status}
                                </span>
                                <span class="orderPart">
                                    <i class="fa-solid fa-box-open"></i>    
                                    ${o.productsOfOrder?.length ?? 'No product'}
                                </span>
                                <span class="orderPart">
                                    <i class="fa-solid fa-money-bill"></i>
                                    ${window.myFormatMoney(o.totalPrice)}
                                </span>
                                <span class="orderPart">
                                    <i class="fa-solid fa-phone"></i>
                                    ${o.phone}
                                </span>
                                <span class="orderPart">
                                    <i class="fa-solid fa-location-dot"></i>
                                    ${o.address}
                                </span>
                            </div>
                            <div class="orderProducts">
                                <div class="row">
                                    ${
                                        o.productsOfOrder.map(item => {
                                            return html`
                                                <div class="col-6">
                                                    <div class="orderProducts-item">
                                                        <div class="row">
                                                            <div class="col-4">
                                                                <img width="100%" src="${window.APP_NAME}/resources/images/product/${item.thumbnail}" alt="Product" />
                                                            </div>
                                                            <div class="col-8">
                                                                <div class="orderProducts-body">
                                                                    <h4 class="orderProducts-item--name">
                                                                        ${item.name}
                                                                    </h4>
                                                                    <p class="orderProducts-item--quantity">
                                                                        <span>${item.orderQuantity}</span>
                                                                    </p>
                                                                    <p class="orderProducts-item--unitPrice">
                                                                        ${window.myFormatMoney(item.orderUnitPrice)}
                                                                    </p>
                                                                    <p class="orderProducts-item--totalPrice">
                                                                        ${window.myFormatMoney(item.orderUnitPrice * item.orderQuantity)}
                                                                    </p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            `;
                                        })
                                    }
                                </div>
                            </div>
                            ${
                                o.ableToCancel
                                ?
                                html`
                                    <div class="orderActions">
                                        ${
                                            (o.orderID == this._orderIDCancelQuestion)
                                            ?
                                            html`
                                                <button @click=${() => this._orderIDCancelQuestion = null} class="orderActions-item" style="background-color: #6e7f80">
                                                    No
                                                </button>
                                                <button @click=${this._handleYesChoiceCancel} class="orderActions-item" style="background-color: #006a4e">
                                                    Yes
                                                </button>
                                            `
                                            :
                                            html`
                                                <button @click=${this._handleClickCancel(o.orderID)} class="orderActions-item">
                                                    Cancel
                                                </button>
                                            `
                                        }
                                    </div>
                                `
                                : ''
                            }
                        </div>
                    `;
                })
            }
        `;
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div id="order-list-modal">
                <div class="content-showed">
                    <div class="close-box">
                        <span class="close-btn" @click=${this._handleClickClose}>
                            <i class="fa-solid fa-xmark"></i>
                        </span>
                    </div>

                    <div class="content">
                        ${this._renderOrders()}
                    </div>
                </div>
            </div>
        `;
    }
}

customElements.define('nos-order', OrdersListComponent);