package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionTagDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.QuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionTagServiceImpl extends ReadWriteServiceImpl<Question, Tag> implements QuestionTagService {

    private QuestionTagDao questionTagDao;

    @Autowired
    public QuestionTagServiceImpl(QuestionTagDao questionTagDao) {
        super(questionTagDao);
    }
}
