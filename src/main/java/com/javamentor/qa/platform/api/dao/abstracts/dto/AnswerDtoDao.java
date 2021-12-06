package com.javamentor.qa.platform.api.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerDtoDao {
    public List<AnswerDto> getAllByQuestionId(Long id);
}
