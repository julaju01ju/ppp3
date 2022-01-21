package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagDtoPagination;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllTagsSortedByName implements PageDtoDao<TagDtoPagination> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDtoPagination> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");
        Query query = entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagDtoPagination (" +
                        "t.id, " +
                        "t.name, " +
                        "t.description, " +
                        "t.persistDateTime, " +
                        "cast(t.questions.size as long)) " +
                        "from Tag t " +
                        "order by length(t.name), t.name ", TagDtoPagination.class);
        query.setFirstResult((page - 1) * itemsOnPage);
        query.setMaxResults(itemsOnPage);
        return query.getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                ("Select CAST(count(tag.id) as int) AS countTags from Tag tag");
        return (int) queryTotal.getSingleResult();
    }
}
