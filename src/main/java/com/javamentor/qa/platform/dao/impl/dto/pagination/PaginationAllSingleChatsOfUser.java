package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
public class PaginationAllSingleChatsOfUser implements PageDtoDao<SingleChatDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SingleChatDto> getItems(Map params) {

        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        final String query = "select new com.javamentor.qa.platform.models.dto.SingleChatDto(" +
                "singleChat.chat.id, case when singleChat.userOne.id = :userId then singleChat.useTwo.nickname " +
                "else singleChat.userOne.nickname end, " +
                "case when singleChat.userOne.id = :userId then singleChat.useTwo.imageLink " +
                "else singleChat.userOne.imageLink end, " +
                "message.message, message.persistDate) " +
                "from SingleChat singleChat, Message message " +
                "where message.persistDate in (select max(messageMaxDate.persistDate) from Message messageMaxDate where " +
                "messageMaxDate.chat.id = singleChat.chat.id) " +
                "and (singleChat.userOne.id = :userId " +
                "or singleChat.useTwo.id = :userId) " +
                "order by message.persistDate desc";

        List<Object[]> l = entityManager.createQuery("select singleChat, messages " +
                        "from SingleChat singleChat " +
                        "inner join Message messages on singleChat.chat.id = messages.chat.id " +
                        "join fetch singleChat.userOne userOne " +
                        "join fetch singleChat.useTwo userTwo " +
                        "where messages.persistDate in (select max(messageMaxDate.persistDate) from Message messageMaxDate where " +
                        "messageMaxDate.chat.id = singleChat.chat.id) " +
                        "and (userOne.id = :userId " +
                        "or userTwo.id = :userId) " +
                        "order by messages.persistDate desc")
                .setParameter("userId", params.get("userId"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();

        return null;

//        return entityManager.createQuery(query).setParameter("userId", params.get("userId"))
//                .setFirstResult((page - 1) * itemsOnPage)
//                .setMaxResults(itemsOnPage)
//                .getResultList();
//        forEach((element) -> System.out.println(element.toString()));

//        ArrayList<SingleChatDto> singleChatDtoArrayList = new ArrayList<>();
//        for(Object[] o: l){
//            SingleChat s = null;
//            Message m = null;
//            for ( Object n : o){
//                try{
//                    s = (SingleChat) n;
//                } catch (ClassCastException e){
//                    m = (Message) n;
//                }
//            }
//            User mainUser;
//            if (s.getUserOne().getId().equals(params.get("userId"))) {
//                mainUser = s.getUseTwo();
//            } else {
//                mainUser = s.getUserOne();
//            }
//            singleChatDtoArrayList.add(new SingleChatDto( s.getChat().getId(), mainUser.getNickname(),
//                    mainUser.getImageLink(), m.getMessage(), m.getPersistDate()));
//        }
//
//        return singleChatDtoArrayList;
    }

    @Override
    public int getTotalResultCount(Map params) {
        final String query =
                "select singleChat from SingleChat singleChat " +
                        "join fetch singleChat.userOne userOne " +
                        "join fetch singleChat.useTwo userTwo " +
                        "where userOne.id = :userId " +
                        "or userTwo.id = :userId";

        return entityManager.createQuery(query).setParameter("userId", params.get("userId"))
                .getResultList().size();
    }
}
