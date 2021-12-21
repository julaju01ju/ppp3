package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService extends ReadWriteService<Tag, Long> {
    Optional<Tag> getTagByName(String name);

    Tag checkExistsTagAndCreateTagIfNotExistOrFindTag(Tag tag);

    List<Tag> getListTagForCreateQuestion(List<Tag> listTag);
}
