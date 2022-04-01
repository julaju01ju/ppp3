package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 29.11.2021
 */

@Service
public class UserDtoServiceImpl extends PageDtoServiceImpl<UserDto> implements UserDtoService {

    private UserDtoDao userDtoDao;
    private TagDtoDao tagDtoDao;

    @Autowired
    public UserDtoServiceImpl(UserDtoDao userDtoDao, TagDtoDao tagDtoDao) {
        this.userDtoDao = userDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id).map(dto -> {
            dto.setTopTags(tagDtoDao.getTop3UserTagsByReputation(id));
            return dto;
        });
    }

    @Override
    public List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id) {
        List<UserProfileQuestionDto> list = userDtoDao.getAllQuestionsByUserId(id);
//        for(UserProfileQuestionDto e : list) {
//            if(e.getListTagDto() == null) {
//                e.setListTagDto(tagDtoDao.getTagsByQuestionId(e.getQuestionId()));
//            }
//        }
        return list;
    }

}
