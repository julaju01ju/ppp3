package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void changeReputationByQuestion(Question question, User voteSender, VoteType voteType) {
        Reputation reputationUp = new Reputation();
        reputationUp.setType(ReputationType.VoteQuestion);
        reputationUp.setQuestion(question);
        reputationUp.setAuthor(question.getUser());
        reputationUp.setSender(voteSender);
        reputationUp.setCount(voteType == VoteType.UP_VOTE ? 10 : 5);

        super.persist(reputationUp);
    }

    @Override
    @Transactional
    public void increaseReputationByQuestionVoteUp(Question question, User voteSender) {
        Reputation reputationUp = new Reputation();
        reputationUp.setType(ReputationType.VoteQuestion);
        reputationUp.setQuestion(question);
        reputationUp.setAuthor(question.getUser());
        reputationUp.setSender(voteSender);
        reputationUp.setCount(10);

        super.persist(reputationUp);
    }

    @Override
    @Transactional
    public void decreaseReputationByQuestionVoteDown(Question question, User voteSender) {
        Reputation reputationDown = new Reputation();
        reputationDown.setType(ReputationType.VoteQuestion);
        reputationDown.setQuestion(question);
        reputationDown.setAuthor(question.getUser());
        reputationDown.setSender(voteSender);
        reputationDown.setCount(5);

        super.persist(reputationDown);
    }
}