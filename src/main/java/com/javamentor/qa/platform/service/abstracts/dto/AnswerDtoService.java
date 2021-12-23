package com.javamentor.qa.platform.service.abstracts.dto;



import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;

import java.util.List;

public interface AnswerDtoService {
    public List<AnswerDto> getAllAnswersByQuestionId(Long id);

    void deleteAnswerByAnswerId(Long id);

    Answer getById(Long id);

}
