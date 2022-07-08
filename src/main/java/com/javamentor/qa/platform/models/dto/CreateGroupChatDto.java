package com.javamentor.qa.platform.models.dto;

import java.util.List;

public class CreateGroupChatDto {

    private String chatName;

    private List<Long> userIds;

    public String getChatName() {
        return chatName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }
}
