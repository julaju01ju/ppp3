//package com.javamentor.qa.platform.webapp.converters;
//
//import com.javamentor.qa.platform.models.dto.AnswerDto;
//import com.javamentor.qa.platform.models.entity.question.answer.Answer;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper
//public interface AnswerConverter {
//
//    @Mapping(source = "answer.user.id", target = "userId")
//    @Mapping(source = "answer.user.reputationCount", target = "userReputation")
//    @Mapping(source = "answer.question.id", target = "qusetionId")
//    @Mapping(source = "answer.htmlBody", target = "body")
//    @Mapping(source = "answer.persistDateTime", target = "persistDate")
//    @Mapping(source = "answer.dateAcceptTime", target = "dateAccept")
//    @Mapping(source = "answer.countValueable", target = "countValueable")
//    @Mapping(source = "answer.user.imageLink", target = "image")
//    @Mapping(source = "answer.user.nickname", target = "nickName")
//    AnswerDto answerToAnserDto(Answer answer);
//
//}
