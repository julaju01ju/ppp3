package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;


@Repository
public class ChatDtoDaoImpl implements ChatDtoDao {


    @PersistenceContext
    private EntityManager entityManager;

    public List<ChatDto> getChatByString(Long userId, String searchedString) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.ChatDto " +
                        "(ch.id, "+
                        "ch.title, "+
                        "case " +
                        "when ch.chatType=:chatTypeSingle then u.imageLink " +
                        "else " +
                        "(select gc.imageChat from GroupChat as gc where gc.id=ch.id)" +
                        "end , "+
                        "m.message, "+
                        "m.persistDate) "+
                        "from Chat  as ch " +
                        "join Message as m  on ch.id = m.chat.id " +
                        "left join GroupChat as gc on m.chat.id=gc.chat.id " +
                        "left join User as u on m.userSender.id = u.id "+
                        "where upper(ch.title) like upper(:searchString) and "+
                        "(ch.id in(select sc.chat.id from SingleChat as sc " +
                        "where sc.userOne.id= :userId or sc.useTwo = :userId) " +
                        "or " +
                        "ch.id in (select g.id  from  GroupChat as g " +
                        "join g.users as ghu on ghu.id = :userId)) and " +
                        "m.persistDate = (select max(mes.persistDate) from Message as mes where mes.chat.id=ch.id) " +
                        "order by m.persistDate desc ", ChatDto.class)
                .setParameter("searchString", "%" + searchedString + "%")
                .setParameter("userId", userId)
                .setParameter("chatTypeSingle", ChatType.SINGLE)
                .getResultList();
    }
}
