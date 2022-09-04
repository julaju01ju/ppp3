package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupChatDtoDaoImpl implements GroupChatDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.GroupChatDto" +
                                "(g.id, " +
                                "g.title) " +
                                "from GroupChat g, " +
                                "Chat c where g.id = c.id", GroupChatDto.class)
                .setMaxResults(1));
    }
}

