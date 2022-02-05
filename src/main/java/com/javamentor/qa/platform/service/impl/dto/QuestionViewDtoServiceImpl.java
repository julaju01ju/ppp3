package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionViewDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ali Veliev 10.12.2021
 */

@Service
public class QuestionViewDtoServiceImpl extends PageDtoServiceImpl<QuestionViewDto> implements QuestionViewDtoService {

    @Autowired
    private TagDtoDao tagDtoDao;

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
