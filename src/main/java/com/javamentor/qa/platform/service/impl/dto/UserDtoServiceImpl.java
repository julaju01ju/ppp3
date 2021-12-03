package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.impl.dto.UserDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;

/**
 * @author Ali Veliev 29.11.2021
 */

@Service
public class UserDtoServiceImpl implements UserDtoService {

   private final UserDtoDaoImpl userDtoDao;

    public UserDtoServiceImpl(UserDtoDaoImpl userDtoDao) {
        this.userDtoDao = userDtoDao;
    }

    public UserDto getUserById(Long id){
        return userDtoDao.getUserById(id);
    }

}
