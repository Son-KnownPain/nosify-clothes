/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nosify.session_beans;

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
public class OrdersFacade extends AbstractFacade<Orders> implements OrdersFacadeLocal {

    @PersistenceContext(unitName = "EnterpriseAppAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdersFacade() {
        super(Orders.class);
    }
    
    public List<Orders> findByUserID(int userID) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Orders> criteriaQuery = criteriaBuilder.createQuery(Orders.class);
        
        Root<Orders> orderRoot = criteriaQuery.from(Orders.class);
        
        Predicate whereUserIDPredicate = criteriaBuilder.equal(orderRoot.get("userID"), userID);
        
        criteriaQuery.where(whereUserIDPredicate);
        
        try {
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
