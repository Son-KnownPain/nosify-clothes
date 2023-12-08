package com.nosify.session_beans;

import com.nosify.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class UsersFacade extends AbstractFacade<Users> implements UsersFacadeLocal {

    @PersistenceContext(unitName = "EnterpriseAppAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }
    
    @Override
    public Users findByEmail(String email) {
        // Criteria Query
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
        Root<Users> root = criteriaQuery.from(Users.class);
        
        Predicate byEmailPredicate = criteriaBuilder.equal(root.get("email"), email);
        
        criteriaQuery.where(byEmailPredicate);
        
        TypedQuery<Users> query = em.createQuery(criteriaQuery);
        
        try {
            Users user = query.getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public List<Users> findLikeName(String name) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
        
        Root<Users> root = criteriaQuery.from(Users.class);
        
        Path<String> fullnamePath = root.get("fullname");
        
        Predicate likePredicate = criteriaBuilder.like(fullnamePath, "%" + name + "%");
        
        criteriaQuery.where(likePredicate);
        
        TypedQuery<Users> query = em.createQuery(criteriaQuery);
        
        return query.getResultList();
    }
    
}
