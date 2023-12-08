package com.nosify.entities;

import com.nosify.entities.Categories;
import com.nosify.entities.OrderDetails;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-10-30T18:29:53")
@StaticMetamodel(Products.class)
public class Products_ { 

    public static volatile SingularAttribute<Products, Integer> quantityInStock;
    public static volatile SingularAttribute<Products, String> thumbnail;
    public static volatile SingularAttribute<Products, Integer> productID;
    public static volatile SingularAttribute<Products, Double> price;
    public static volatile CollectionAttribute<Products, OrderDetails> orderDetailsCollection;
    public static volatile SingularAttribute<Products, String> name;
    public static volatile SingularAttribute<Products, Double> discount;
    public static volatile SingularAttribute<Products, String> description;
    public static volatile SingularAttribute<Products, Categories> categoryID;

}