package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface RelatedTagDtoDao {
    List<Tag> getAllChildTagsById(long id);
}
