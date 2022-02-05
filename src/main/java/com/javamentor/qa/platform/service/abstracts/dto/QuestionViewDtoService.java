package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;

import java.util.Map;

/**
 * @author Ali Veliev 10.12.2021
 */

public interface QuestionViewDtoService extends PageDtoService<QuestionViewDto> {
    PageDto<QuestionViewDto> getPageQuestionsWithTags(String pageDtoDaoName, Map<String, Object> params);
}
