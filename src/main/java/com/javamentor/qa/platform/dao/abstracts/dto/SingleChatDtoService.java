package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Message;

import java.util.List;

public interface SingleChatDtoService {
    List<SingleChatDto> receiveSingleChatsDtosByUsername(String username, int page, int itemsOnPage);

    Message receiveLastMessageBySingleChatId(Long id);
}
