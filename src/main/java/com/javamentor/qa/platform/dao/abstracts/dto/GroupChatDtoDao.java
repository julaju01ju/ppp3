package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;

import java.util.Map;
import java.util.Optional;

public interface GroupChatDtoDao {
    Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params);
}