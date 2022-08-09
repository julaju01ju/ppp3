package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class SingleChatDaoImpl extends ReadWriteDaoImpl<SingleChat, Long> implements SingleChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SingleChat> addSingleChatAndMessage(SingleChat singleChat, String message) {
        TypedQuery<SingleChat> query = entityManager.createQuery(
                        "select sch from SingleChat sch" +
                                " join Message m  on sch.id = m.chat.id " +
                                "where sch.useTwo = :id " +
                                "AND m.message = :message", SingleChat.class)
                .setParameter("id", singleChat.getUseTwo())
                .setParameter("message", message);
        return SingleResultUtil.getSingleResultOrNull(query);

    }
}
