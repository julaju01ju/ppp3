package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDAO;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> getUserByEmail(String username) {
        String hql = "select u from User u " +
                "join fetch u.role where u.email = :username";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("username", username);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllByIds(Iterable<Long> ids) {
        return null;
    }

    @Override
    public boolean existsByAllIds(Collection<Long> ids) {
        return false;
    }

    @Override
    public void persist(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void persistAll(User... entities) {

    }

    @Override
    public void persistAll(Collection<User> entities) {

    }

    @Override
    public void deleteAll(Collection<User> entities) {

    }

    @Override
    public void updateAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
