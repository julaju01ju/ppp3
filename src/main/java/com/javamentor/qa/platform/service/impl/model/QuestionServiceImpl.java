package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final QuestionDao questionDao;
    private final TagDao tagDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao, TagDao tagDao) {
        super(questionDao);
        this.questionDao = questionDao;
        this.tagDao = tagDao;
    }

    @Override
    public Long getQuestionCount() {
        return questionDao.getQuestionCount();
    }
}
