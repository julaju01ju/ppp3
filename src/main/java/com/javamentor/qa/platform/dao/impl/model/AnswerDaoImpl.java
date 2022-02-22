package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer,Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean getIfNotExists(Long questionId, Long userId) {
        TypedQuery<Answer> typedQuery = entityManager.createQuery(
                        "select a from Answer a where a.question.id = :questionId and a.user.id = :userId",
                        Answer.class
                ).setParameter("questionId", questionId)
                .setParameter("userId", userId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }
}