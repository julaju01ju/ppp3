package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getIgnoredTags(Long userId) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.TagDto(it.id, it.ignoredTag.name, it.ignoredTag.description) " +
                                "from IgnoredTag it where it.user.id = :userId", TagDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Map<Long, List<TagDto>> getTagsByQuestionIds(List<?> ids) {

        List<Map<Long, List<TagDto>>> list = entityManager.createNativeQuery(
                        "select qht.question_id, t.id, t.name, t.description " +
                                "from tag t join question_has_tag qht on t.id = qht.tag_id " +
                                "and qht.question_id in :ids")
                .setParameter("ids", ids)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    Map<Long, List<TagDto>> map = new HashMap<>();

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {

                        List<TagDto> tagDtoList = map.computeIfAbsent(
                                ((BigInteger) objects[0]).longValue(),
                                id -> new ArrayList<>());
                            tagDtoList.add(new TagDto(
                                    ((BigInteger) objects[1]).longValue(),
                                    (String) objects[2],
                                    (String) objects[3]
                            ));
                        return map;
                    }

                    @Override
                    public List<Map<Long, List<TagDto>>> transformList(List list) {
                        List<Map<Long, List<TagDto>>> resultList = new ArrayList<>();
                        resultList.add(map);
                        return resultList;
                    }
                })
                .getResultList();

        return list.get(0);
    }

    @Override
    public List<TagDto> getFoundTags(String searchString) {
        return entityManager.createNativeQuery("SELECT t.id, t.name, t.description "+
                "FROM tag t JOIN question_has_tag qht on t.id = qht.tag_id " +
                "GROUP BY qht.tag_id, t.id " +
                        "HAVING t.name LIKE :searchString " +
                        "ORDER BY COUNT(qht.tag_id) DESC " +
                        "LIMIT 10")
                .setParameter("searchString", "%" + searchString + "%")
                .getResultList();
    }
}
