package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;

import java.util.List;

public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long> {
    List<Long> getListOfUsersIdFromQuestionViewedByQuestionIdCache(Long questionId);
    List<Long> refreshCache(Long questionId);
}
