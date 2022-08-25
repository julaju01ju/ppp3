package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatDto {
    private long id;
    private String chatName;
    private PageDto<MessageDto> page;

    public GroupChatDto(long id, String chatName) {
        this.id = id;
        this.chatName = chatName;
    }
}
