package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.FindChatByStringDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.FindChatByStringDtoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FindChatByStringDtoServiceImpl implements FindChatByStringDtoService {

    private final FindChatByStringDtoDao findChatByStringDtoDao;

    public FindChatByStringDtoServiceImpl(FindChatByStringDtoDao findChatByStringDtoDao) {
        this.findChatByStringDtoDao = findChatByStringDtoDao;
    }

    @Override
    public List<ChatDto> getChatByString(String searchedString) {
        return findChatByStringDtoDao.getChatByString(searchedString);
    }

    @Override
    public boolean ifNotExistSearchedString(String  searchedString) {
        return getChatByString(searchedString).isEmpty();
    }
}
