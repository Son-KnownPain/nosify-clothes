package com.nosify.session_beans;

import com.nosify.entities.Products;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class ProductsFacade extends AbstractFacade<Products> implements ProductsFacadeLocal {

    @PersistenceContext(unitName = "EnterpriseAppAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductsFacade() {
        super(Products.class);
    }
    
    @Override
    public List<Products> findLikeName(String name) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
        
        Root<Products> root = criteriaQuery.from(Products.class);
        
        Path<String> pathString = root.get("name");
        
        Predicate likePredicate = criteriaBuilder.like(pathString, "%" + name + "%");
        
        criteriaQuery.where(likePredicate);
        
        TypedQuery<Products> query = em.createQuery(criteriaQuery);
        
        return query.getResultList();
    }
    
    @Override
    public List<Products> findByListID(List<Integer> ids) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
        
        Root<Products> root = criteriaQuery.from(Products.class);
        
        Predicate inPredicate = root.get("productID").in(ids);
        
        criteriaQuery.where(inPredicate);
        
        TypedQuery<Products> query = em.createQuery(criteriaQuery);
        
        return query.getResultList();
    }
    
    @Override
    public List<Products> findRelatedProducts(int mainProductID) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery relatedCriteriaQuery = criteriaBuilder.createQuery(Products.class);
        Root<Products> productRoot = relatedCriteriaQuery.from(Products.class);
        
        Products mainProduct = this.find(mainProductID);
        
        if (mainProduct == null) {
            return new ArrayList<Products>();
        }
        
        int cateID = mainProduct.getCategoryID().getCategoryID();
        
        double oPrice = mainProduct.getPrice();
        double discount = mainProduct.getDiscount();

        double rPrice = (1 - discount) * oPrice;


        Predicate cateIDPredicate = criteriaBuilder.equal(productRoot.get("categoryID").get("categoryID"), cateID);
        
        Predicate otherProIDPredicate = criteriaBuilder.notEqual(productRoot.get("productID"), mainProduct.getProductID());
        
        Predicate priceCondition1 = criteriaBuilder.ge(
            criteriaBuilder.prod(
                productRoot.get("price").as(Double.class), 
                criteriaBuilder.diff(
                    1.0, productRoot.get("discount").as(Double.class)
                )
            ),
            rPrice / 2
        );
        
        Predicate priceCondition2 = criteriaBuilder.le(
            criteriaBuilder.prod(
                        productRoot.get("price").as(Double.class), 
                        criteriaBuilder.diff(
                                1.0, productRoot.get("discount").as(Double.class)
                        )
                ),
            rPrice * (3/2)
        );
        
        Predicate finalPredicate = criteriaBuilder.and(otherProIDPredicate,  cateIDPredicate, priceCondition1,priceCondition2);
        
        relatedCriteriaQuery.where(finalPredicate);
        
        TypedQuery<Products> query = em.createQuery(relatedCriteriaQuery);
        
        try {
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Products>();
        }
    }
    
}
