package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;

public interface ReputationDao extends ReadWriteDao<Reputation, Long> {

    void changeReputationByQuestion(Question question, User voteSender, VoteType voteType);

}
