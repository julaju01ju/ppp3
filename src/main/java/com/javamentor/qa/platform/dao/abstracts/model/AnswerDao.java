package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {
    int getCountAnswer(Long question_id);

}
