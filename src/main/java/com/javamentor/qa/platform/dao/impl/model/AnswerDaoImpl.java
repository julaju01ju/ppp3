package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountAnswer(Long question_id) {
        String sql = "select CAST(COALESCE(count(answer),0) as int) AS countAnswer" +
                " from Answer answer where answer.question.id =: question_id";
        Query query = entityManager.createQuery(sql).setParameter("question_id", question_id);
        return (int) query.getResultStream().findFirst().get();
    }
}
