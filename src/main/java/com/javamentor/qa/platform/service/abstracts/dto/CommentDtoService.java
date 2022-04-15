package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

public interface CommentDtoService {
    CommentDto getLastAddedCommentDtoByQuestionId(Long id);
}
