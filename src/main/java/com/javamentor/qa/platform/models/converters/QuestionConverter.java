package com.javamentor.qa.platform.models.converters;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {UserDao.class, QuestionDao.class}, componentModel = "spring")
public abstract class QuestionConverter {

    UserDao userDao;
    QuestionDao questionDao;

    @Autowired
    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Mapping(source = "listTagDto", target = "tags")
    @Mapping(expression = "java(userDao.getById(questionDto.getAuthorId()).get())", target = "user")
    public abstract Question questionDtoToQuestion(QuestionDto questionDto);

    @Mapping(source = "tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.nickname", target = "authorName")
    @Mapping(source = "question.user.imageLink", target = "authorImage")
    @Mapping(expression = "java(questionDao.getCountAnswer(question))", target = "countAnswer")
    @Mapping(expression = "java(questionDao.getCountValuable(question))", target = "countValuable")
    @Mapping(constant = "0", target = "viewCount")
    public abstract QuestionDto questionToQuestionDto(Question question);

}
