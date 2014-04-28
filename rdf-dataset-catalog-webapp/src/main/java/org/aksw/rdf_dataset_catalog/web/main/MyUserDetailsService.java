package org.aksw.rdf_dataset_catalog.web.main;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.aksw.rdf_dataset_catalog.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service("authService")
public class MyUserDetailsService
    implements UserDetailsService
{
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager newEm){
        this.entityManager = newEm;
    }

    public UserDetails loadUserByUsername(String username){

        // assuming that you have a User class that implements UserDetails
        return entityManager.createQuery("from User where username = :username", UserInfo.class)
                            .setParameter("username", username)
                            .getSingleResult();

    }
}