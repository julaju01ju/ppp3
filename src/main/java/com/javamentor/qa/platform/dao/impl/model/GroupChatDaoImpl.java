package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class GroupChatDaoImpl extends ReadWriteDaoImpl<GroupChat, Long> implements GroupChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GroupChat> getGroupChatById(Long id) {
        TypedQuery<GroupChat> query = entityManager.createQuery("select gch from GroupChat gch left join fetch gch.users" +
                " where gch.id = :id", GroupChat.class).setParameter("id", id);
    return SingleResultUtil.getSingleResultOrNull(query);
    }
}
