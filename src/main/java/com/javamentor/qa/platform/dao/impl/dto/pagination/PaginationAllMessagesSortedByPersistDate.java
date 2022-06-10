package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.*;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllMessagesSortedByPersistDate implements PageDtoDao<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createNativeQuery(
                        "SELECT " +
                                "m.id AS m_id, " +
                                "m.message, " +
                                "m.persist_date, " +
                                "u.id, " +
                                "u.nickname, " +
                                "u.image_link " +

                                "FROM message m " +
                                "JOIN user_entity u ON m.user_sender_id = u.id " +
                                "WHERE m.chat_id = :chatId " +
                                "ORDER BY m.persist_date DESC")
                .setParameter("chatId", params.get("chatId"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {

                                          @Override
                                          public Object transformTuple(Object[] tuple, String[] aliases) {

                                              MessageDto messageDto = new MessageDto();
                                              messageDto.setId(((BigInteger) tuple[0]).longValue());
                                              messageDto.setMessage((String) tuple[1]);
                                              messageDto.setPersistDateTime(((Timestamp) tuple[2]).toLocalDateTime());
                                              messageDto.setUserId(((BigInteger) tuple[3]).longValue());
                                              messageDto.setNickName((String) tuple[4]);
                                              messageDto.setImage((String) tuple[5]);

                                              return messageDto;
                                          }

                                          @Override
                                          public List transformList(List list) {
                                              return list;
                                          }
                                      }
                ).getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                ("Select CAST(count(message.id) as int) AS countMessages from Message message WHERE message.chat.id = :chatId")
                .setParameter("chatId", params.get("chatId"));
        return (int) queryTotal.getSingleResult();
    }
}
