/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nosify.session_beans;

import com.nosify.entities.OrderDetails;
import com.nosify.entities.Orders;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author ADMIN
 */
@Stateless
public class OrderDetailsFacade extends AbstractFacade<OrderDetails> implements OrderDetailsFacadeLocal {

    @PersistenceContext(unitName = "EnterpriseAppAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderDetailsFacade() {
        super(OrderDetails.class);
    }
    
    public List<OrderDetails> findByOrderID(int orderID) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<OrderDetails> criteriaQuery = criteriaBuilder.createQuery(OrderDetails.class);
        
        Root<OrderDetails> orderDetailRoot = criteriaQuery.from(OrderDetails.class);
        
        Predicate whereOrderIDPredicate = criteriaBuilder.equal(orderDetailRoot.get("orderID"), orderID);
        
        criteriaQuery.where(whereOrderIDPredicate);
        
        try {
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
