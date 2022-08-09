package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SingleChatServiceImpl extends ReadWriteServiceImpl<SingleChat, Long> implements SingleChatService {

    private final SingleChatDao singleChatDao;

    private final MessageService messageService;


    @Autowired
    public SingleChatServiceImpl(SingleChatDao singleChatDao, MessageService messageService) {
        super(singleChatDao);
        this.singleChatDao = singleChatDao;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public Optional<SingleChat> addSingleChatAndMessage(SingleChat singleChat, String message) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        messageService.persist(new Message(message, sender, singleChat.getChat()));
        return singleChatDao.addSingleChatAndMessage(singleChat, message);
    }

}
