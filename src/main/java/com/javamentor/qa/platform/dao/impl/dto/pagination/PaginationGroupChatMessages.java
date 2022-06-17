package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.MessageViewDtoResultTransformer;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationGroupChatMessages implements PageDtoDao<MessageViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageViewDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.MessageViewDto" +
                                "(m.id, " +
                                "m.message, " +
                                "u.fullName, " +
                                "u.id, " +
                                "u.imageLink, " +
                                "m.persistDate) " +
                                "from Message m join User u on m.userSender.id = u.id " +
                                "join Chat c on m.chat.id = c.id " +
                                "where c.chatType = :chatType " +
                                "order by m.persistDate desc"
                        , MessageViewDto.class)
                .setParameter("chatType" , ChatType.GROUP)
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return (int) entityManager.createQuery(
                        "select CAST(count(m.id) as int) " +
                                "from Message m " +
                                "join User u on u.id = m.userSender.id " +
                                "join Chat c on m.chat.id = c.id " +
                                "where c.chatType = :chatType")
                .setParameter("chatType", ChatType.GROUP)
                .getSingleResult();
    }
}
