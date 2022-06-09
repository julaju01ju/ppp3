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

        List<Object[]> l = entityManager.createQuery("select singleChat, messages " +
                        "from SingleChat singleChat " +
                        "inner join Message messages on singleChat.chat.id = messages.chat.id " +
                        "join fetch singleChat.userOne a " +
                        "join fetch singleChat.useTwo b " +
                        "where messages.persistDate in (select max(b.persistDate) from Message b where " +
                        "b.chat.id = singleChat.chat.id) " +
                        "and (a.email = :username " +
                        "or b.email = :username) " +
                        "order by messages.persistDate desc")
                .setParameter("username", params.get("username"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();

        ArrayList<SingleChatDto> singleChatDtoArrayList = new ArrayList<>();
        for(Object[] o: l){
            SingleChat s = null;
            Message m = null;
            for ( Object n : o){
                try{
                    s = (SingleChat) n;
                } catch (ClassCastException e){
                    m = (Message) n;
                }
            }
            User mainUser;
            if (s.getUserOne().getUsername().equals(params.get("username"))) {
                mainUser = s.getUseTwo();
            } else {
                mainUser = s.getUserOne();
            }
            singleChatDtoArrayList.add(new SingleChatDto( s.getChat().getId(), mainUser.getNickname(),
                    mainUser.getImageLink(), m.getMessage(), m.getPersistDate()));
        }

        return singleChatDtoArrayList;
    }

    @Override
    public int getTotalResultCount(Map params) {
        final String query =
                "select singleChat from SingleChat singleChat " +
                        "join fetch singleChat.userOne a " +
                        "join fetch singleChat.useTwo b " +
                        "where a.email = :username " +
                        "or b.email = :username";

        return entityManager.createQuery(query).setParameter("username", params.get("username"))
                .getResultList().size();
    }
}
