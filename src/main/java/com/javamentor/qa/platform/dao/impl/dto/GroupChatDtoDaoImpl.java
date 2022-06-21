package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.GroupChatDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupChatDtoDaoImpl implements GroupChatDtoDao {

    private final GroupChatDtoResultTransformer groupChatDtoResultTransformer;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GroupChatDtoDaoImpl(GroupChatDtoResultTransformer groupChatDtoResultTransformer) {
        this.groupChatDtoResultTransformer = groupChatDtoResultTransformer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select g.id, " +
                                "c.title " +
                                "from GroupChat g, " +
                                "Chat c where g.id = c.id")
                .unwrap(Query.class)
                .setMaxResults(1)
                .setResultTransformer(
                        groupChatDtoResultTransformer.getGroupChatDto(pageDtoDaoName, params))
        );
    }
}

