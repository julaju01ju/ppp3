package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AnswerDtoDaoImpl
        implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<AnswerDto> getAllByQuestionId(Long id) {
        List<AnswerDto> answerDtos;
        String query = "select a.id , a.user.id," +
                        "(select rep.count from Reputation rep where rep.author.id = a.user.id),"+
                        "a.question.id, " +
                        "a.htmlBody,"+
                        "a.persistDateTime,"+
                        "a.isHelpful,"+
                        "a.dateAcceptTime,"+
                        "(select sum(case va.vote  when 'UP_VOTE' then 1 else -1 end) from VoteAnswer va group by a" +
                ".id),"+
                        "(select u.imageLink from User u),"+
                        "(select u.nickname from User u)"+
                        "from Answer as a where a.question.id = :id";

        answerDtos = (List<AnswerDto>) entityManager.createQuery(query)
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {
                                return new AnswerDto(
                                        ((Long) tuple[0]).longValue(),
                                        ((Long) tuple[1]).longValue(),
                                        ((Integer) tuple[2]).longValue(),
                                        ((Long) tuple[3]).longValue(),
                                        ((String) tuple[4]),
                                        ((LocalDateTime) tuple[5]),
                                        ((Boolean) tuple[6]).booleanValue(),
                                        ((LocalDateTime) tuple[7]),
                                        ((Long) tuple[8]).longValue(),
                                        ((String) tuple[9]),
                                        ((String) tuple[10]));
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                ).getResultList();

        return answerDtos;

    }

    @Override
    @Transactional
    public void deleteAnswerByAnswerId(Long id) {
        String query = "UPDATE Answer SET isDeleted = true where id = :id";
        entityManager.createQuery(query)
                .setParameter("id", id)
                .executeUpdate();
    }

}
