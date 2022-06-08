package com.javamentor.qa.platform.models.dto;

import org.hibernate.transform.ResultTransformer;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessageViewDtoResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 8909545788077515255L;
    private final transient Map<Long, MessageViewDto> messageViewDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Long messageId = ((Number) tuple[0]).longValue();

        return messageViewDtoMap.computeIfAbsent(
                messageId,
                v -> {
                    MessageViewDto messageViewDtoTemp = new MessageViewDto();
                    messageViewDtoTemp.setId(((BigInteger) tuple[3]).longValue());
                    messageViewDtoTemp.setMessage((String) tuple[4]);
                    messageViewDtoTemp.setNickName((String) tuple[1]);
                    messageViewDtoTemp.setUserId(((BigInteger) tuple[0]).longValue());
                    messageViewDtoTemp.setImage((String) tuple[2]);
                    messageViewDtoTemp.setPersistDateTime(((Timestamp) tuple[5]).toLocalDateTime());

                    return messageViewDtoTemp;
                }
        );
    }

    @Override
    public List<MessageViewDto> transformList(List list) {
        return new ArrayList<>(messageViewDtoMap.values());
    }
}
