package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Service;


public interface UserService extends ReadWriteService<User,Long>{
    public User getUserByEmail(String email);
}
