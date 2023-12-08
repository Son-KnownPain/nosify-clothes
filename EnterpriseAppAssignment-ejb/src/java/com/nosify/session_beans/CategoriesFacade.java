/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nosify.session_beans;

import com.nosify.entities.Categories;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author ADMIN
 */
@Stateless
public class CategoriesFacade extends AbstractFacade<Categories> implements CategoriesFacadeLocal {

    @PersistenceContext(unitName = "EnterpriseAppAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriesFacade() {
        super(Categories.class);
    }
    
    @Override
    public List<Categories> getCategoriesesByThumbnail(boolean isHasThumbnail) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Categories> criteriaQuery = criteriaBuilder.createQuery(Categories.class);
        
        Root<Categories> root = criteriaQuery.from(Categories.class);
        
        Predicate nullOrNotPredicate;
        
        if (isHasThumbnail) {
            nullOrNotPredicate = criteriaBuilder.isNotNull(root.get("thumbnail"));
        } else {
            nullOrNotPredicate = criteriaBuilder.isNull(root.get("thumbnail"));
        }
        
        criteriaQuery.where(nullOrNotPredicate);
        
        TypedQuery<Categories> query = em.createQuery(criteriaQuery);
        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
 }
