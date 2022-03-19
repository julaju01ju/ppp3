package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Ali Veliev 29.11.2021
 */

@Service
public class UserDtoServiceImpl extends PageDtoServiceImpl<UserDto> implements UserDtoService {

    @Autowired
    private UserDtoDao userDtoDao;
    @Autowired
    private TagDtoDao tagDtoDao;

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id).map(dto -> {
            dto.setTopTags(tagDtoDao.getTop3UserTagsByReputation(id));
            return dto;
        });
    }

}
