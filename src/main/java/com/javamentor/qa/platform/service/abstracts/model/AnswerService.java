package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;

public interface AnswerService extends ReadWriteService<Answer, Long> {
    int getCountAnswer(Question question);
}
