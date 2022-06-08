package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.MessageViewDtoResultTransformer;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationGroupChatMessages implements PageDtoDao<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createNativeQuery(
                "select user_entity.id, " +
                        "user_entity.full_name, " +
                        "user_entity.image_link, " +
                        "message.id, " +
                        "message.message, " +
                        "message.persist_date " +
                        "from " +
                        "group_chat inner join chat on group_chat.chat_id = chat.id " +
                        "inner join message on group_chat.chat_id = message.chat_id " +
                        "inner join groupchat_has_users on chat.id = groupchat_has_users.chat_id " +
                        "inner join user_entity on groupchat_has_users.user_id = user_entity.id " +
                        "order by message.persist_date desc")
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .unwrap(Query.class)
                .setResultTransformer(new MessageViewDtoResultTransformer()).getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return ((BigInteger) entityManager.createNativeQuery(
                "select count (message.id) from group_chat " +
                        "inner join chat on group_chat.chat_id = chat.id " +
                        "inner join message on group_chat.chat_id = message.chat_id " +
                        "inner join groupchat_has_users on chat.id = groupchat_has_users.chat_id " +
                        "inner join user_entity on groupchat_has_users.user_id = user_entity.id")
                .getSingleResult()).intValue();
    }
}
