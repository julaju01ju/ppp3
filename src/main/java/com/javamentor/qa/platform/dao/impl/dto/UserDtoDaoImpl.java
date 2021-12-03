package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ali Veliev 29.11.2021
 */

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public UserDto getUserById(Long id){
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDto(rep.id, rep.author.email, rep.author.fullName,rep.author.imageLink,rep.author.city,rep.count)" +
                        " from Reputation rep where rep.author.id =: id", UserDto.class)
                .setParameter("id", id).getSingleResult();
    }

}
