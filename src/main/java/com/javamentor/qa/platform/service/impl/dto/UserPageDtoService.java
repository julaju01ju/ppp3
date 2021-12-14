package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserPageDtoService {

    PageDtoServiceImpl<UserDtoTest> paginationService;

    @Autowired
    public UserPageDtoService(PageDtoServiceImpl<UserDtoTest> paginationService) {
        this.paginationService = paginationService;
    }

    public PageDto<UserDtoTest> getPaginationUserTest(Long id, int currentPageNumber, int itemsOnPage) {
        PageDto<UserDtoTest> pageDto;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("currentPageNumber", currentPageNumber);
        params.put("itemsOnPage", itemsOnPage);
        pageDto = paginationService.getPageDto("paginationUserTest", params);
        return pageDto;
    }
}
