// APP NAME
window.APP_NAME = document.querySelector('meta[name="my-app-name"]').content;

window.myFormatMoney = function(number) {
    // Sử dụng Intl.NumberFormat để định dạng số tiền
    const formatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0,
    });

    // Sử dụng hàm replace để loại bỏ ký tự đơn vị tiền tệ
    const result = formatter.format(number);
    return result.replace('₫', 'đ');
}

// Custom Events
window.myEvents = {
    showProductDetail: 'showproductdetail',
    showCategoryDetail: 'showcategorydetail',
    showOrderList: 'showOrderList',
    reFetchCart: 'reFetchCart',
}

const handlers = {
    dispatchShowOrderList() {
        window.dispatchEvent(new CustomEvent(window.myEvents.showOrderList, {}));
    }
}

window.myCart = {
    addToCart(id, quantity = 1) {
        try {
            // Get old cart
            const cart = this.getCart();

            let isAvai = false;
            
            //  Check +1 if it available
            cart.forEach(c => {
                if (c.id === id) {
                    isAvai = true;
                    c.quantity += quantity;
                }
            });
            
            if (!isAvai) {
                cart.push(
                    {
                        id,
                        quantity,
                        checked: true,
                    }
                );
            }

            this.pushToLocalStorage(cart);
        } catch (e) {
            console.error(e.message);
            this.pushToLocalStorage(JSON.stringify(
                [
                    {
                        id,
                        quantity,
                        checked: true,
                    }
                ]
            ));
        }
        this.reFetch()
    },
    decreaseQuantity(id) {
        try {
            // Get old cart
            const cart = this.getCart();

            let isFetch = false;
            
            //  Check +1 if it available
            cart.forEach(c => {
                if (c.id === id && c.quantity > 1) {
                    c.quantity--;
                    isFetch = true;
                }
            });

            this.pushToLocalStorage(cart);
            isFetch && this.reFetch();
        } catch (e) {
            console.error(e.message);
        }
    },
    removeItem(id) {
        try {
            // Get old cart
            let cart = this.getCart();
            
            //  Check +1 if it available
            cart = cart.filter(c => c.id !== id);

            this.pushToLocalStorage(cart);
        } catch (e) {
            console.error(e.message);
        }
        this.reFetch();
    },
    removeAllItem() {
        try {
            this.pushToLocalStorage([]);
        } catch (e) {
            console.error(e.message);
        }
        this.reFetch();
    },
    toggleChecked(id) {
        try {
            // Get old cart
            let cart = this.getCart();

            cart.forEach(c => {
                if (c.id === id) {
                    c.checked = !c.checked;
                }
            });

            this.pushToLocalStorage(cart);
        } catch (e) {
            console.error(e.message);
        }
        this.reFetch();
    },
    toggleCheckedAll(checked = false) {
        try {
            // Get old cart
            let cart = this.getCart();
            
            //  Check +1 if it available
            cart = cart.map(c => {
                c.checked = checked;
                return c;
            });

            this.pushToLocalStorage(cart);
        } catch (e) {
            console.error(e.message);
        }
        this.reFetch();
    },
    clearChecked() {
        try {
            // Get old cart
            let cart = this.getCart();
            
            //  Check +1 if it available
            cart = cart.filter(c => !c.checked);

            this.pushToLocalStorage(cart);
        } catch (e) {
            console.error(e.message);
        }
        this.reFetch();
    },
    getCart() {
        try {
            let cart = JSON.parse(localStorage.getItem('cart'));

            if (!Array.isArray(cart)) {
                return [];
            }

            cart = cart.filter(c => typeof c === 'object' && !Array.isArray(c) && c !== null);

            return cart;
        } catch (e) {
            console.error(e.message);
            return [];
        }
    },
    pushToLocalStorage(cart) {
        localStorage.setItem('cart', JSON.stringify(cart));
    },
    reFetch() {
        window.dispatchEvent(new CustomEvent(window.myEvents.reFetchCart, {}))
    },
}

// Handle is user login
function handleIsUserLogin() {
    const userLoggedIn = localStorage.getItem('user_logged_in');
    if (userLoggedIn) {
        document.querySelectorAll('.user-not-login-btn').forEach(btn => btn.style.display = 'none');
        document.querySelectorAll('.user-logged-in-btn').forEach(btn => btn.style.removeProperty('display'));
    } else {
        document.querySelectorAll('.user-logged-in-btn').forEach(btn => btn.style.display = 'none');
        document.querySelectorAll('.user-not-login-btn').forEach(btn => btn.style.removeProperty('display'));
    }
}
handleIsUserLogin();
