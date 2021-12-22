package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionTagDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;

import org.springframework.stereotype.Repository;

@Repository
public class QuestionTagDaoImpl extends ReadWriteDaoImpl<Question, Tag> implements QuestionTagDao { //?
}
