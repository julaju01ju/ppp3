package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        super(tagDao);
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getListTagForCreateQuestionAndCreateTagIfNotExist(List<String> listTagName) {

        List<Tag> listTagForQuestion = new ArrayList<>();
        List<Tag> listAllTags = tagDao.getAll();

        Map<String, Tag> mapTag = new HashMap<>();
        for (Tag allTag : listAllTags) {
            mapTag.put(allTag.getName(), allTag);
        }

        for (String tagName : listTagName) {
            if (mapTag.containsKey(tagName)) {
                listTagForQuestion.add(mapTag.get(tagName));
            } else {
                Tag tag = new Tag();
                tag.setName(tagName);
                persist(tag);
                listTagForQuestion.add(tag);
            }
        }
        return listTagForQuestion;
    }
}
