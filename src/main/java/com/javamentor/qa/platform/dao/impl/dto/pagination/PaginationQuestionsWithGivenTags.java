package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class PaginationQuestionsWithGivenTags implements PageDtoDao<QuestionDto> {

    @PersistenceContext
    private EntityManager em;
    private List<Long> ignoredTag;
    private List<Long> trackedTag;

    @Override
    public List<QuestionDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        getTagsFromParams(params);

        List<Long> questionIdList = (List<Long>) em.createNativeQuery(
                        "SELECT " +
                                "DISTINCT q.id FROM question q JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "WHERE CASE " +
                                "   WHEN -1 IN :ignoredTag AND -1 IN :trackedTag THEN TRUE " +
                                "   WHEN -1 IN :ignoredTag THEN qht.tag_id IN :trackedTag " +
                                "   WHEN -1 IN :trackedTag THEN q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   ELSE qht.tag_id IN :trackedTag AND q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   END " +
                                "ORDER BY q.id")
                .setParameter("ignoredTag", ignoredTag)
                .setParameter("trackedTag", trackedTag)
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();

        return em.createNativeQuery(
                        "SELECT " +
                                "q.id AS q_id, " +
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
                                "   WHERE a.question_id = q.id) AS answers, " +
                                "t.id AS t_id, " +
                                "t.name AS t_name, " +
                                "t.description AS t_desc " +
                                "FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "JOIN tag t ON qht.tag_id = t.id " +
                                "WHERE " +
                                "q.id IN :questionId ORDER BY q.id")
                .setParameter("questionId", questionIdList)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionResultTransformer()).getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {

        getTagsFromParams(params);

        return (int)em.createNativeQuery(
                        "SELECT " +
                                "DISTINCT q.id FROM question q JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "WHERE CASE " +
                                "   WHEN -1 IN :ignoredTag AND -1 IN :trackedTag THEN TRUE " +
                                "   WHEN -1 IN :ignoredTag THEN qht.tag_id IN :trackedTag " +
                                "   WHEN -1 IN :trackedTag THEN q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   ELSE qht.tag_id IN :trackedTag AND q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "END ")
                .setParameter("ignoredTag", ignoredTag)
                .setParameter("trackedTag", trackedTag)
                .getResultList().stream().count();
    }

    private void getTagsFromParams(Map<String, Object> params) {
        ignoredTag = (List<Long>) params.get("ignoredTag");
        trackedTag = (List<Long>) params.get("trackedTag");
    }
}

class QuestionResultTransformer implements ResultTransformer {

    private Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

        Long questionId = ((Number) tuple[0]).longValue();

        QuestionDto questionDto = questionDtoMap.computeIfAbsent(
                questionId,
                v -> {
                    QuestionDto questionDtoTemp = new QuestionDto();
                    questionDtoTemp.setId(((BigInteger) tuple[0]).longValue());
                    questionDtoTemp.setTitle((String) tuple[1]);
                    questionDtoTemp.setDescription((String) tuple[2]);
                    questionDtoTemp.setLastUpdateDateTime(((Timestamp) tuple[3]).toLocalDateTime());
                    questionDtoTemp.setPersistDateTime(((Timestamp) tuple[4]).toLocalDateTime());
                    questionDtoTemp.setAuthorId(((BigInteger) tuple[5]).longValue());
                    questionDtoTemp.setAuthorName((String) tuple[6]);
                    questionDtoTemp.setAuthorImage((String) tuple[7]);
                    questionDtoTemp.setAuthorReputation(((BigInteger) tuple[8]).longValue());
                    questionDtoTemp.setCountValuable(((BigInteger) tuple[9]).intValue());
                    questionDtoTemp.setCountAnswer(((BigInteger) tuple[10]).intValue());
                    questionDtoTemp.setViewCount(0);
                    questionDtoTemp.setListTagDto(new ArrayList<>());
                    return questionDtoTemp;
                }
        );

        TagDto tagDto = new TagDto();
        tagDto.setId(((BigInteger) tuple[11]).longValue());
        tagDto.setName((String) tuple[12]);
        tagDto.setDescription((String) tuple[13]);
        questionDto.getListTagDto().add(tagDto);

        return questionDto;
    }

    @Override
    public List<QuestionDto> transformList(List list) {
        return new ArrayList<>(questionDtoMap.values());
    }
}