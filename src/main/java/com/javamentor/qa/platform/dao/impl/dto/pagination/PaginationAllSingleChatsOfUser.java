package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoService;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
public class PaginationAllSingleChatsOfUser implements PageDtoDao<SingleChatDto> {

    private SingleChatDtoService singleChatDtoService;

    @Autowired
    public PaginationAllSingleChatsOfUser(SingleChatDtoService singleChatDtoService){
        this.singleChatDtoService = singleChatDtoService;
    }

    @Override
    public List<SingleChatDto> getItems(Map params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return singleChatDtoService.receiveSingleChatsDtosByUsername((String)params.get("username"),
                page, itemsOnPage);

    }

    @Override
    public int getTotalResultCount(Map params) {
        return singleChatDtoService.receiveSingleChatsDtosByUsername((String)params.get("username"),
                0, 0).size();
    }
}
