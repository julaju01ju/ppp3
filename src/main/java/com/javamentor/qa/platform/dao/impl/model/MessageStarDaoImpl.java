package com.javamentor.qa.platform.dao.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;

import com.javamentor.qa.platform.models.entity.user.MessageStar;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static java.lang.Long.parseLong;

@Repository
public class MessageStarDaoImpl extends ReadWriteDaoImpl<MessageStar, Long> implements MessageStarDao {
    @PersistenceContext
    private EntityManager entityManager;




//    проверяет состоит ли юзер в чате, проверка проходит через группу groupchat_has_user
    @Override
    public boolean isChatHasUser(long chatId, long userId) {

        Query query = entityManager.createNativeQuery("SELECT chat_id FROM groupchat_has_users WHERE chat_id = :chatId AND  user_id = :userId")
                .setParameter("userId", userId).setParameter("chatId", chatId);

        return query.getResultList().size() > 0;
    }

//    проверяет не больше ли трех избранных сообщений у юзера
    @Override
    public boolean isUserHasNoMoreThanThreeMessageStar(long userId) {


        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM message_star WHERE user_id = :userId")
                .setParameter("userId", userId);


        return Integer.parseInt(String.valueOf(query.getSingleResult())) < 3;
    }


}
