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
        String query =
                "select a.id as id, a.user_id as userId,\n" +
                        "(select rep.count as userReputation from reputation rep where rep.author_id = a.user_id),\n" +
                        "a.question_id as questionId,\n" +
                        "a.html_body as body,\n" +
                        "a.persist_date as persistDate,\n" +
                        "a.is_helpful as isHelpful,\n" +
                        "a.date_accept_time as dateAccept,\n" +
                        "(select sum (case when va.vote = 'UP_VOTE' then 1 else -1 end ) from votes_on_answers va)\n" +
                        "as countValuable,\n" +
                        "(select ue.image_link as image from user_entity ue where ue.id = a.user_id),\n" +
                        "(select ue.nickname as nickname from user_entity ue where ue.id = a.user_id)\n" +
                        "from answer a where a.question_id = ?1";

        answerDtos = (List<AnswerDto>) entityManager.createNativeQuery(query)
                .setParameter(1, id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {
                                return new AnswerDto(
                                        ((BigInteger) tuple[0]).longValue(),
                                        ((BigInteger) tuple[1]).longValue(),
                                        ((Integer) tuple[2]).longValue(),
                                        ((BigInteger) tuple[3]).longValue(),
                                        ((String) tuple[4]),
                                        ((Timestamp) tuple[5]).toLocalDateTime(),
                                        ((Boolean) tuple[6]).booleanValue(),
                                        ((Timestamp) tuple[7]).toLocalDateTime(),
                                        ((BigInteger) tuple[8]).longValue(),
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

}
