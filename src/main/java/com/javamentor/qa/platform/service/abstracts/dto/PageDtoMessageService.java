package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;

public interface PageDtoMessageService extends PageDtoService<MessageViewDto> {
    PageDto<MessageViewDto> getPageDtoMessage(String pageDtoDaoName, Map<String, Object> params);
}
