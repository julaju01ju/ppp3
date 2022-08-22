package com.javamentor.qa.platform.service.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Service
public class MessageStarServiceImpl extends ReadWriteServiceImpl<MessageStar, Long> implements MessageStarService{
    @PersistenceContext
    private EntityManager entityManager;
    private MessageStarDao messageStarDao;

    @Autowired
    public MessageStarServiceImpl(MessageStarDao messageStarDao) {
        super(messageStarDao);
        this.messageStarDao = messageStarDao;
    }




    //    проверяет состоит ли юзер в чате, проверка проходит через группу groupchat_has_user
    @Override
    public boolean isChatHasUser(long chatId, long userId) {

        Query query = entityManager.createQuery("select u.id from GroupChat gr join gr.users u where gr.id= :chatId")
                .setParameter("chatId", chatId);

        return query.getResultList().size() > 0;
    }

    //    проверяет не больше ли трех избранных сообщений у юзера
    @Override
    public boolean isUserHasNoMoreThanThreeMessageStar(long userId) {


        Query query = entityManager.createQuery("select count(messageStar.user.id) from MessageStar messageStar where messageStar.user.id = :userId")
                .setParameter("userId", userId);


        return Integer.parseInt(String.valueOf(query.getSingleResult())) < 3;
    }
}
