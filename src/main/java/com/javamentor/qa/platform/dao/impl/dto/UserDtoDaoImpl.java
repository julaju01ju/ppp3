package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 29.11.2021
 */

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserById(Long id) {

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserDto (" +
                                "user.id, " +
                                "user.email, " +
                                "user.fullName, " +
                                "user.imageLink, " +
                                "user.city, " +
                                "(select coalesce (max(rep.count),0) from Reputation rep where rep.author.id =: id) " +
                                ") " +
                                "from User user where user.id =: id "
                        , UserDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }
}
