package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllTagsSortedByPopular implements PageDtoDao<TagViewDto> {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        Query query = entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagViewDto (" +
                        "t.id, " +
                        "t.name, " +
                        "t.description, " +
                        "cast (t.questions.size as long), " +
                        "(select count (q.id) from Question q join q.tags qr where q.persistDateTime = current_date and t.id = qr.id) as one_day, " +
                        "(select count (q.id) from Question q join q.tags qr where q.persistDateTime between current_date and current_date - 7 and t.id = qr.id) as one_week) " +
                        "from Tag t " +
                        "order by cast(t.questions.size as long) desc, t.name ", TagViewDto.class);
        query.setFirstResult((page - 1) * itemsOnPage);
        query.setMaxResults(itemsOnPage);
        return query.getResultList();
    }
//LocalDate и LocalDateTime
    @Override
    public int getTotalResultCount(Map<String, Object> params) {

        Query queryTotal = entityManager.createQuery
                ("Select CAST(count(tag.id) as int) AS countTags from Tag tag");
        return (int) queryTotal.getSingleResult();
    }
}
