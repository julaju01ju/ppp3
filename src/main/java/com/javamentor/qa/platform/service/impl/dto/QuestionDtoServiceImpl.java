package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    @Autowired
    private QuestionDtoDao questionDtoDao;

    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {
        return questionDtoDao.getQuestionById(id);
    }
}
