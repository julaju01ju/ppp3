package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageViewDto {
    private long id;
    private String message;
    private String nickName;
    private long userId;
    private String image;
    private LocalDateTime persistDateTime;
}
