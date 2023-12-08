package com.nosify.controllers.api;

import com.nosify.entities.OrderDetails;
import com.nosify.entities.OrderDetailsPK;
import com.nosify.entities.Orders;
import com.nosify.entities.Products;
import com.nosify.entities.Users;
import com.nosify.enums.RequestAttributeKeys;
import com.nosify.models.order.AllOrdersResponse;
import com.nosify.models.order.ProductsOfOrder;
import com.nosify.models.order.UserOrderRequest;
import com.nosify.models.responses.DataResponse;
import com.nosify.models.responses.GeneralResponse;
import com.nosify.providers.StatusProvider;
import com.nosify.providers.UrlProvider;
import com.nosify.session_beans.OrderDetailsFacadeLocal;
import com.nosify.session_beans.OrdersFacadeLocal;
import com.nosify.session_beans.ProductsFacadeLocal;
import com.nosify.session_beans.UsersFacadeLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlProvider.API_PREFIX + UrlProvider.Order.PREFIX)
public class OrderApiController {

    ProductsFacadeLocal productsFacade = lookupProductsFacadeLocal();

    OrderDetailsFacadeLocal orderDetailsFacade = lookupOrderDetailsFacadeLocal();

    OrdersFacadeLocal ordersFacade = lookupOrdersFacadeLocal();

    UsersFacadeLocal usersFacade = lookupUsersFacadeLocal();
    
    @PostMapping(value = "" + UrlProvider.Order.CHECKOUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<?> checkout(@RequestBody @Valid UserOrderRequest data, BindingResult br, HttpServletRequest request) throws MethodArgumentNotValidException {
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }
        
        DataResponse<UserOrderRequest> res = new DataResponse<>();
        
        try {
            // Check quantity in stock has enough
            for (HashMap<String, Integer> productMap : data.getProductsData()) {
                Object productIDObj = productMap.get("productID");
                
                Object quantityObj = productMap.get("quantity");
                
                if (productIDObj == null || quantityObj == null) {
                    res.setSuccess(false);
                    res.setMessage("productsData must have id, quantity key");
                    res.setData(null);
                    return ResponseEntity.badRequest().body(res);
                }
                
                int quantity = (int) quantityObj;
                
                int productID = (int) productIDObj;
                
                Products product = productsFacade.find(productID);
                
                if (product == null) {
                    res.setSuccess(false);
                    res.setMessage("productID not found");
                    res.setData(null);
                    return ResponseEntity.badRequest().body(res);
                }
                
                // Minus quantity in stock
                int newQuantityInStock = product.getQuantityInStock() - quantity;
                if (newQuantityInStock < 0) {
                    res.setSuccess(false);
                    res.setMessage("Product with name " + product.getName() + " has quantity in stock is not enough!");
                    res.setData(null);
                    return ResponseEntity.badRequest().body(res);
                }
            }
            
            // Get UserID
            String userID = (String) request.getAttribute(RequestAttributeKeys.USER_ID.toString());
            
            Users user = usersFacade.find(Integer.parseInt(userID));
            if (user == null) return ResponseEntity.notFound().build();
            
            // Create order
            Orders order = new Orders();
        
            order.setUserID(user);
            order.setAddress(data.getAddress());
            order.setPhone(data.getPhone());
            order.setOrderDate(new Date());
            order.setDeliveriedDate(null);
            order.setStatus(0);
            
            ordersFacade.create(order);
            
            // Create order details
            for (HashMap<String, Integer> productMap : data.getProductsData()) {
                Object productIDObj = productMap.get("productID");
                
                Object quantityObj = productMap.get("quantity");
                
                int quantity = (int) quantityObj;
                
                int productID = (int) productIDObj;
                
                Products product = productsFacade.find(productID);
                
                OrderDetails orderDetail = new OrderDetails();
                
                orderDetail.setOrders(order);
                orderDetail.setProducts(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setUnitPrice(product.getPrice() * (1 - product.getDiscount()));
                
                // Add PK embeddable class
                OrderDetailsPK odPK = new OrderDetailsPK();
                odPK.setOrderID(order.getOrderID());
                odPK.setProductID(product.getProductID());
                orderDetail.setOrderDetailsPK(odPK);
                
                // Minus quantity in stock
                int newQuantityInStock = product.getQuantityInStock() - quantity;
                product.setQuantityInStock(newQuantityInStock);
                
                productsFacade.edit(product);
                orderDetailsFacade.create(orderDetail);
                
            }
            
            // Set status response
            res.setSuccess(true);
            res.setMessage("Successfully checkout");
            res.setData(data);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            
            res.setSuccess(false);
            res.setMessage(e.toString());
            res.setData(null);
        }
        
        return ResponseEntity.ok(res);
    }
    
    @GetMapping(value = "" + UrlProvider.Order.ALL_ORDERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> allOrders(HttpServletRequest request) {
        DataResponse<List<AllOrdersResponse>> res = new DataResponse<>();
        try {
            // Get UserID
            String userID = (String) request.getAttribute(RequestAttributeKeys.USER_ID.toString());
            
            // Tìm user bằng ID và kiểm tra
            Users user = usersFacade.find(Integer.parseInt(userID));
            if (user == null) return ResponseEntity.notFound().build();
            
            // Tạo list AllOrdersResponse
            List<AllOrdersResponse> allOrdersResponses = new ArrayList<>();
            
            for (Orders order : user.getOrdersCollection()) {
                // Tạo total price và danh sách sản phẩm
                int totalPrice = 40000;
                List<ProductsOfOrder> productsOfOrderList = new ArrayList<>();
                
                for (OrderDetails orderDetail : order.getOrderDetailsCollection()) {
                    Products product = orderDetail.getProducts();
                    
                    totalPrice += orderDetail.getQuantity() * (product.getPrice() * (1 - product.getDiscount()));
                    
                    productsOfOrderList.add(
                            ProductsOfOrder.builder()
                                    .productID(product.getProductID())
                                    .name(product.getName())
                                    .price(product.getPrice())
                                    .discount(product.getDiscount())
                                    .quantityInStock(product.getQuantityInStock())
                                    .orderQuantity(orderDetail.getQuantity())
                                    .orderUnitPrice(orderDetail.getUnitPrice())
                                    .thumbnail(product.getThumbnail())
                            .build()
                    );
                }
                
                allOrdersResponses.add(
                        AllOrdersResponse.builder()
                                .orderID(order.getOrderID())
                                .phone(order.getPhone())
                                .address(order.getAddress())
                                .orderDate(order.getOrderDate())
                                .deliveriedDate(order.getDeliveriedDate())
                                .status(StatusProvider.getStatus(order.getStatus()))
                                .totalPrice(totalPrice)
                                .ableToCancel(StatusProvider.ableToCancelStatus().contains(order.getStatus()))
                                .canceled(StatusProvider.canceledStatus().contains(order.getStatus()))
                                .productsOfOrder(productsOfOrderList)
                            .build()
                );
            }
            res.setSuccess(true);
            res.setMessage("Successfully get all orders of user!");
            res.setData(allOrdersResponses);
            return ResponseEntity.ok(res);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping(value = "" + UrlProvider.Order.USER_CANCEL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userCancelOrder(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            int orderID = Integer.parseInt(id);
            
            // Get UserID
            String userID = (String) request.getAttribute(RequestAttributeKeys.USER_ID.toString());
            
            System.out.println("USERID: " + userID);
            System.out.println("orderID: " + orderID);
            
            // Tìm user bằng ID và kiểm tra
            Users user = usersFacade.find(Integer.parseInt(userID));
            if (user == null) return ResponseEntity.notFound().build();
            
            Orders order = ordersFacade.find(orderID);
            
            if (order == null) {
                return ResponseEntity.notFound().build();
            } else if (StatusProvider.ableToCancelStatus().contains(order.getStatus())) {
                order.setStatus(StatusProvider.CANCEL_BY_USER);
                for (OrderDetails orderDetail : order.getOrderDetailsCollection()) {
                    Products product = orderDetail.getProducts();
                    int newQuantity = product.getQuantityInStock() + orderDetail.getQuantity();
                    product.setQuantityInStock(newQuantity);
                    
                    productsFacade.edit(product);
                }
                ordersFacade.edit(order);
                return ResponseEntity.ok(GeneralResponse.builder().success(true).message("Successfully cancel order").build());
            } else {
                return ResponseEntity.badRequest().body(GeneralResponse.builder().success(false).message("Cannot cancel order because order was deliveried"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private UsersFacadeLocal lookupUsersFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (UsersFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/UsersFacade!com.nosify.session_beans.UsersFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private OrdersFacadeLocal lookupOrdersFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (OrdersFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/OrdersFacade!com.nosify.session_beans.OrdersFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private OrderDetailsFacadeLocal lookupOrderDetailsFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (OrderDetailsFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/OrderDetailsFacade!com.nosify.session_beans.OrderDetailsFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ProductsFacadeLocal lookupProductsFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (ProductsFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/ProductsFacade!com.nosify.session_beans.ProductsFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
