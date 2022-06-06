package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.SingleChatDtoService;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
public class SingleChatDtoDaoImpl implements SingleChatDtoService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public List<SingleChatDto> receiveSingleChatsDtosByUsername(String username, int page, int itemsOnPage) {
        final String query =
                "select singleChat from SingleChat singleChat " +
                        "join fetch singleChat.userOne a " +
                        "join fetch singleChat.useTwo b " +
                        "where a.email = '" + username + "' " +
                        "or b.email = '" + username + "'";

        ArrayList<SingleChat> singleChatsList;

        if( page > 0 && itemsOnPage >0 ){
            singleChatsList = new ArrayList<>(entityManager.createQuery(query)
                    .setFirstResult((page -1) * itemsOnPage)
                    .setMaxResults(itemsOnPage)
                    .getResultList());

        } else{
            singleChatsList = new ArrayList<>(entityManager.createQuery(query)
                    .getResultList());
        }

        LinkedHashMap<SingleChat, Message> singleChatMessageLinkedHashMap = new LinkedHashMap<>();

        for (SingleChat s: singleChatsList){
            singleChatMessageLinkedHashMap.put(s, receiveLastMessageBySingleChatId(s.getId()));
        }

        ArrayList<SingleChatDto> singleChatDtoArrayList = new ArrayList<>();

        singleChatMessageLinkedHashMap.forEach((chat, message)-> {
            User mainUser;

            if (chat.getUserOne().getUsername().equals(username)) {
                mainUser = chat.getUseTwo();
            } else {
                mainUser = chat.getUserOne();
            }
            singleChatDtoArrayList.add(
                    new SingleChatDto(chat.getId(), mainUser.getNickname(),
                            mainUser.getImageLink(), message.getMessage(), message.getPersistDate()));
        });

        singleChatDtoArrayList.sort( (x, y) -> {
            if (x.getPersistDateTimeLastMessage().compareTo(y.getPersistDateTimeLastMessage()) <= 0){
                return 1;
            }
            return -1;
        });
        return singleChatDtoArrayList;
    }

    @Override
    @Transactional
    public Message receiveLastMessageBySingleChatId(Long id) {
        final String query = "from Message as message join fetch message.chat c where c.id = '" + id +"' " +
                "order by message.persistDate desc";
        return (Message) entityManager.createQuery(query).setMaxResults(1).getSingleResult();
    }
}
