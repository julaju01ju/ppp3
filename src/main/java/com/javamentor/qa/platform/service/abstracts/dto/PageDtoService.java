package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;

public interface PageDtoService<T> {
    default PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params){
        return null;
    }
    default PageDto<T> getPageDto(Map<String, Object> params){
        return null;
    }
}
