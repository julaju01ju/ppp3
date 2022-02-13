package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllTagsSortedByName implements PageDtoDao<TagViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagViewDto> getItems(Map<String, Object> params) {

        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime one = LocalDateTime.now().minusDays(1);

        Query query = entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagViewDto (" +
                        "t.id, " +
                        "t.name, " +
                        "t.description, " +
                        "cast (t.questions.size as long), " +
                        "(select count(q.id)  from Question q join  q.tags qht  where q.persistDateTime = current_date and qht.id = q.id ) as one_day, " +
                        "(select count (q.id) from Question q where q.persistDateTime = current_date  ) as one_week) " +
//                        "(select count(t.id) from t.questions qht join Question q where q.persistDateTime between :today and :one and q.id = qht.id" +
                        "from Tag t " +
                        "order by cast(t.questions.size as long) desc, t.name ", TagViewDto.class);
//        and q.persistDateTime = current_date
//        query.setParameter("today", today);
//        query.setParameter("one", one);
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
