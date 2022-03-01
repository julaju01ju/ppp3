package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;


public interface UserService extends ReadWriteService<User,Long>{
    void updatePasswordByEmail(String email, String password);

    void disableUserByEmail(String email);

    User getUserByEmail(String email);
}
