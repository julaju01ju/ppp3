package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.GroupChatDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.MessageDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupChatDtoDaoImpl implements GroupChatDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unckecked")
    @Override
    public Optional<MessageDto> getGroupChatByMessageIdAndUserId(Long messageId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select m.id, " +
                                "m.message, " +
                                "m.persistDate, " +
                                "u.id, " +
                                "u.fullName, " +
                                "u.imageLink " +
                                "from Message m, " +
                                "User u " +
                                "where m.id = :messageId " +
                                "and u.id = :userId")
                .setParameter("messageId", messageId)
                .setParameter("userId", userId)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {
                                          @Override
                                          public Object transformTuple(Object[] tuple, String[] aliases) {
                                              MessageDto messageDto = new MessageDto();
                                              messageDto.setId((Long) tuple[0]);
                                              messageDto.setMessage((String) tuple[1]);
                                              messageDto.setPersistDateTime((LocalDateTime) tuple[2]);
                                              messageDto.setUserId((Long) tuple[3]);
                                              messageDto.setNickName((String) tuple[4]);
                                              messageDto.setImage((String) tuple[5]);

                                              return messageDto;
                                          }

                                          @Override
                                          public List transformList(List list) {
                                              return list;
                                          }
                                      }
                ));
    }
}

