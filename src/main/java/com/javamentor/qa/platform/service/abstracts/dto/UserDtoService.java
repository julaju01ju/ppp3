package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 01.12.2021
 */

public interface UserDtoService extends PageDtoService<UserDto> {
    Optional<UserDto> getUserById(Long id);
    List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id);
    List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id);
    List<UserDto> getTop10UserDtoForAnswer();
}
