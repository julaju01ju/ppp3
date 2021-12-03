package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends ReadWriteServiceImpl<Role,Long>{

    public RoleService(ReadWriteDao<Role, Long> readWriteDao) {
        super(readWriteDao);
    }
}
