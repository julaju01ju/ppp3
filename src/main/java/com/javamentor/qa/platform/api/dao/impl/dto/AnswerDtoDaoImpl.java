package com.javamentor.qa.platform.api.dao.impl.dto;

import com.javamentor.qa.platform.api.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import net.bytebuddy.asm.Advice;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component("dao")
public class AnswerDtoDaoImpl
        implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

//    @Autowired
//    public AnswerDtoDaoImpl(EntityManager em) {
//        this.entityManager = em;
//    }


    @Override
    public List<AnswerDto> getAllByQuestionId(Long id) {
//        return (List<AnswerDto>) entityManager.createQuery("select a.id as id, a.user.id as userId," +
//                        "select count from reputation where author.id = a.user.id as userReputation, a.question.id as" +
//                        " questionId, a.htmlBody as body, a.persistDateTime as persistDate, a.isHelpful as isHelpful, " +
//                        "a.dateAcceptTime as dateAccept, select array_length(vote, UP_VOTE) from a.voteAnswers as countValuable, a.user.imageLink as " +
//                        "image, a.user.nickname as nickName from Answer a where a.question.id = : id")
//                .setParameter("id", id)
//                .unwrap(org.hibernate.query.Query.class)
//                .setResultTransformer(Transformers.aliasToBean(AnswerDto.class))
//                .getResultList();
       return null;
    }


    public Answer getOne(Long id){
        Answer answer =  entityManager.find(Answer.class,id);
        System.out.println(answer);
        return answer;
    }

    public Role getRole(Long id){
        Role role =  entityManager.find(Role.class,id);
        System.out.println(role);
        return role;
    }
}
