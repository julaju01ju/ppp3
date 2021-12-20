package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

/**
 * @author Maksim Solovev 20.12.2021.
 */

public interface ReputationService extends ReadWriteService<Reputation, Long> {
    Long getReputationCount(Long questionId);
}
