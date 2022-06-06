package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.SingleChatDto;

import java.util.List;

public interface SingleChatDtoService {
    List<SingleChatDto> receiveSingleChatsDtosByUsername(String username, int page, int itemsOnPage);
}
