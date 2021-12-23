package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteOnQuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maksim Solovev 23.12.2021.
 */

@Service
@Transactional
public class VoteOnQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteOnQuestionService {

    private VoteOnQuestionDao voteOnQuestionDao;

    @Autowired
    public VoteOnQuestionServiceImpl(VoteOnQuestionDao voteOnQuestionDao) {
        super(voteOnQuestionDao);
        this.voteOnQuestionDao = voteOnQuestionDao;
    }

    @Override
    public void insertUpVoteQuestion(Question question, User user) {
        voteOnQuestionDao.insertUpVoteQuestion(question, user);
    }

    @Override
    public void insertDownVoteQuestion(Question question, User user) {
        voteOnQuestionDao.insertDownVoteQuestion(question, user);
    }

    @Override
    public Boolean getIfNotExists(Long questionId, Long userId, VoteType voteType) {
        return voteOnQuestionDao.getIfNotExists(questionId, userId, voteType);
    }

    @Override
    public Long getCountOfVotes(Long questionId) {
        return voteOnQuestionDao.getCountOfVotes(questionId);
    }
}
