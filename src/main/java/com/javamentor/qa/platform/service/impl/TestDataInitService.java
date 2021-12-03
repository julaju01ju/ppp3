package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.model.RoleService;
import com.javamentor.qa.platform.service.impl.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class TestDataInitService {

    private RoleService roleService;
    private UserService userService;

    @Autowired
    public TestDataInitService(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    public TestDataInitService() {
    }

    public void createUserRole() {
        Role role = new Role("USER");
        roleService.persist(role);
        User user = new User();
        user.setEmail("email@mail.com");
        user.setPassword("pass");
        user.setFullName("fullName");
        user.setPersistDateTime(LocalDateTime.of(2021, 12, 3, 0, 0));
        user.setIsEnabled(true);
        user.setIsDeleted(false);
        user.setCity("City");
        user.setLinkSite("linkSite");
        user.setLinkGitHub("linkGitHub");
        user.setLinkVk("linkVk");
        user.setAbout("About");
        user.setImageLink("imageLink");
        user.setLastUpdateDateTime(LocalDateTime.of(2021, 12, 4, 0, 0));
        user.setNickname("nickName");
        user.setRole(roleService.getAll().get(1));
        userService.persist(user);
    }
}
