package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ReadWriteServiceImpl<User,Long>{
    public UserService(ReadWriteDao<User, Long> readWriteDao) {
        super(readWriteDao);
    }
}
