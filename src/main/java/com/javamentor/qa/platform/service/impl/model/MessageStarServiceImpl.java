package com.javamentor.qa.platform.service.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MessageStarServiceImpl extends ReadWriteServiceImpl<MessageStar, Long> implements MessageStarService{
    private MessageStarDao messageStarDao;

    @Autowired
    public MessageStarServiceImpl(MessageStarDao messageStarDao) {
        super((ReadWriteDao<MessageStar, Long>) messageStarDao);
        this.messageStarDao = messageStarDao;
    }


    @Override
    public Object isChatHasUser(long chatId, long userId) {

        return messageStarDao.isChatHasUser(chatId, userId);
    }

    @Override
    public Object isUserHasNoMoreThanThreeMessageStar(long userId) {
        return messageStarDao.isUserHasNoMoreThanThreeMessageStar(userId);
    }

}
