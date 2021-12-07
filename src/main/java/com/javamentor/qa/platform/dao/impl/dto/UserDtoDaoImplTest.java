package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDaoTest;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDtoDaoImplTest implements UserDtoDaoTest {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public List<UserDtoTest> getAllUsers() {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDtoTest(tb.id, tb.name) from UserTest as tb").getResultList();
    }
}
