package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentDtoServiceImpl implements CommentDtoService {

    private final CommentDtoDao commentDtoDao;

    @Autowired
    public CommentDtoServiceImpl(CommentDtoDao commentDtoDao) {
        this.commentDtoDao = commentDtoDao;
    }

    @Override
    public CommentDto checkMyCommentDtoByQuestionId(Long id) {
        return commentDtoDao.getCommentDtosByQuestionId(id)
                .stream()
                .reduce((e1, e2) -> e2)
                .orElse(null);
    }
}
