package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;

import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

public interface QuestionDtoService extends PageDtoService<QuestionDto> {
    Optional<QuestionDto> getQuestionById(Long id);
}
