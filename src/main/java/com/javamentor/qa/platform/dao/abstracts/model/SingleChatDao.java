package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;

import java.util.Optional;

public interface SingleChatDao extends ReadWriteDao <SingleChat, Long> {
    Optional<SingleChat> addSingleChatAndMessage(SingleChat singleChat, String message);
}
