package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MessageDtoServiceImpl extends PageDtoServiceImpl<MessageDto> implements MessageDtoService {

    @Override
    public PageDto<MessageDto> getPageMessages(String pageDtoDaoName, Map<String, Object> params) {

        PageDto<MessageDto> pageDto = super.getPageDto(pageDtoDaoName, params);
        return pageDto;
    }
}
