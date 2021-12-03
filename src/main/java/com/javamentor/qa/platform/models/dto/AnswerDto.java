package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AnswerDto {
    private Long id;
    private Long userId;
    private Long userReputation;
    private Long questionId;
    private String body;
    private LocalDateTime persistDate;
    private Boolean isHelpful;
    private LocalDateTime dateAccept;
    private Long countValuable;
    private String image;
    private String nickName;
}
