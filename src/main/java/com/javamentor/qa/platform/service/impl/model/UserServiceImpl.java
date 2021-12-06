package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ReadWriteServiceImpl<User,Long> implements UserService {

    private PasswordEncoder passwordEncoder;
    private UserDao userDao;


    @Autowired
    public UserServiceImpl(ReadWriteDao<User, Long> readWriteDao, PasswordEncoder passwordEncoder, UserDao userDao) {
        super(readWriteDao);
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

    @Override
    public User getUserByEmail(String email) {
       return userDao.getUserByEmail(email);
    }
}
