package com.javamentor.qa.platform.service.abstracts.dto;

import org.springframework.http.ResponseEntity;

/**
 * @author Maksim Solovev 19.12.2021.
 */

public interface QuestionDtoService {
    Long getQuestionReputation(Long questionId);

}
