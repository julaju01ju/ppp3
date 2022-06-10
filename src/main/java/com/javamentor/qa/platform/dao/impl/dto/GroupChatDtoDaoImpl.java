package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoMessageService;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupChatDtoDaoImpl implements GroupChatDtoDao {

    private final PageDtoMessageService pageDtoMessageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GroupChatDtoDaoImpl(PageDtoMessageService pageDtoMessageService) {
        this.pageDtoMessageService = pageDtoMessageService;
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
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuple, String[] aliases) {

                        GroupChatDto groupChatDto = new GroupChatDto();
                        groupChatDto.setId((Long) tuple[0]);
                        groupChatDto.setChatName((String) tuple[1]);
                        groupChatDto.setPage(
                                pageDtoMessageService
                                        .getPageDtoMessage(pageDtoDaoName, params));

                        return groupChatDto;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
        );
    }
}

