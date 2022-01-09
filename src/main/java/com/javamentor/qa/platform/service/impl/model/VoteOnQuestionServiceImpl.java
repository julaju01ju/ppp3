package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteOnQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @Transactional
    public Boolean getIfNotExists(Long questionId, Long userId) {
        return voteOnQuestionDao.getIfNotExists(questionId, userId);
    }

    @Override
    public Long getCountOfVotes(Long questionId) {
        return voteOnQuestionDao.getCountOfVotes(questionId);
    }
}
