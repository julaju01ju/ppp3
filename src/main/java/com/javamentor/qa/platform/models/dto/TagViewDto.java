package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagViewDto {

    private Long id;
    private String name;
    private String description;
    private Long questionsCount;
    private Long questionCountOneDay;
    private Long questionCountWeekDay;
}
