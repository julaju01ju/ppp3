package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Tag> tagsThatExistsInDatabase = tagDao.getListTagsByListOfTagName(listTagName);

        Map<String, Tag> mapTagsThatExistsInDatabase = tagsThatExistsInDatabase.stream()
                .collect(Collectors.toMap(tag -> tag.getName(), tag -> tag));

        for (String tagName : listTagName) {
            if (mapTagsThatExistsInDatabase.containsKey(tagName)) {
                listTagForQuestion.add(mapTagsThatExistsInDatabase.get(tagName));
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
