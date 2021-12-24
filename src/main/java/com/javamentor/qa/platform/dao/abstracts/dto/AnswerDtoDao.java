package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface AnswerDtoDao {
    public List<AnswerDto> getAllByQuestionId(Long id);

    void deleteAnswerByAnswerId(Long id);
}
