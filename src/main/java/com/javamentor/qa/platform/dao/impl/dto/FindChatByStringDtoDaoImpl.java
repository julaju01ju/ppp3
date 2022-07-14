package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.FindChatByStringDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class FindChatByStringDtoDaoImpl implements FindChatByStringDtoDao {


    @PersistenceContext
    private EntityManager entityManager;

    public List<ChatDto> getChatByString(String searchString) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.ChatDto " +
                        "(ch.id, " +
                        "ch.title, " +
                        "case " +
                        "when ch.chatType = 1 then (select gc.imageChat from GroupChat as gc where gc.id=ch.id) " +
                        "else (select u.imageLink from user as u where u.id=m.userSender.id) " +
                        "end, " +
                        "m.message," +
                        "m.persistDate) " +
                        "from Chat as ch, Message as m, User as user " +
                        "where upper(ch.title) like upper(:searchString) " +
                        "and ch.id=m.chat.id " +
                        "and m.userSender.id=user.id  " +
                        "and m.persistDate in (select max(messageMaxDate.persistDate) from Message as messageMaxDate " +
                        "where messageMaxDate.chat.id = ch.id)" +
                        "order by m.persistDate desc ", ChatDto.class)
                .setParameter("searchString", "%" + searchString + "%")
                .getResultList();
    }
}
