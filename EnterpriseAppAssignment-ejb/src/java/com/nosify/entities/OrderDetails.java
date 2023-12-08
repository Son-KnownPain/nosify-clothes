/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nosify.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "OrderDetails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderDetails.findAll", query = "SELECT o FROM OrderDetails o"),
    @NamedQuery(name = "OrderDetails.findByOrderID", query = "SELECT o FROM OrderDetails o WHERE o.orderDetailsPK.orderID = :orderID"),
    @NamedQuery(name = "OrderDetails.findByProductID", query = "SELECT o FROM OrderDetails o WHERE o.orderDetailsPK.productID = :productID"),
    @NamedQuery(name = "OrderDetails.findByQuantity", query = "SELECT o FROM OrderDetails o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderDetails.findByUnitPrice", query = "SELECT o FROM OrderDetails o WHERE o.unitPrice = :unitPrice")})
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrderDetailsPK orderDetailsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UnitPrice")
    private double unitPrice;
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orders orders;
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Products products;

    public OrderDetails() {
    }

    public OrderDetails(OrderDetailsPK orderDetailsPK) {
        this.orderDetailsPK = orderDetailsPK;
    }

    public OrderDetails(OrderDetailsPK orderDetailsPK, int quantity, double unitPrice) {
        this.orderDetailsPK = orderDetailsPK;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderDetails(int orderID, int productID) {
        this.orderDetailsPK = new OrderDetailsPK(orderID, productID);
    }

    public OrderDetailsPK getOrderDetailsPK() {
        return orderDetailsPK;
    }

    public void setOrderDetailsPK(OrderDetailsPK orderDetailsPK) {
        this.orderDetailsPK = orderDetailsPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderDetailsPK != null ? orderDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderDetails)) {
            return false;
        }
        OrderDetails other = (OrderDetails) object;
        if ((this.orderDetailsPK == null && other.orderDetailsPK != null) || (this.orderDetailsPK != null && !this.orderDetailsPK.equals(other.orderDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nosify.entities.OrderDetails[ orderDetailsPK=" + orderDetailsPK + " ]";
    }
    
}
