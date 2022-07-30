package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class GroupChatDtoServiceImpl extends PageDtoServiceImpl<MessageDto> implements GroupChatDtoService {

    private final MessageDtoService messageDtoService;
    private final GroupChatDtoDao groupChatDtoDao;

    @Autowired
    public GroupChatDtoServiceImpl(GroupChatDtoDao groupChatDtoDao, MessageDtoService messageDtoService) {
        this.messageDtoService = messageDtoService;
        this.groupChatDtoDao = groupChatDtoDao;
    }

    @Override
    public Optional<GroupChatDto> getOptionalGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {
        Optional<GroupChatDto> groupChatDto = groupChatDtoDao.getOptionalGroupChatDto(pageDtoDaoName, params);
                groupChatDto
                .ifPresent(chatDto -> chatDto.setPage(
                        messageDtoService.getPageDto(pageDtoDaoName, params)));
        return groupChatDto;
    }
}
