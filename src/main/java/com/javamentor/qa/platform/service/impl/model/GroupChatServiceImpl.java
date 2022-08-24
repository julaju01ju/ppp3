package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupChatServiceImpl extends ReadWriteServiceImpl<GroupChat, Long> implements GroupChatService {

    private final GroupChatDao groupChatDao;

    @Autowired
    public GroupChatServiceImpl(GroupChatDao groupChatDao) {
        super(groupChatDao);
        this.groupChatDao = groupChatDao;
    }

    @Override
    public Optional<GroupChat> getGroupChatWithUsersById(Long id) {
        return groupChatDao.getGroupChatWithUsersById(id);
    }

    @Override
    public void deleteById(Long id) {

        Optional<GroupChat> groupChat = getById(id);

        if(groupChat.isPresent()) {
            User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            groupChat.get().getUsers().remove(user);
            update(groupChat.get());
        }
    }
}
