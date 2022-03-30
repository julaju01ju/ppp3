package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionsDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserProfileQuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileQuestionDtoServiceImpl implements UserProfileQuestionDtoService {

    private TagDtoDao tagDtoDao;
    private UserProfileQuestionsDtoDao userProfileQuestionsDtoDao;

    @Autowired
    public UserProfileQuestionDtoServiceImpl(UserProfileQuestionsDtoDao userProfileQuestionsDtoDao,
                                             TagDtoDao tagDtoDao) {
        this.userProfileQuestionsDtoDao = userProfileQuestionsDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id) {
        List<UserProfileQuestionDto> list = userProfileQuestionsDtoDao.getAllQuestionsByUserId(id);
        for(UserProfileQuestionDto e : list) {
            if(e.getListTagDto() == null) {
                e.setListTagDto(tagDtoDao.getTagsByQuestionId(e.getQuestionId()));
            }
        }
        return list;
    }
}
