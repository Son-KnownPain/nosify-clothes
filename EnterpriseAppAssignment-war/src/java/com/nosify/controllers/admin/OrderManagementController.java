package com.nosify.controllers.admin;

import com.nosify.entities.OrderDetails;
import com.nosify.entities.Orders;
import com.nosify.entities.Products;
import com.nosify.models.common.AdminDeleteRecord;
import com.nosify.models.order.AdminEditOrder;
import com.nosify.models.order.AdminShowOrder;
import com.nosify.models.order.ProductsOfOrder;
import com.nosify.providers.AdminUrlProvider;
import com.nosify.providers.StatusProvider;
import com.nosify.session_beans.OrdersFacadeLocal;
import com.nosify.session_beans.ProductsFacadeLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(AdminUrlProvider.ADMIN_PREFIX + AdminUrlProvider.Order.PREFIX)
@SessionAttributes("statusOptions")
public class OrderManagementController {

    ProductsFacadeLocal productsFacade = lookupProductsFacadeLocal();

    OrdersFacadeLocal ordersFacade = lookupOrdersFacadeLocal();
    
    @GetMapping({AdminUrlProvider.Order.INDEX1, AdminUrlProvider.Order.INDEX2, AdminUrlProvider.Order.INDEX3})
    public ModelAndView index(@RequestParam(name = "q", required = false) String q) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        List<Orders> data = new ArrayList<>();
        
        if (q == null) {
            data = ordersFacade.findAll();
        } else {
            List<Orders> ordesrByID = new ArrayList<>();
            try {
                int id = Integer.parseInt(q);
                Orders order = ordersFacade.find(id);
                if (order != null) {
                    ordesrByID.add(order);
                }
                data = ordesrByID;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                data = ordersFacade.findAll();
            }
        }
        
        List<AdminShowOrder> orders = new ArrayList<>();
        
        Collections.reverse(data);
        
        for (Orders order : data) {
            int totalPrice = 40000;

            for (OrderDetails orderDetail : order.getOrderDetailsCollection()) {
                Products product = orderDetail.getProducts();

                totalPrice += orderDetail.getQuantity() * (product.getPrice() * (1 - product.getDiscount()));
            }
            
            orders.add(
                    AdminShowOrder.builder()
                            .orderID(order.getOrderID())
                            .phone(order.getPhone())
                            .address(order.getAddress())
                            .orderDate(formatter.format(order.getOrderDate()))
                            .deliveriedDate(order.getDeliveriedDate() != null ?formatter.format(order.getDeliveriedDate()) : "None")
                            .status(StatusProvider.getStatus(order.getStatus()))
                            .totalPrice(totalPrice)
                            .userID(order.getUserID())
                    .build()
            );
        }
        
        return new ModelAndView("/admin/order/index", "data", orders);
    }
    
    @GetMapping("" + AdminUrlProvider.Order.DETAIL)
    public ModelAndView detail(@PathVariable("id") String id) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            
            int orderID = Integer.parseInt(id);
            
            Orders order = ordersFacade.find(orderID);
            
            if (order == null) return new ModelAndView("redirect:/admin/order");
            
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
            
            AdminShowOrder orderDetail = AdminShowOrder.builder()
                            .orderID(order.getOrderID())
                            .phone(order.getPhone())
                            .address(order.getAddress())
                            .orderDate(formatter.format(order.getOrderDate()))
                            .deliveriedDate(order.getDeliveriedDate() != null ?formatter.format(order.getDeliveriedDate()) : "None")
                            .status(StatusProvider.getStatus(order.getStatus()))
                            .totalPrice(totalPrice)
                            .userID(order.getUserID())
                            .productsOfOrder(productsOfOrderList)
                    .build();
            
            return new ModelAndView("/admin/order/detail", "order", orderDetail);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/admin/order");
        }
    }
    
    @GetMapping("" + AdminUrlProvider.Order.EDIT)
    public ModelAndView edit(@PathVariable("id") String id, Model model) {
        try {
            int orderID = Integer.parseInt(id);
            
            Orders order = ordersFacade.find(orderID);
            
            if (order == null) return new ModelAndView("redirect:/admin/order");
            
            List<Map.Entry<Integer, String>> optionList = new ArrayList<>(StatusProvider.getStatus().entrySet());
            
            model.addAttribute("statusOptions", optionList);
            
            return new ModelAndView("/admin/order/edit", "orderEdit", order);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/admin/order");
        }
    }
    
    @PutMapping("" + AdminUrlProvider.Order.UPDATE)
    public String update(@Valid @ModelAttribute("orderEdit") AdminEditOrder data, BindingResult br) {
        try {
            Orders order = ordersFacade.find(data.getOrderID());
            
            if (order == null) return "redirect:/admin/order";
            
            if (!StatusProvider.getStatus().keySet().contains(data.getStatus())) {
                br.rejectValue("status", "error.status", "Invalid status");
            }
            if (br.hasErrors()) {
                return "/admin/order/edit";
            }
            
            int status = data.getStatus();
            
            if (status == StatusProvider.DELIVERIED && order.getDeliveriedDate() == null) {
                order.setDeliveriedDate(new Date());
            }
            
            if (StatusProvider.ableToCancelStatus().contains(status)) {
                for (OrderDetails orderDetail : order.getOrderDetailsCollection()) {
                    Products product = orderDetail.getProducts();
                    int newQuantity = product.getQuantityInStock() - orderDetail.getQuantity();
                    if (newQuantity >= 0) {
                        product.setQuantityInStock(newQuantity);
                        productsFacade.edit(product);
                    }
                }
            }
            
            if (StatusProvider.canceledStatus().contains(status)) {
                for (OrderDetails orderDetail : order.getOrderDetailsCollection()) {
                    Products product = orderDetail.getProducts();
                    int newQuantity = product.getQuantityInStock() + orderDetail.getQuantity();
                    product.setQuantityInStock(newQuantity);
                    
                    productsFacade.edit(product);
                }
            }
            
            order.setStatus(status);
            
            ordersFacade.edit(order);
            
            return "redirect:/admin/order";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "redirect:/admin/order";
        }
    }
    
    @GetMapping("" + AdminUrlProvider.Order.DELETE)
    public ModelAndView showDeleteConfirm(@PathVariable("id") int id) {
        Orders order = ordersFacade.find(id);
        
        if (order == null) return new ModelAndView("redirect:/admin/order");
        
        AdminDeleteRecord idObject = AdminDeleteRecord.builder().id(id).build();
        
        return new ModelAndView("/admin/order/delete", "orderDelete", idObject);
    }
    
    @DeleteMapping("" + AdminUrlProvider.Product.DELETE_CONFIRMATION)
    public String delete(@ModelAttribute("orderDelete") @Valid AdminDeleteRecord data, BindingResult br) {
        if (br.hasErrors()) return "/admin/order/delete";
        
        Orders order = ordersFacade.find(data.getId());
        
        if (order == null) return "redirect:/admin/order";
        
        ordersFacade.remove(order);
        
        return "redirect:/admin/order";
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
