package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class GroupChatDtoServiceImpl extends PageDtoServiceImpl<MessageDto> implements GroupChatDtoService {

    private final GroupChatDtoDao groupChatDtoDao;

    @Autowired
    public GroupChatDtoServiceImpl(GroupChatDtoDao groupChatDtoDao) {
        this.groupChatDtoDao = groupChatDtoDao;
    }

    @Override
    public Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {
        Optional<GroupChatDto> groupChatDto = groupChatDtoDao.getOptionalGroupChatDto(pageDtoDaoName, params);
        return groupChatDto;
    }
}
