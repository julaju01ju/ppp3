package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService extends ReadWriteService<User,Long>{
    void updatePassword(String username, String password);
}
