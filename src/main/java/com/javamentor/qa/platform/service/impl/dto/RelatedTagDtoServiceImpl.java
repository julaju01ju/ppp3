package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagDtoDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatedTagDtoServiceImpl implements RelatedTagDtoService {
    private final RelatedTagDtoDao relatedTagDtoDao;

    @Autowired
    public RelatedTagDtoServiceImpl(RelatedTagDtoDao relatedTagDtoDao) {
        this.relatedTagDtoDao = relatedTagDtoDao;
    }

    @Override
    public List<Tag> getAllChildTagsById(long id) {
        return relatedTagDtoDao.getAllChildTagsById(id);
    }
}
