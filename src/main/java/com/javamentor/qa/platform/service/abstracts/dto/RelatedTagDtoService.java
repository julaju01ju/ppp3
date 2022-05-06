package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface RelatedTagDtoService {
    List<Tag> getAllChildTagsById(long id);
}
