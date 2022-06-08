package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;
import java.util.Optional;

public interface GroupChatDtoService extends PageDtoService<MessageViewDto> {
    PageDto<MessageViewDto> getPageDtoMessage(String pageDtoDaoName, Map<String, Object> params);
    Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params);
}
