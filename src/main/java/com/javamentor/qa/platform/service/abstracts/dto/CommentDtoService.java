package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;

public interface CommentDtoService {
    List<CommentDto> getCommentDtoByQuestionId(Long id);
}
