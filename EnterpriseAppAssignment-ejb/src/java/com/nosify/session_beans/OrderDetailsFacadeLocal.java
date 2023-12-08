/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nosify.session_beans;

import com.nosify.entities.OrderDetails;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ADMIN
 */
@Local
public interface OrderDetailsFacadeLocal {

    void create(OrderDetails orderDetails);

    void edit(OrderDetails orderDetails);

    void remove(OrderDetails orderDetails);

    OrderDetails find(Object id);

    List<OrderDetails> findAll();

    List<OrderDetails> findRange(int[] range);

    int count();

    public List<OrderDetails> findByOrderID(int orderID);
    
}
