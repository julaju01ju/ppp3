package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;

import java.util.List;
import java.util.Map;

public interface ChatDtoService {
    List<ChatDto> getChatByString(Long userId, String searchedString);
}
