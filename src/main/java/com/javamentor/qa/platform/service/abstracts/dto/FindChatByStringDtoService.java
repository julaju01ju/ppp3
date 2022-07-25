package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;

import java.util.List;
import java.util.Map;

public interface FindChatByStringDtoService {
    List<ChatDto> getChatByString(Map<String,Object> params);

    boolean ifNotExistSearchedString(Map<String,Object> params);
}
