package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
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
    public Long getReputationCount(Long questionId) {
        return reputationDao.getReputationCount(questionId);
    }
}
