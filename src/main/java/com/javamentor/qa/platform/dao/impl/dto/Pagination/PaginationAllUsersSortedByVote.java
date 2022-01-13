package com.javamentor.qa.platform.dao.impl.dto.Pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserDtoResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllUsersSortedByVote implements PageDtoDao<UserDto> {

    @PersistenceContext
   private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> params) {
        Query query = entityManager.createQuery("select" +
                " user.id AS id, user.email AS email,user.fullName AS fullName," +
                " user.imageLink AS linkImage,user.city AS city," +
                " (select CAST(COALESCE(sum(reputation.count), 0) as int) from Reputation reputation where reputation.author.id = user.id) AS reputation," +
                " ((SELECT COALESCE(SUM(CAST(vQ.vote AS int)), 0) FROM VoteQuestion vQ JOIN Question q ON vQ.question.id = q.id WHERE q.user.id = user.id) +" +
                " (SELECT COALESCE(SUM(CAST(vA.vote AS int)), 0) FROM VoteAnswer vA JOIN Answer ans ON vA.answer.id = ans.id WHERE ans.user.id = user.id)) AS sum1" +
                " FROM User user" +
                " ORDER BY sum1 DESC").unwrap(org.hibernate.query.Query.class).setResultTransformer(new UserDtoResultTransformer());
        query.setFirstResult(((int) params.get("currentPageNumber") - 1) * (int) params.get("itemsOnPage"));
        query.setMaxResults((int) params.get("itemsOnPage"));
        return query.getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                ("Select count(user.id) from User user");
        return ((Long) queryTotal.getSingleResult()).intValue();
    }
}
