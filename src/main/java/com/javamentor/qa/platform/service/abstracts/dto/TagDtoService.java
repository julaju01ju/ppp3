package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagDtoPagination;

import java.util.List;

public interface TagDtoService extends PageDtoService<TagDtoPagination>{
    List<TagDto> getIgnoredTags(Long userId);
}
