package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;

public class PageDtoServiceImpl<T, P> implements PageDtoService<T, P> {

    PageDtoDao<T, P> pageDtoDao;

    public PageDtoServiceImpl(PageDtoDao<T, P> pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    @Override
    public PageDto<T> getPageDto(P param, int currentPageNumber, int itemsOnPage) {

        if (param == null) {
            throw new IllegalArgumentException("Не получится искать записи, когда параметр == null");
        }

        if (itemsOnPage > 0) {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }

        PageDto<T> pageDto = new PageDto<>();
        pageDto.setItemsOnPage(itemsOnPage);
        pageDto.setCurrentPageNumber(currentPageNumber);
        pageDto.setItems(pageDtoDao.getItems(param));
        pageDto.setTotalResultCount(pageDtoDao.getTotalResultCount(param));
        pageDto.setTotalPageCount((int) Math.ceil(pageDto.getTotalResultCount() / itemsOnPage));

        if (currentPageNumber > pageDto.getTotalPageCount()) {
            throw new IllegalArgumentException("Страницы под номером "
                    + currentPageNumber + " пока не существует");
        }

        return pageDto;
    }
}
