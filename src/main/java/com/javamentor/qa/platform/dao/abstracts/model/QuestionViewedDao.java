package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;

public interface QuestionViewedDao extends ReadWriteDao<QuestionViewed, Long> {

    boolean questionIdIsPresentForAuthUser(Long questionId, Long authUser);
}
