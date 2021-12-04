package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class AnswerDtoDaoImpl
        implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<AnswerDto> getAllByQuestionId(Long id) {
        return (List<AnswerDto>) entityManager.createQuery("select a.id as id, a.user.id as userId," +
                        "select count from reputation where author.id = a.user.id as userReputation, a.question.id as" +
                        " questionId, a.htmlBody as body, a.persistDateTime as persistDate, a.isHelpful as isHelpful, " +
                        "a.dateAcceptTime as dateAccept, select array_length(vote, UP_VOTE) from a.voteAnswers as countValuable, a.user.imageLink as " +
                        "image, a.user.nickname as nickName from Answer a where a.question.id = : id")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(AnswerDto.class))
                .getResultList();
    }
}
