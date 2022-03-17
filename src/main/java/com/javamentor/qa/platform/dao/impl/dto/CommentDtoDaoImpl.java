package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentDtoDaoImpl implements CommentDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentDto> getCommentDtosByQuestionId(Long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                                "(comment.id," +
                                "comment.text, " +
                                "comment.user.id, " +
                                "comment.user.fullName, " +
                                "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                                "comment.persistDateTime)" +
                                "FROM Comment comment " +
                                "left JOIN CommentQuestion commentQuestion ON (comment.id = commentQuestion.comment.id) " +
                                "WHERE commentQuestion.question.id = :id " +
                                "order by comment.persistDateTime desc"
                        , CommentDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
