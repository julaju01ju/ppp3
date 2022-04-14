package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;

public interface CommentDtoDao {
    List<CommentDto> getCommentDtosByQuestionId(Long id);
    CommentDto getCommentDtoByAnswerId(Long id);
}
