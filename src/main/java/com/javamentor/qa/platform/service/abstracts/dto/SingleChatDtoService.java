package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;

import java.util.List;

public interface SingleChatDtoService {
    List<SingleChat> receiveSingleChatsByUsername(String username);
    Message receiveLastMessageBySingkeChatId (Long id);
}
