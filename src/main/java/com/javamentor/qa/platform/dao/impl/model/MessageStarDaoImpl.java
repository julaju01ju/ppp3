package com.javamentor.qa.platform.dao.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import org.springframework.stereotype.Repository;

@Repository
public class MessageStarDaoImpl extends ReadWriteDaoImpl<MessageStar, Long> implements MessageStarDao {
}
