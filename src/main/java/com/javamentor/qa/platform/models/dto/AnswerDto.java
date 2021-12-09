package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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


    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userReputation, questionId, body, persistDate, isHelpful, dateAccept, countValuable, image, nickName);
    }
}
