package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;


public interface ReputationService extends ReadWriteService<Reputation, Long> {

    void changeReputationByQuestion(Question question, User voteSender, VoteType voteType);

    void increaseReputationByQuestionVoteUp(Question question, User voteSender);

    void decreaseReputationByQuestionVoteDown(Question question, User voteSender);
}
