/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nosify.session_beans;

import com.nosify.entities.Products;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ADMIN
 */
@Local
public interface ProductsFacadeLocal {

    void create(Products products);

    void edit(Products products);

    void remove(Products products);

    Products find(Object id);

    List<Products> findAll();

    List<Products> findRange(int[] range);

    int count();

    public List<Products> findLikeName(String name);

    public List<Products> findByListID(List<Integer> ids);

    public List<Products> findRelatedProducts(int mainProductID);
    
}
