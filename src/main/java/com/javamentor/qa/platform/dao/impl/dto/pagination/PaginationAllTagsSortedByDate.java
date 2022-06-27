package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllTagsSortedByDate implements PageDtoDao<TagViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");
        String tagsFilter = (String) params.get("tagsFilter");

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.TagViewDto" +
                                "(t.id, " +
                                "t.name, " +
                                "t.description, " +
                                "t.persistDateTime, " +
                                "cast (t.questions.size as long), " +
                                "(select count (q.id) from Question  q " +
                                "join q.tags qh " +
                                "where t.id = qh.id and q.persistDateTime >= current_date) as one_day, " +
                                "(select count (q.id) from Question  q " +
                                "join q.tags qh " +
                                "where t.id = qh.id and q.persistDateTime " +
                                "between (current_date-7) and current_date ) as one_week) " +
                                "from Tag t " +
                                "WHERE t.name LIKE :tagsFilter " +
                                "order by persistDateTime desc, t.name ", TagViewDto.class)
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .setParameter("tagsFilter", "%" + tagsFilter + "%")
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        String tagsFilter = (String) params.get("tagsFilter");
        return (int) entityManager.createQuery
                ("Select CAST(count(tag.id) as int) AS countTags from Tag tag WHERE tag.name LIKE :tagsFilter")
                .setParameter("tagsFilter", "%" + tagsFilter + "%")
                .getSingleResult();
    }
}
