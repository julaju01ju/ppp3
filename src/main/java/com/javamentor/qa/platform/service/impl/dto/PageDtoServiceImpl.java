package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.enums.Period;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@NoArgsConstructor
@Setter
public class PageDtoServiceImpl<T> implements PageDtoService<T> {

    @Autowired
    Map<String, PageDtoDao<T>> pageDtoDaoMap;

    public PageDtoServiceImpl(Map<String, PageDtoDao<T>> pageDtoDaoMap) {
        this.pageDtoDaoMap = pageDtoDaoMap;
    }

    @Transactional
    @Override
    public PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params) {

        if (params.isEmpty()) {
            throw new IllegalArgumentException("Не получится искать записи, когда параметры == null");
        }

        if ((int) params.get("itemsOnPage") <= 0) {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }

        PageDto<T> pageDto = new PageDto<>();
        PageDtoDao<T> pageDtoDao;

        if (pageDtoDaoMap.containsKey(pageDtoDaoName)) {
            pageDtoDao = pageDtoDaoMap.get(pageDtoDaoName);
        } else {
            throw new IllegalArgumentException("Неправильное имя PageDtoDao");
        }

        pageDto.setItemsOnPage((int) params.get("itemsOnPage"));
        pageDto.setCurrentPageNumber((int) params.get("currentPageNumber"));
        if (!params.containsKey("period")) {
            params.put("period", Period.ALL);
        }
        pageDto.setPeriod((Period) params.get("period"));
        params.put("truncedDate", getTruncedDate((Period) params.get("period")));
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

    private static LocalDateTime getTruncedDate(Period period) {
        LocalDateTime res;
        switch (period) {
            case WEEK:
                res = LocalDateTime.now().minusWeeks(1);
                break;
            case MONTH:
                res = LocalDateTime.now().minusMonths(1);
                break;
            case YEAR:
                res = LocalDateTime.now().minusYears(1);
                break;
            default:
                res = LocalDateTime.MIN;
        }
        return res;
    }

}
