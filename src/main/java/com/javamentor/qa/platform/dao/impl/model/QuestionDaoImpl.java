package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Long getQuestionCount() {
        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM question WHERE is_deleted = false");
        Object count = query.getResultList().get(0);
        return ((Number) count).longValue();
    }
}
