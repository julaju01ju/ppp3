package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import com.javamentor.qa.platform.service.abstracts.model.CommentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentQuestionServiceImpl extends ReadWriteServiceImpl<CommentQuestion,Long> implements CommentQuestionService {

    private final CommentDtoDao commentDtoDao;

    @Autowired
    public CommentQuestionServiceImpl(ReadWriteDao<CommentQuestion, Long> readWriteDao, CommentDtoDao commentDtoDao) {
        super(readWriteDao);
        this.commentDtoDao = commentDtoDao;
    }

    @Override
    public CommentDto checkMyCommentDto(Long id, String text, Long userId) {
        List<CommentDto> list = commentDtoDao.getCommentDtosByQuestionId(id);
        for (CommentDto e : list) {
            if ((e.getComment()).equals(text) & (e.getUserId()).equals(userId)) {
                return e;
            }
        }
        return null;
    }
}
