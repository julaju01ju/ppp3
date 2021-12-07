package com.javamentor.qa.platform.security.service.impl;

import com.javamentor.qa.platform.dao.abstracts.model.UserDAO;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;

    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.getUserByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), user.get().getAuthorities());
    }
}
