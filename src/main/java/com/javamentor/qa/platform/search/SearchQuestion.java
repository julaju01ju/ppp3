package com.javamentor.qa.platform.search;

import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Component
public class SearchQuestion {
    @PersistenceContext
    private EntityManager entityManager;

    public List<QuestionViewDto> getItems(Map<String, Object> inputParams) {
        return entityManager.createNativeQuery(
                        "SELECT " +
                                "distinct q.id AS q_id, " +
                                "q.title, " +
                                "q.description, " +
                                "q.last_redaction_date, " +
                                "q.persist_date, " +
                                "u.id , " +
                                "u.full_name, " +
                                "u.image_link, " +
                                "(SELECT coalesce(sum(r.count),0) FROM reputation r " +
                                "   WHERE r.author_id = u.id) AS reputation, " +
                                "(SELECT coalesce(count(up.vote), 0) FROM votes_on_questions up " +
                                "   WHERE up.vote = 'UP_VOTE' AND up.question_id = q.id) " +
                                "- " +
                                "(SELECT coalesce(count(down.vote), 0) FROM votes_on_questions down " +
                                "   WHERE down.vote = 'DOWN_VOTE' AND down.question_id = q.id) AS votes, " +
                                "(SELECT coalesce(count(a.id),0) FROM answer a " +
                                "   WHERE a.question_id = q.id) AS answers " +
                                "FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "JOIN question_has_tag qht ON q.id = qht.question_id " +
                                " where q.title like concat ('%', :title, '%')" +
                                " and q.description like concat ('%', :body, '%')" +
                                " and u.full_name like concat ('%', :userName, '%')" +
                                " and (q.title like concat ('%', :request, '%')"+
                                " or q.description like concat ('%', :request, '%'))"+
                                "ORDER BY q.id ")
                .setParameter("title", inputParams.get("title"))
                .setParameter("body", inputParams.get("body"))
                .setParameter("request", inputParams.get("request"))
                .setParameter("userName",inputParams.get("user"))
                .getResultList();
    }
}