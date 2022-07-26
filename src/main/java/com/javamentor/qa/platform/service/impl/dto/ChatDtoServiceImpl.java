package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatDtoServiceImpl implements ChatDtoService {

    private final ChatDtoDao findChatByStringDtoDao;

    public ChatDtoServiceImpl(ChatDtoDao findChatByStringDtoDao) {
        this.findChatByStringDtoDao = findChatByStringDtoDao;
    }

    @Override
    public List<ChatDto> getChatByString(Long userId, String searchedString) {
        return findChatByStringDtoDao.getChatByString(userId, searchedString);
    }

}
