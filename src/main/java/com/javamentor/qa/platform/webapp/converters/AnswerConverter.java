package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AnswerConverter {

    public abstract AnswerDto answerToAnswerDto(Answer answer);

    public abstract Answer answerDtoToAnswer(AnswerDto answerDto);
}
