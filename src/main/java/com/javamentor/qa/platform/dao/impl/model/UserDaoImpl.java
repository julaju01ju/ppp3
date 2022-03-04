package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
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
    CacheManager cacheManager;

    @Cacheable(value = "getUserByEmail", key = "#email")
    public Optional<User> getUserByEmail(String email) {
        checkIfExists(email);
        checkIfExists(email);
        String hql = "select u from User u " +
                "join fetch u.role where u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("email", email);
checkIfExists(email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    //@Cacheable(value = "checkIfExists", key = "#email", unless = "#email != null ")
    @Cacheable(value = "checkIfExists", key = "#email")
    public boolean checkIfExists(String email) {
//        if (count>0 == true){
//            cacheManager.getCache(email).clear();
//  }
        return (long) entityManager.createQuery(" SELECT COUNT(e) FROM User e"
                + "  WHERE e.email =: email").setParameter("email", email).getSingleResult()>0;
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

