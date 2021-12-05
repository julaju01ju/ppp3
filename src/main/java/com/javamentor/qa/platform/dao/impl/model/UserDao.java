package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class UserDao extends ReadWriteDaoImpl<User,Long>{

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDao(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(String email){
        Query query =  entityManager.createQuery("select u from User u where u.email=:email");
        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    @Override
    public void persist(User e) {
        String password = e.getPassword();
        e.setPassword(passwordEncoder.encode(password));
        super.persist(e);
    }

    @Override
    public void update(User e) {
        String password = e.getPassword();
        e.setPassword(passwordEncoder.encode(password));
        super.update(e);
    }
}
