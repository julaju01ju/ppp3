package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

//@Service
public class GroupChatDtoServiceImpl extends PageDtoServiceImpl<MessageDto> {

    public PageDto<MessageDto> getPageDtoMessage(String pageDtoDaoName, Map<String, Object> params) {
        PageDto<MessageDto> pageDto = super.getPageDto(pageDtoDaoName, params);
        return pageDto;
    }

    public GroupChatDto getPageDtoGroupChat() {
        GroupChatDto result = new GroupChatDto();
        result.setId(1);
        result.setChatName("1group");
        result.setImage("dick.pick");
        result.setPage(getPageDtoMessage(null, null));
        result.setPersistDateTime(LocalDateTime.now());
        return result;
    }

    public Optional<GroupChatDto> getGroupChatByGroupChatIdAndUserId(Long groupChatId, Long userId) {
       // Optional<GroupChatDto> groupChatDto = groupChatDtoDao.get....
        return null;
    }

}
