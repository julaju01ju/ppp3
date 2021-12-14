package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDtoTest;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationUserTest implements PageDtoDao<UserDtoTest> {

    @PersistenceContext
    EntityManager entityManager;

    List<UserDtoTest> items = new LinkedList<>();


    @Override
    public List<UserDtoTest> getItems(Map<String, Object> params) {
        if (items.isEmpty()) {
            Query query = entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDtoTest" +
                    "(user.id,user.about,user.city,user.email,user.fullName," +
                    "user.nickname,user.password) from User user", UserDtoTest.class);
            query.setFirstResult((int) params.get("currentPageNumber") * (int) params.get("itemsOnPage"));
            query.setMaxResults((int) params.get("itemsOnPage"));
            items = query.getResultList();
        }
        return items;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return items.isEmpty() ? getItems(params).size() : items.size();

    }
}
