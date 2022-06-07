package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;

import java.util.Optional;

public interface GroupChatDtoDao {
    Optional<MessageDto> getGroupChatByMessageIdAndUserId(Long messageId, Long userId);
}
