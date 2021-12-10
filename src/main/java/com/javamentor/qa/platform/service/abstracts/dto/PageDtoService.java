package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;

public interface PageDtoService<T, P> {
    PageDto<T> getPageDto(P param, int currentPageNumber, int itemsOnPage);
}
