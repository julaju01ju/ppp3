package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

/**
 * @author Ali Veliev 01.12.2021
 */

public interface UserDtoService {
    UserDto getUserById(Long id);
}
