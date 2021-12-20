package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Maksim Solovev 20.12.2021.
 */
@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getReputationCount(Long questionId) {
        return (Long) entityManager.createNativeQuery("UPDATE reputation set count = case when voQ.vote = 'UP_VOTE' " +
                "THEN reputation.count + 10 " +
                "ELSE reputation.count - 5 " +
                "END " +
                "FROM reputation rep join votes_on_questions voQ on voQ.question_id = ?questionId " +
                "select count from reputation where question_id = ?questionId;").setParameter("questionId", questionId).getSingleResult();
    }
}
