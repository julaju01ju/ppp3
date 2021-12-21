package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.converters.TagConverter;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {
    private final TagDao tagDao;
    private final TagConverter tagConverter;

    public TagServiceImpl(TagDao tagDao, TagConverter tagConverter) {
        super(tagDao);
        this.tagDao = tagDao;
        this.tagConverter = tagConverter;
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        return tagDao.getTagByName(name);
    }

    @Override
    public List<Tag> getListTagForCreateQuestion(List<Tag> listTag) {

        List<Tag> listTagForQuestion = new ArrayList<>();
        for (Tag tag : listTag) {
            listTagForQuestion.add(checkExistsTagAndCreateTagIfNotExistOrFindTag(tag));
        }
        return listTagForQuestion;
    }

    @Override
    public Tag checkExistsTagAndCreateTagIfNotExistOrFindTag(Tag tag) {
        Optional<Tag> tagChecked = getTagByName(tag.getName());
        if (tagChecked.isEmpty()) {
            persist(tag);
        } else {
            tag = tagDao.findTag(tagChecked.get().getId());
        }
        return tag;
    }
}
