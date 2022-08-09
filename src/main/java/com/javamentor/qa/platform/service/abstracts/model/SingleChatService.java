package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;

import java.util.Optional;

public interface SingleChatService extends ReadWriteService<SingleChat, Long>{
     Optional<SingleChat> addSingleChatAndMessage(SingleChat singleChat, String message);

}
