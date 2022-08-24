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

import java.util.Objects;
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
    public void addSingleChatAndMessage(SingleChat singleChat, String message) {

        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        messageService.persist(new Message(message, sender, singleChat.getChat()));
    }
    @Override
    public boolean isDeleted(Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<SingleChat> singleChat = singleChatDao.getById(id);

        if(singleChat.isPresent()) {
            if (Objects.equals(user.getId(), singleChat.get().getUserOne().getId())) {
                return singleChat.get().isDeleteOne();
            }else if (Objects.equals(user.getId(), singleChat.get().getUseTwo().getId())) {
                return singleChat.get().isDeleteTwo();
            }
        }

        return true;
    }

    @Override
    public void deleteById(Long id) {

        Optional<SingleChat> singleChat = getById(id);

        if (singleChat.isPresent()) {

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (Objects.equals(user.getId(), singleChat.get().getUserOne().getId())) {
                singleChat.get().setDeleteOne(true);
            } else if (Objects.equals(user.getId(), singleChat.get().getUseTwo().getId())) {
                singleChat.get().setDeleteTwo(true);
            }

            update(singleChat.get());
        }

    }
}

