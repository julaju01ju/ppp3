package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@NoArgsConstructor
@Setter
public class PageDtoServiceImpl<T> implements PageDtoService<T> {

    Map<String, PageDtoDao<T>> pageDtoDaoMap;

    public PageDtoServiceImpl(Map<String, PageDtoDao<T>> pageDtoDaoMap) {
        this.pageDtoDaoMap = pageDtoDaoMap;
    }

    @Override
    public PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params) {

        if (params.isEmpty()) {
            throw new IllegalArgumentException("Не получится искать записи, когда параметры == null");
        }

        if ((int)params.get("itemsOnPage") <= 0) {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }

        PageDto<T> pageDto = new PageDto<>();
        PageDtoDao<T> pageDtoDao = pageDtoDaoMap.get(pageDtoDaoName);

        pageDto.setItemsOnPage((int)params.get("itemsOnPage"));
        pageDto.setCurrentPageNumber((int)params.get("currentPageNumber"));
        pageDto.setItems(pageDtoDao.getItems(params));
        pageDto.setTotalResultCount(pageDtoDao.getTotalResultCount(params));
        pageDto.setTotalPageCount((int) (Math.ceil(((double)pageDto.getTotalResultCount() / (int)params.get("itemsOnPage")))));

        if ((int)params.get("currentPageNumber") > pageDto.getTotalPageCount()) {
            throw new IllegalArgumentException("Страницы под номером "
                    + params.get("currentPageNumber") + " пока не существует");
        }

        return pageDto;
    }
}
