package com.nosify.entities;

import com.nosify.entities.Orders;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-10-30T18:29:53")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> role;
    public static volatile SingularAttribute<Users, String> forgotPasswordCode;
    public static volatile SingularAttribute<Users, String> emailVerifyCode;
    public static volatile SingularAttribute<Users, String> fullname;
    public static volatile SingularAttribute<Users, String> avatar;
    public static volatile CollectionAttribute<Users, Orders> ordersCollection;
    public static volatile SingularAttribute<Users, Integer> userID;
    public static volatile SingularAttribute<Users, Date> emailVerifyAt;
    public static volatile SingularAttribute<Users, String> email;

}