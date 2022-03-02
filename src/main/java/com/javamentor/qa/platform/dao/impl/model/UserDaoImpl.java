package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;


@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Cacheable(value = "getUserByEmail", key = "#email")
    public Optional<User> getUserByEmail(String email) {
        String hql = "select u from User u " +
                "join fetch u.role where u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Cacheable(value = "user", key = "#email")
    public boolean checkIsExists(String email) {
        long count = (long) entityManager.createQuery("SELECT COUNT(e)  FROM User e"
                + "  WHERE e.email =: email").setParameter("email", email).getSingleResult();
        return count > 0;
    }

    @CacheEvict(value = "getUserByEmail", key = "#email")
    public void updatePasswordByEmail(String email, String password) {
        String hql = "update User u set u.password = :password where u.email = :email";
        entityManager.createQuery(hql)
                .setParameter("password", password)
                .setParameter("email", email).executeUpdate();
    }

    @Override
    @CacheEvict(value = "getUserByEmail", key = "#email")
    public void disableUserByEmail(String email) {
        String hql = "update User u set u.isEnabled = false where u.email = :email";
        entityManager.createQuery(hql).setParameter("email", email).executeUpdate();
    }


}

