package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileQuestionDto {
    private Long questionId;
    private String title;
    private List<TagDto> listTagDto;
    private Long countAnswer;
    private LocalDateTime persistDate;
}
