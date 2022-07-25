package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FindChatByStringDtoDao {
    List<ChatDto> getChatByString (Map<String,Object> params);
}
