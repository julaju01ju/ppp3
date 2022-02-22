package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
                        "(select sum(rep.count) from Reputation as rep wher rep.author.id = a.user.id)," +
                            "a.question.id,"+
                            "a.htmlBody,"+
                            "a.persistDateTime, "+
                            "a.isHelpful,"+
                            "a.isDeleted,"+
                            "a.dateAcceptTime,"+
                        "(select sum(case va.vote when 'UP_VOTE' then 1 else -1 end) from VoteAnswer as va where va.answer.id = a.id),"+
                        "(select u.imageLink from User as u where u.id = a.user.id),"+
                            "(select u.nickname from User as u where u.id = a.user.id)"+
                        "from Answer as a where a.question.id = :id and a.isDeleted = false";


        return (List<AnswerDto>) entityManager.createQuery(query)
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
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
                                        ((String) tuple[11]));
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                ).getResultList();
    }


}
