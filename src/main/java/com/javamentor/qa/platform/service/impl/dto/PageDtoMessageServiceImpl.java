package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.MessageViewDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoMessageService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PageDtoMessageServiceImpl extends PageDtoServiceImpl<MessageViewDto> implements PageDtoMessageService {

    @Override
    public PageDto<MessageViewDto> getPageDtoMessage(String pageDtoDaoName, Map<String, Object> params) {
        PageDto<MessageViewDto> pageDto = super.getPageDto(pageDtoDaoName, params);
        return pageDto;
    }

}
