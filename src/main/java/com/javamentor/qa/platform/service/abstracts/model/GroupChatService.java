package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.GroupChat;

import java.util.Optional;

public interface GroupChatService extends ReadWriteService<GroupChat, Long>{

    Optional<GroupChat> getGroupChatById(Long id);
}
