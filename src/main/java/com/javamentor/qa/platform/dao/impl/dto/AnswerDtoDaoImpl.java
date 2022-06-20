package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerDtoDaoImpl
        implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<AnswerDto> getAllByQuestionId(Long id) {
        final String query =
                "select a.id, a.user.id," +
                        "(select sum(rep.count) from Reputation as rep where rep.author.id = a.user.id)," +
                        "a.question.id," +
                        "a.htmlBody," +
                        "a.persistDateTime, " +
                        "a.isHelpful," +
                        "a.isDeleted," +
                        "a.dateAcceptTime," +
                        "(select sum(case va.vote when 'UP_VOTE' then 1 else -1 end) from VoteAnswer as va where va.answer.id = a.id)," +
                        "(select u.imageLink from User as u where u.id = a.user.id)," +
                        "(select u.nickname from User as u where u.id = a.user.id) " +
                        "from Answer as a " +
                        "where a.question.id = :id and a.isDeleted = false";

        final String  queryCommentDto = "select new com.javamentor.qa.platform.models.dto.CommentDto(comment.id, " +
                        "comment.text, comment.user.id, " +
                        "comment.user.fullName, " +
                        "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                        "comment.persistDateTime) " +
                        "from Comment comment " +
                        "inner join CommentAnswer commentAnswer " +
                        "on comment.id = commentAnswer.comment.id " +
                        "where commentAnswer.answer.id = :id order by comment.persistDateTime desc";

        return (List<AnswerDto>) entityManager.createQuery(query)
                .setParameter("id", id)
                .unwrap(Query.class)
                .setResultTransformer(
                        new ResultTransformer() {

                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {
                                return new AnswerDto(
                                        ((Long) tuple[0]).longValue(),
                                        ((Long) tuple[1]).longValue(),
                                        Optional.ofNullable(tuple[2]).map(t2 -> ((Long) t2).longValue()).orElse(null),
                                        ((Long) tuple[3]).longValue(),
                                        ((String) tuple[4]),
                                        ((LocalDateTime) tuple[5]),
                                        ((Boolean) tuple[6]).booleanValue(),
                                        ((Boolean) tuple[7]).booleanValue(),
                                        ((LocalDateTime) tuple[8]),
                                        Optional.ofNullable(tuple[9]).map(t9 -> ((Long) t9).longValue()).orElse(null),
                                        ((String) tuple[10]),
                                        ((String) tuple[11]),
                                        entityManager.createQuery(queryCommentDto)
                                                .setParameter("id", tuple[0]).getResultList());
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                ).getResultList();
    }

    @Override
    public List<AnswerDto> getDeletedAnswersByUserId(Long id) {
        String query =
                "SELECT new com.javamentor.qa.platform.models.dto.AnswerDto(" +
                        "a.id, " +
                        "a.user.id, " +
                        "(SELECT SUM(r.count) FROM Reputation AS r WHERE r.author.id = a.user.id), " +
                        " a.question.id," +
                        "a.htmlBody, " +
                        "a.persistDateTime, " +
                        "a.isHelpful, " +
                        "a.isDeleted, " +
                        "a.dateAcceptTime, " +
                        "(SELECT SUM(case v.vote WHEN 'UP_VOTE' THEN 1 ELSE -1 END) FROM VoteAnswer AS v WHERE v.answer.id = :id)," +
                        "(SELECT u.imageLink FROM User AS u WHERE u.id = a.user.id)," +
                        "(SELECT u.nickname FROM User AS u WHERE u.id = a.user.id)) " +
                        "FROM Answer AS a WHERE a.user.id = :id AND a.isDeleted = true";
        return entityManager.createQuery(query)
                .setParameter("id", id)
                .getResultList();

    }


    @Override
    public Long getAmountAllAnswersByUserId(Long id) {

        String query = "SELECT count(*) FROM Answer AS a WHERE a.user.id = :id AND a.isDeleted = false AND a.persistDateTime >= :data";
        Long countRecords = (Long) entityManager.createQuery(query)
                .setParameter("id", id)
                .setParameter("data", LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusWeeks(1))
                .getSingleResult();

        return countRecords;
    }

}