package com.javamentor.qa.platform.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupChatDto {

    private String chatName;
    private List<Long> userIds;
}
