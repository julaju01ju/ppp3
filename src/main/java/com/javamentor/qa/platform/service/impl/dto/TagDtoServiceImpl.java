package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDtoPagination;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagDtoServiceImpl extends PageDtoServiceImpl<TagDtoPagination> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    @Autowired
    public TagDtoServiceImpl (TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

}
