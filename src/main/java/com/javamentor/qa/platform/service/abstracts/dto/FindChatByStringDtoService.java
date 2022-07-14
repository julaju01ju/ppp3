package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;

import java.util.List;
import java.util.Set;

public interface FindChatByStringDtoService {
    List<ChatDto> getChatByString(String searchedString);

    boolean ifNotExistSearchedString(String searchedString);
}
