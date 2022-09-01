package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GroupChatDaoImpl extends ReadWriteDaoImpl<GroupChat, Long> implements GroupChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GroupChat> getGroupChatWithUsersById(Long id) {
        TypedQuery<GroupChat> query = entityManager.createQuery("select gch from GroupChat gch left join fetch gch.users" +
                " where gch.id = :id", GroupChat.class).setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public boolean isUsersChat(Long chatId, User user) {
        return Objects.equals(entityManager.createQuery("select count(gc.id) from GroupChat gc " +
                        "join gc.users u on u.id = :uid where gc.id = :chatId")
                .setParameter("chatId", chatId)
                .setParameter("uid", user.getId())
                .getSingleResult().toString(), "1");
    }
}
