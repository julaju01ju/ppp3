package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl extends ReadWriteServiceImpl<User,Long> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;


    @Autowired
    public UserServiceImpl( PasswordEncoder passwordEncoder, UserDao userDao) {
        super(userDao);
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }


    @Override
    public void persist(User user) {
        String pass = user.getPassword();
        user.setPassword(passwordEncoder.encode(pass));
        super.persist(user);
    }

    @Override
    public void update(User user) {
        String pass = user.getPassword();
        user.setPassword(passwordEncoder.encode(pass));
        super.update(user);
    }

}
