package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoDao;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository

public class SingleChatDtoDaoImpl implements SingleChatDtoDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public List<SingleChat> receiveSingleChatsByUsername(String username) {
        final String query = "from SingleChat as singleChat where singleChat.userOne.getUsername() = " + username +
                "or singleChat.userOne.getUsername() =" + username;
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public Message receiveLastMessageBySingkeChatId(Long id) {
        final String query = "from Message as message where message.chat.id = " + id;

        return (Message) entityManager.createQuery(query).getResultList().get(0);
    }
}
