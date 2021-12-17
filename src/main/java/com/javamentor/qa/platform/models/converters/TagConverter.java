package com.javamentor.qa.platform.models.converters;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TagConverter {

    public abstract TagDto tagToTagDto(Tag tag);

    public abstract Tag tagDtoToTag(TagDto tagDto);
}
