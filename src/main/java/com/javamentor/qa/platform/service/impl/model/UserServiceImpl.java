package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
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

    @Override
    public void updatePasswordByEmail(String email, String password) {
        userDao.updatePasswordByEmail(email, password);
    }

    @Override
    public void disableUserByEmail(String email) {
        userDao.disableUserByEmail(email);
    }

    @Override
    public List<User> getAllByRole(Role role) { return userDao.getAllByRole(role); }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }
}
