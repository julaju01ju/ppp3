package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.Optional;

public interface CommentDtoService {
    Optional<CommentDto> getCommentDtoByCommentId(Long id);
}
