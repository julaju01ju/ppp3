package com.javamentor.qa.platform.dao.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;

import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;


@Repository
public class MessageStarDaoImpl extends ReadWriteDaoImpl<MessageStar, Long> implements MessageStarDao {
    @PersistenceContext
    private EntityManager entityManager;
    UserService userService;




//    проверяет состоит ли юзер в чате, проверка проходит через группу groupchat_has_user
    @Override
    public boolean isChatHasUser(long chatId, long userId) {
        String hql = "select u.id from GroupChat gr join gr.users u where gr.id= :chatId";
        Query query = entityManager.createQuery(hql).setParameter("chatId", chatId);
        return query.getResultList().contains(userId);
    }

//    проверяет не больше ли трех избранных сообщений у юзера
    @Override
    public boolean isUserHasNoMoreThanThreeMessageStar(long userId) {
        String hql = "select count(messageStar.user.id) from MessageStar messageStar where messageStar.user.id = :userId";
        Query query = entityManager.createQuery(hql).setParameter("userId", userId);
        long count = (long) query.getSingleResult();
        return count < 3;
    }


}
