package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;

public interface CommentAnswerService extends ReadWriteService<CommentAnswer, Long> {
    CommentDto getCommentByAnswerId(Long id);
}
