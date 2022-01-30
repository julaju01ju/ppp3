package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> getUserByEmail(String username) {
        String hql = "select u from User u " +
                "join fetch u.role where u.email = :username";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("username", username);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public Optional<User> getById(Long id) {
        String hql = "select u from User u " +
                "join fetch u.role where u.id = :id";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
