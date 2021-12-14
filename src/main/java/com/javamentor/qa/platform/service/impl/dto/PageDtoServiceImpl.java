package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PageDtoServiceImpl<T> implements PageDtoService<T> {

    Map <String, PageDtoDao<T>> pageDtoDaoMap;

    @Autowired
    public PageDtoServiceImpl(Map < String, PageDtoDao<T>> pageDtoDaoMap) {
        this.pageDtoDaoMap = pageDtoDaoMap;
    }

    @Override
    public PageDto<T> getPageDto(String pageDtoDaoName,Map<String, Object> params, int currentPageNumber, int itemsOnPage) {

        if (params.isEmpty()) {
            throw new IllegalArgumentException("Не получится искать записи, когда параметры == null");
        }

        if (itemsOnPage > 0) {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }

        PageDto<T> pageDto = new PageDto<>();
        PageDtoDao<T> pageDtoDao = pageDtoDaoMap.get(pageDtoDaoName);

        pageDto.setItemsOnPage(itemsOnPage);
        pageDto.setCurrentPageNumber(currentPageNumber);
        pageDto.setItems(pageDtoDao.getItems(params));
        pageDto.setTotalResultCount(pageDtoDao.getTotalResultCount(params));
        pageDto.setTotalPageCount((int) Math.ceil(pageDto.getTotalResultCount() / itemsOnPage));

        if (currentPageNumber > pageDto.getTotalPageCount()) {
            throw new IllegalArgumentException("Страницы под номером "
                    + currentPageNumber + " пока не существует");
        }

        return pageDto;
    }
}
