package com.nosify.entities;

import com.nosify.entities.OrderDetailsPK;
import com.nosify.entities.Orders;
import com.nosify.entities.Products;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-10-30T18:29:53")
@StaticMetamodel(OrderDetails.class)
public class OrderDetails_ { 

    public static volatile SingularAttribute<OrderDetails, Double> unitPrice;
    public static volatile SingularAttribute<OrderDetails, OrderDetailsPK> orderDetailsPK;
    public static volatile SingularAttribute<OrderDetails, Integer> quantity;
    public static volatile SingularAttribute<OrderDetails, Orders> orders;
    public static volatile SingularAttribute<OrderDetails, Products> products;

}