package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;

import java.util.List;


public interface CommentAnswerDao extends ReadWriteDao<CommentAnswer, Long> {
    CommentDto getCommentByAnswerId(Long id);
}
