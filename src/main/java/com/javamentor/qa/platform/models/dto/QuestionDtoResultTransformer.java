package com.javamentor.qa.platform.models.dto;

import org.hibernate.transform.ResultTransformer;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionDtoResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 8909545788077515255L;
    private final transient Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

        Long questionId = ((Number) tuple[0]).longValue();

        return questionDtoMap.computeIfAbsent(
                questionId,
                v -> {
                    QuestionDto questionDtoTemp = new QuestionDto();
                    questionDtoTemp.setId(((BigInteger) tuple[0]).longValue());
                    questionDtoTemp.setTitle((String) tuple[1]);
                    questionDtoTemp.setDescription((String) tuple[2]);
                    questionDtoTemp.setLastUpdateDateTime(((Timestamp) tuple[3]).toLocalDateTime());
                    questionDtoTemp.setPersistDateTime(((Timestamp) tuple[4]).toLocalDateTime());
                    questionDtoTemp.setAuthorId(((BigInteger) tuple[5]).longValue());
                    questionDtoTemp.setAuthorName((String) tuple[6]);
                    questionDtoTemp.setAuthorImage((String) tuple[7]);
                    questionDtoTemp.setAuthorReputation(((BigInteger) tuple[8]).longValue());
                    questionDtoTemp.setCountValuable(((BigInteger) tuple[9]).intValue());
                    questionDtoTemp.setCountAnswer(((BigInteger) tuple[10]).intValue());
                    questionDtoTemp.setViewCount(0);
                    questionDtoTemp.setListTagDto(new ArrayList<>());
                    return questionDtoTemp;
                }
        );
    }

    @Override
    public List<QuestionDto> transformList(List list) {
        return new ArrayList<>(questionDtoMap.values());
    }
}