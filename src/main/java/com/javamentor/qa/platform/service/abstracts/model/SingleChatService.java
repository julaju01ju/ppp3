package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;

public interface SingleChatService extends ReadWriteService<SingleChat, Long>{
    void addSingleChatAndMessage(SingleChat singleChat, String message);
}
