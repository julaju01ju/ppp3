package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed,Long> implements QuestionViewedDao {

}
