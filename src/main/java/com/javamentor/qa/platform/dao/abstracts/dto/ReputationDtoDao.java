package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

/**
 * @author Maksim Solovev 20.12.2021.
 */

public interface ReputationDtoDao extends ReadWriteDao<Reputation, Long> {
    Long getReputationCount();
}
