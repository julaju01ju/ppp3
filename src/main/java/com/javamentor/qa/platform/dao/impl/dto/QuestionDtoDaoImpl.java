package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session session;

    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {


        return entityManager.createQuery("select q from Question q where q.id=:id", QuestionDto.class)
                .setParameter("id",id)
                .getResultStream().findAny();
    }
}
