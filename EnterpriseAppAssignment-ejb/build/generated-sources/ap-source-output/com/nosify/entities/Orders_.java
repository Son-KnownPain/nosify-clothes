package com.nosify.entities;

import com.nosify.entities.OrderDetails;
import com.nosify.entities.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-10-30T18:29:53")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, String> address;
    public static volatile SingularAttribute<Orders, Integer> orderID;
    public static volatile SingularAttribute<Orders, String> phone;
    public static volatile CollectionAttribute<Orders, OrderDetails> orderDetailsCollection;
    public static volatile SingularAttribute<Orders, Date> orderDate;
    public static volatile SingularAttribute<Orders, Date> deliveriedDate;
    public static volatile SingularAttribute<Orders, Users> userID;
    public static volatile SingularAttribute<Orders, Integer> status;

}