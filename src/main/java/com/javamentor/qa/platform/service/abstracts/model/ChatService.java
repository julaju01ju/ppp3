package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.user.MessageStar;

public interface ChatService extends ReadWriteService<Chat, Long> {
    boolean isChatHasUser(Long chatId, Long userId);
}
