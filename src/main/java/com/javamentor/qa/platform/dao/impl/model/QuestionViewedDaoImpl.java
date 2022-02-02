package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "questionViewed", key = "#questionId")
    public List<Long> getListOfUsersIdFromQuestionViewedByQuestionIdCache(Long questionId) {
        String hql = "select q.user.id from QuestionViewed q where q.question.id = :questionId";
        return entityManager.createQuery(hql)
                .setParameter("questionId", questionId)
                .getResultList();
    }

    @Override
    @CachePut(value = "questionViewed", key = "#questionId")
    public List<Long> refreshCache(Long questionId) {
        return getListOfUsersIdFromQuestionViewedByQuestionIdCache(questionId);
    }
}
