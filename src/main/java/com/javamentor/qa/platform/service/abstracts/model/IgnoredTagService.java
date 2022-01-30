package com.javamentor.qa.platform.models.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;

public interface IgnoredTagService extends ReadWriteService<IgnoredTag, Long> {

    public Boolean getTagIfNotExist(Long tagId, Long userId);
}
