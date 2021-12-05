package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends ReadWriteDaoImpl<User,Long>{

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDao(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void persist(User e) {
        String password = e.getPassword();
        e.setPassword(passwordEncoder.encode(password));
        super.persist(e);
    }

    @Override
    public void update(User e) {
        String password = e.getPassword();
        e.setPassword(passwordEncoder.encode(password));
        super.update(e);
    }
}
