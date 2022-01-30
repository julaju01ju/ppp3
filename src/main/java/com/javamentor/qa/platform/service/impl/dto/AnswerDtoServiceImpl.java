package com.javamentor.qa.platform.models.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private AnswerDtoDao answerDto;

    @Autowired
    public AnswerDtoServiceImpl(AnswerDtoDao answerDto){
        this.answerDto = answerDto;
    }

    public AnswerDtoServiceImpl() {

    }

    @Override
    public List<AnswerDto> getAllAnswersByQuestionId(Long id) {
        return answerDto.getAllByQuestionId(id);
    }

    @Override
    public void deleteAnswerByAnswerId(Long id) {
        answerDto.deleteAnswerByAnswerId(id);
    }

}
