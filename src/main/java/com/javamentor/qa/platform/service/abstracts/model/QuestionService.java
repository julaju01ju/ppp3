package com.javamentor.qa.platform.models.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;

public interface QuestionService extends ReadWriteService<Question, Long> {
    Long getQuestionCount();
}
