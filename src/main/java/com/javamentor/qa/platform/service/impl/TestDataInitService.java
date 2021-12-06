package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.model.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class TestDataInitService {

    private RoleServiceImpl roleService;
    private UserService userService;

    @Autowired
    public TestDataInitService(RoleServiceImpl roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    public TestDataInitService() {
    }

    public void createRole(){
        Role role = new Role("USER");
        roleService.persist(role);
    }

    public void createUser() {
        for(int i = 0; i <= 50; i++){
            User user = new User();
            user.setEmail(i+"@mail.com");
            user.setPassword("pass"+i);
            user.setFullName("fullName"+i);
            user.setPersistDateTime(LocalDateTime.of(2021, 12, 3, 0, 0));
            user.setIsEnabled(true);
            user.setIsDeleted(false);
            user.setCity("City"+i);
            user.setLinkSite("linkSite"+i);
            user.setLinkGitHub("linkGitHub"+i);
            user.setLinkVk("linkVk"+i);
            user.setAbout("About"+i);
            user.setImageLink("imageLink"+i);
            user.setLastUpdateDateTime(LocalDateTime.of(2021, 12, 4, 0, 0));
            user.setNickname(("nickName"+i));
            user.setRole(roleService.getAll().get(1));
            userService.persist(user);
        }
    }
}
