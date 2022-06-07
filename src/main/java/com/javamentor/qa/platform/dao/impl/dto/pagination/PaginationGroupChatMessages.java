package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;

import java.util.List;
import java.util.Map;

public class PaginationGroupChatMessages implements PageDtoDao<GroupChatDto> {

    @Override
    public List<GroupChatDto> getItems(Map<String, Object> params) {
        return null;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return 0;
    }
}
