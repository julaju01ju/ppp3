package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.MessageViewDtoResultTransformer;
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

        return entityManager.createNativeQuery(
                "select user_entity.id as user_entity_ud, " +
                        "user_entity.full_name, " +
                        "user_entity.image_link, " +
                        "message.id, " +
                        "message.message, " +
                        "message.persist_date " +
                        "from message join user_entity on message.user_sender_id = user_entity.id " +
                        "join chat on message.chat_id = chat.id " +
                        "where chat.chat_type = 1 " +
                        "order by message.persist_date desc")
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new MessageViewDtoResultTransformer())
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return ((BigInteger) entityManager.createNativeQuery(
                "select count (message.id) " +
                        "from message " +
                        "inner join user_entity on message.user_sender_id = user_entity.id " +
                        "inner join chat on message.chat_id = chat.id " +
                        "where chat.chat_type = 1")
                .getSingleResult()).intValue();
    }
}
