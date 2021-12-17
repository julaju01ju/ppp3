package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountAnswer(Question question) {
        String sql = "select CAST(COALESCE(count(answer),0) as int) AS countAnswer" +
                " from Answer answer where answer.question_id = ?";
        Query query = entityManager.createNativeQuery(sql).setParameter(1, question.getId());
        return (int) query.getResultStream().findFirst().get();
    }

    @Override
    public int getCountValuable(Question question) {
        String sql = "select CAST(COALESCE(sum(case when voteQuestion.vote = 'UP_VOTE' then 1" +
                " when voteQuestion.vote = 'DOWN_VOTE' then -1" +
                " end),0) as int) AS countValuable" +
                " from votes_on_questions voteQuestion" +
                " where voteQuestion.question_id = ?";

        Query query = entityManager.createNativeQuery(sql).setParameter(1, question.getId());
        return (int) query.getResultStream().findFirst().get();
    }
}
