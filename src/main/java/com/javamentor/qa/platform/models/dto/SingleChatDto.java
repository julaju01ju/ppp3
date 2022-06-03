package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleChatDto {

    private Long id;
    private String name;
    private String image;
    private Message lastMessage;
    private LocalDateTime persistDateTimeLastMessage;
}
