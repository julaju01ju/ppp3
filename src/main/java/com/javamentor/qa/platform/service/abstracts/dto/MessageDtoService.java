package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;

public interface MessageDtoService extends PageDtoService<MessageDto> {
    PageDto<MessageDto> getPageMessages(String pageDtoDaoName, Map<String, Object> params);
}
