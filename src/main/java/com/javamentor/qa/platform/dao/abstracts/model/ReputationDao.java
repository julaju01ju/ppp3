package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

import java.util.Optional;

/**
 * @author Maksim Solovev 20.12.2021.
 */

public interface ReputationDao extends ReadWriteDao<Reputation, Long> {

    void increaseReputationByQuestionVoteUp(Question question, User voteSender);

    void decreaseReputationByQuestionVoteDown(Question question, User voteSender);
}
