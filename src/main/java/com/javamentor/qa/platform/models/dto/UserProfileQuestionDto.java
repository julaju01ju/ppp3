package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;
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
    private List<Tag> listTagDto;
    private Long countAnswer;
    private LocalDateTime persistDate;

    public UserProfileQuestionDto(Long questionId, String title, Long countAnswer, LocalDateTime persistDate) {
        this.questionId = questionId;
        this.title = title;
        this.countAnswer = countAnswer;
        this.persistDate = persistDate;
    }
}
