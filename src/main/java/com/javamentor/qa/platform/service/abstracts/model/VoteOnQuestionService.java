package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;

/**
 * @author Maksim Solovev 23.12.2021.
 */

public interface VoteOnQuestionService extends ReadWriteService<VoteQuestion, Long> {

    void insertUpVoteQuestion(Question question, User user);

    void insertDownVoteQuestion(Question question, User user);

    Boolean getIfNotExists(Long questionId, Long userId, VoteType voteType);

    Long getCountOfVotes(Long questionId);
}
