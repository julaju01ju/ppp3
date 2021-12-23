package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Maksim Solovev 20.12.2021.
 */

@Service
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {

    private ReputationDao reputationDao;

    @Autowired
    public ReputationServiceImpl(ReputationDao reputationDao) {
        super(reputationDao);
        this.reputationDao = reputationDao;
    }

    @Override
    public void increaseReputationByQuestionVoteUp(Question question, User voteSender) {
        reputationDao.increaseReputationByQuestionVoteUp(question, voteSender);
    }

    @Override
    public void decreaseReputationByQuestionVoteDown(Question question, User voteSender) {
        reputationDao.decreaseReputationByQuestionVoteDown(question, voteSender);
    }
}
