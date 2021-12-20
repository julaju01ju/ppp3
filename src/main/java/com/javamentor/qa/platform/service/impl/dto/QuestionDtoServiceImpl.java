package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maksim Solovev 19.12.2021.
 */

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao) {
        this.questionDtoDao = questionDtoDao;
    }

    @Override
    @Transactional
    public Long getQuestionReputation(Long questionId) {
        return questionDtoDao.getQuestionReputation(questionId);
    }
}
