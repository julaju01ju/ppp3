package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.service.impl.dto.PageDtoServiceImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GroupChatDtoResultTransformer extends PageDtoServiceImpl<MessageDto> {

    public ResultTransformer getGroupChatDto(String pageDtoDaoName, Map<String, Object> params) {
        PageDto<MessageDto> pageDto = super.getPageDto(pageDtoDaoName, params);

        return new ResultTransformer() {
            @Override
            public Object transformTuple (Object[]tuple, String[]aliases){
                GroupChatDto groupChatDto = new GroupChatDto();
                groupChatDto.setId((Long) tuple[0]);
                groupChatDto.setChatName((String) tuple[1]);
                groupChatDto.setPage(pageDto);
                return groupChatDto;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
    }
}
