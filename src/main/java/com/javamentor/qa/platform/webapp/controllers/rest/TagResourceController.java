package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/tag")
@Api("CRUD operations with Tags")
public class TagResourceController {

    private final TrackedTagDtoService trackedTagDtoService;
    private final TagDtoService tagDtoService;
    private final TagService tagService;
    private final TagConverter tagConverter;
    private final TrackedTagService trackedTagService;
    private final IgnoredTagService ignoredTagService;


    @Autowired
    public TagResourceController(TrackedTagDtoService trackedTagDtoService,
                                 TagDtoService tagDtoService,
                                 TagService tagService,
                                 TagConverter tagConverter,
                                 TrackedTagService trackedTagService,
                                 IgnoredTagService ignoredTagService) {
        this.trackedTagDtoService = trackedTagDtoService;
        this.tagDtoService = tagDtoService;
        this.tagService = tagService;
        this.tagConverter = tagConverter;
        this.trackedTagService = trackedTagService;
        this.ignoredTagService = ignoredTagService;
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

    @ApiOperation(value = "Get all authorized user's ignored tags")
    @ApiResponses(value =
        @ApiResponse(code = 200, message = "Get all ignored tags"))
    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTags() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<TagDto> tagDtos = tagDtoService.getIgnoredTags(userId);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Add Tag into TrackedTag table")
    @ApiResponses(value = {
    @ApiResponse(code = 404, message = "Tag not found")})
    @PostMapping("/{id}/tracked")
    public ResponseEntity<?> addTrackedTag(@PathVariable("id") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if(optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (!(trackedTagService.getTagIfNotExist(tagId, user.getId()))) {
                TrackedTag trackedTag = new TrackedTag();
                trackedTag.setTrackedTag(tag);
                trackedTag.setUser(user);
                trackedTagService.persist(trackedTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(
            value = "Add Tag into IgnoredTag table")
    @ApiResponses(value = {
    @ApiResponse(code = 404, message = "Tag not found")})
    @PostMapping("/{id}/ignored")
    public ResponseEntity<?> addIgnoredTag(@PathVariable("id") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if(optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (!(ignoredTagService.getTagIfNotExist(tagId, user.getId()))) {
                IgnoredTag ignoredTag = new IgnoredTag();
                ignoredTag.setIgnoredTag(tag);
                ignoredTag.setUser(user);
                ignoredTagService.persist(ignoredTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
        }
    }
}
