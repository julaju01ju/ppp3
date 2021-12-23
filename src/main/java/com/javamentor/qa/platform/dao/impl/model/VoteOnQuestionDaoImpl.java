package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteOnQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * @author Maksim Solovev 23.12.2021.
 */

@Repository
public class VoteOnQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteOnQuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void insertUpVoteQuestion(Question question, User user) {

        VoteQuestion voteUp = new VoteQuestion();
        voteUp.setQuestion(question);
        voteUp.setUser(user);
        voteUp.setVote(VoteType.UP_VOTE);

        super.persist(voteUp);
    }

    @Override
    public Boolean getIfNotExists(Long questionId, Long userId, VoteType voteType) {
        TypedQuery<VoteQuestion> typedQuery = entityManager.createQuery(
                        "select vq from VoteQuestion vq where vq.question.id = :questionId and vq.user.id = :userId and vq.vote = :voteType",
                        VoteQuestion.class
                ).setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .setParameter("voteType", voteType);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    public void insertDownVoteQuestion(Question question, User user) {

        VoteQuestion voteDown = new VoteQuestion();
        voteDown.setQuestion(question);
        voteDown.setUser(user);
        voteDown.setVote(VoteType.DOWN_VOTE);

        super.persist(voteDown);
    }

    @Override
    public Long getCountOfVotes(Long questionId){
        Query query = entityManager.createQuery("select count(vq.vote) from VoteQuestion vq where vq.question.id = :questionId group by vq.question.id", Long.class)
                .setParameter("questionId", questionId);
        return ((Number) query.getResultList().get(0)).longValue();
    }

}
