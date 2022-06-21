package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupChatDtoDaoImpl implements GroupChatDtoDao {

    private final MessageDtoService messageDtoService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GroupChatDtoDaoImpl(MessageDtoService messageDtoService) {
        this.messageDtoService = messageDtoService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {

        GroupChatDto groupChatDto = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.GroupChatDto" +
                                "(g.id, " +
                                "c.title) " +
                                "from GroupChat g, " +
                                "Chat c where g.id = c.id", GroupChatDto.class)
                .setMaxResults(1)).get();

        groupChatDto.setPage(messageDtoService.getPageDto(pageDtoDaoName, params));
        return Optional.of(groupChatDto);
    }
}

