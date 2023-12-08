package com.nosify.entities;

import com.nosify.entities.Products;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-10-30T18:29:53")
@StaticMetamodel(Categories.class)
public class Categories_ { 

    public static volatile SingularAttribute<Categories, String> thumbnail;
    public static volatile CollectionAttribute<Categories, Products> productsCollection;
    public static volatile SingularAttribute<Categories, String> name;
    public static volatile SingularAttribute<Categories, String> description;
    public static volatile SingularAttribute<Categories, Integer> categoryID;

}