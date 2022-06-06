package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
public class SingleChatDtoServiceImpl implements com.javamentor.qa.platform.service.abstracts.dto.SingleChatDtoService,
        PageDtoService<SingleChatDto> {

    private com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoService singleChatDtoDao;
    private PageDtoDao<SingleChatDto> pageDtoDao;

    @Autowired
    public SingleChatDtoServiceImpl(com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoService singleChatDtoDao, PageDtoDao<SingleChatDto> pageDtoDao){
        this.singleChatDtoDao = singleChatDtoDao;
        this.pageDtoDao = pageDtoDao;
    }

    @Override
    public List<SingleChatDto> receiveSingleChatsDtosByUsername(String username, int page, int itemsOnPage) {
        return singleChatDtoDao.receiveSingleChatsDtosByUsername(username, page, itemsOnPage);
    }

    @Override
    public PageDto<SingleChatDto> getPageDto(Map<String, Object> params) {

        if (params.isEmpty()) {
            throw new IllegalArgumentException("Не получится искать записи, когда параметры == null");
        }

        if ((int) params.get("itemsOnPage") <= 0) {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }

        PageDto<SingleChatDto> pageDto = new PageDto<>();
        pageDto.setItemsOnPage((int) params.get("itemsOnPage"));
        pageDto.setCurrentPageNumber((int) params.get("currentPageNumber"));
        pageDto.setItems(pageDtoDao.getItems(params));
        pageDto.setTotalResultCount(pageDtoDao.getTotalResultCount(params));
        pageDto.setTotalPageCount((int) (Math.ceil(((double) pageDto.getTotalResultCount() / (int) params.get("itemsOnPage")))));
        if (pageDto.getTotalPageCount() == 0) {
            pageDto.setTotalPageCount(1);
        }
        if ((int) params.get("currentPageNumber") > pageDto.getTotalPageCount()) {
            throw new IllegalArgumentException("Страницы под номером "
                    + params.get("currentPageNumber") + " пока не существует");
        }

        return pageDto;
    }
}
