package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.MessageStar;



public interface MessageStarDao extends ReadWriteDao<MessageStar, Long>{
    boolean isChatHasUser(long chatId, long userId);
    boolean isUserHasNoMoreThanThreeMessageStar(long userId);

}
