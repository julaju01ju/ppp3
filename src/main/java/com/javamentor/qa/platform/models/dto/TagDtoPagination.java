package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDtoPagination {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime persistDateTime;
    private Long questionsCount;
}
