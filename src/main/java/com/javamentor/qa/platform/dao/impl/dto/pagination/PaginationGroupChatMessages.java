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
                                "where c.chatType = 1 " +
                                "order by m.persistDate desc"
                        , MessageViewDto.class)
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
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
