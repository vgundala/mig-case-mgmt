package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.util.PasswordUtil;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UserService implements UserServiceLocal {

    @PersistenceContext(unitName = "case-management-pu")
    private EntityManager em;

    public void registerUser(User user) {
        // Hash the password before persisting
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        em.persist(user);
    }

    public User login(String username, String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);

        try {
            User user = query.getSingleResult();
            if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
                return user;
            }
        } catch (javax.persistence.NoResultException e) {
            // User not found, return null
        }
        return null;
    }
}