package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class SinglechatConverter {

    @Mapping(source = "singleChat.id", target = "id")
    @Mapping(source = "singleChat.useTwo.nickname", target = "name")
    @Mapping(source = "singleChat.useTwo.imageLink", target = "image")
    @Mapping(source = "message.message", target = "lastMessage")
    @Mapping(source = "message.persistDate", target = "persistDateTimeLastMessage")

    public abstract SingleChatDto singleChatToSingleChatDto(SingleChat singleChat, Message message);
}
