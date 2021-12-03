package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.UserDto;

/**
 * @author Ali Veliev 01.12.2021
 */

public interface UserDtoDao {
    UserDto getUserById(Long id);
}
