package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountValuable(Long question_id) {
        String sql = "select CAST(COALESCE(sum(case when voteQuestion.vote = 'UP_VOTE' then 1" +
                " when voteQuestion.vote = 'DOWN_VOTE' then -1" +
                " end),0) as int) AS countValuable" +
                " from VoteQuestion voteQuestion" +
                " where voteQuestion.question.id =: question_id";

        Query query = entityManager.createQuery(sql).setParameter("question_id", question_id);
        return (int) query.getResultStream().findFirst().get();
    }

    @Override
    public Optional<Question> getQuestionByDescriptionAndTitle(String description, String title) {
        String sql = "Select question from Question question" +
                " join fetch question.tags" +
                " where question.description = :description" +
                " and question.title = :title";
        TypedQuery<Question> query = entityManager.createQuery(sql, Question.class)
                .setParameter("description", description)
                .setParameter("title", title);
        return SingleResultUtil.getSingleResultOrNull(query);
    }


}
