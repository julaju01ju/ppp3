package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {

        return entityManager.createNativeQuery("select q.id, q.title, q.description,  q.last_redaction_date,  q.persist_date,  u.id,  u.full_name,  u.image_link, " +
                        "(select sum(r.count) from reputation r where r.author_id = u.id) as reputation, " +
                        "(select count(up.vote) from votes_on_questions up where up.vote = 'UP_VOTE' and up.question_id = q.id) - (select count(down.vote)   from votes_on_questions down   where down.vote = 'DOWN_VOTE' and down.question_id = q.id)      as votes, " +
                        "(select count(a.id) from answer a where a.question_id = q.id)    as answers, " +
                        "t.id, t.name, t.description " +
                        "from question q join user_entity u on u.id = q.user_id " +
                        "join question_has_tag qht on q.id = qht.question_id " +
                        "join tag t on qht.tag_id = t.id where q.id =:id")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        System.out.println(objects);
                        System.out.println(strings);


                        return 5;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                }).getResultStream().findAny();

    }
}
