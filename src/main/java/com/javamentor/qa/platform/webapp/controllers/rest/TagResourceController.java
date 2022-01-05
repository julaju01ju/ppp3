package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/tag")
@Api("CRUD operations with Tags")
public class TagResourceController {

    private final TrackedTagDtoService trackedTagDtoService;


    @Autowired
    public TagResourceController(TrackedTagDtoService trackedTagDtoService) {
        this.trackedTagDtoService = trackedTagDtoService;
    }


    @ApiOperation(value = "Get all authorized user's tracked tags")
    @ApiResponses(value =
    @ApiResponse(code = 200, message = "Get all tracked tags"))
    @GetMapping( "/tracked")
    public ResponseEntity<List<TagDto>> getAllTrackedTags() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        List<TagDto> tagDtos = trackedTagDtoService.getTrackedTags(userId);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

}
