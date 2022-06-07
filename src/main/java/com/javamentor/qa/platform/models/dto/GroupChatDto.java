package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatDto {
    private long id;
    private String chatName;
    private PageDto<MessageDto> page;
    private String image;
    private LocalDateTime persistDateTime;
}
