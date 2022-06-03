package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoDao;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.service.abstracts.dto.SingleChatDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingleChatDtoServiceImpl implements SingleChatDtoService {

    private SingleChatDtoDao singleChatDtoDao;

    @Override
    public List<SingleChat> receiveSingleChatsByUsername(String username) {
        return singleChatDtoDao.receiveSingleChatsByUsername(username);
    }

    @Override
    public Message receiveLastMessageBySingkeChatId(Long id) {
        return singleChatDtoDao.receiveLastMessageBySingkeChatId(id);
    }
}
