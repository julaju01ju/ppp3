package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ali Veliev 10.12.2021
 */

@Service
public class QuestionDtoServiceImpl extends PageDtoServiceImpl<QuestionViewDto>  implements QuestionDtoService {

    @Autowired
    private QuestionDtoDao questionDtoDao;

    @Autowired
    private TagDtoDao tagDtoDao;

    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {
        return questionDtoDao.getQuestionById(id);
    }

    @Override
    public PageDto<QuestionViewDto> getPageQuestionsWithTags(String pageDtoDaoName, Map<String, Object> params) {

        PageDto<QuestionViewDto> pageDto = super.getPageDto(pageDtoDaoName, params);
        List ids = pageDto.getItems().stream().map(
                questionViewDto -> questionViewDto.getId()).collect(Collectors.toList());
        Map<Long, List<TagDto>> tags = tagDtoDao.getTagsByQuestionIds(ids);
        for (QuestionViewDto q : pageDto.getItems()) {
            q.setListTagDto(tags.get(q.getId()));
        }
        return pageDto;
    }
}
