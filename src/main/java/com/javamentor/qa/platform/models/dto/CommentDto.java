package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Ali Veliev 10.12.2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    Long id;
    String  comment;
    Long userId;
    String fullName;
    Long reputation;
    LocalDateTime dateAdded;


}
