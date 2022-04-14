package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.CommentAnswerDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentAnswerDaoImpl extends ReadWriteDaoImpl<CommentAnswer,Long> implements CommentAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CommentDto getCommentByAnswerId(Long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                                "(comment.id," +
                                "comment.text, " +
                                "comment.user.id, " +
                                "comment.user.fullName, " +
                                "(SELECT (sum(r.count)) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                                "comment.persistDateTime)" +
                                "FROM Comment comment " +
                                "left JOIN CommentAnswer commentAnswer ON (comment.id = commentAnswer.comment.id) " +
                                "WHERE commentAnswer.answer.id = :id ", CommentDto.class)
                .setParameter("id", id)
                .getResultList().stream().reduce((e1, e2) -> e2).orElse(null);
    }
}
