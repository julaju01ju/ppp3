package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagViewDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @GetMapping("/tracked")
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

    @GetMapping("/name")
    @ApiOperation("API получение всех тегов, отсортированных по имени, с пагинацией. " +
            "Принимает параметры: page(обязательный) - текущая страница и " +
            "items(необязательный) - количество элементов на страницу. По умолчанию равен 10.")
    public ResponseEntity<PageDto<TagViewDto>> getAllTagsOrderByNamePagination(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "items", required = false,
                    defaultValue = "10") Integer items,
            @RequestParam(value = "filter", required = false,
                    defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("tagsFilter", filter);

        PageDto<TagViewDto> pageDto = tagDtoService.getPageDto("paginationAllTagsSortedByName", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Add Tag into TrackedTag table")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Tag not found")})
    @PostMapping("/{id}/tracked")
    public ResponseEntity<?> addTrackedTag(@PathVariable("id") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (tagService.checkedAndIgnoredContainTag(tagId, user.getId())) {
                TrackedTag trackedTag = new TrackedTag();
                trackedTag.setTrackedTag(tag);
                trackedTag.setUser(user);
                trackedTagService.persist(trackedTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(
            value = "Add Tag into IgnoredTag table")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Tag not found")})
    @PostMapping("/{id}/ignored")
    public ResponseEntity<?> addIgnoredTag(@PathVariable("id") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (tagService.checkedAndIgnoredContainTag(tagId, user.getId())) {
                IgnoredTag ignoredTag = new IgnoredTag();
                ignoredTag.setIgnoredTag(tag);
                ignoredTag.setUser(user);
                ignoredTagService.persist(ignoredTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/latter")
    @ApiOperation("API поиск тегов по букве или слову. " +
            "Выдается 10 самых популярных тегов для текущего поиска. " +
            "В качестве параметров передается searchString - строка или буква.")
    public ResponseEntity<List<TagDto>> getTop10FoundTags(@RequestParam(value = "searchString") String searchString) {
        List<TagDto> tagDtos = tagDtoService.getTop10FoundTags(searchString);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    @GetMapping("/popular")
    @ApiOperation("API получение всех тегов, с пагинацией по популярности (популярность - количество вопросов за тегом)" +
            "Принимает параметры: page(обязательный) - текущая страница и " +
            "items(необязательный) - количество элементов на страницу. По умолчанию равен 10.")
    public ResponseEntity<PageDto<TagViewDto>> getAllTagsOrderByPopularPagination(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "items", required = false,
                    defaultValue = "10") Integer items,
            @RequestParam(value = "filter", required = false,
                    defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("tagsFilter", filter);

        PageDto<TagViewDto> pageDto = tagDtoService.getPageDto("paginationAllTagsSortedByPopular", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Delete Tag from TrackedTag table by tag id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "TrackedTag deleted successfully"),
            @ApiResponse(code = 400, message = "TrackedTag not found")})
    @DeleteMapping("/{id}/tracked")
    public ResponseEntity<?> deleteTrackedTagByTagId(@PathVariable("id") Long tagId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!trackedTagService.getTagIfNotExist(tagId, user.getId())) {
            return ResponseEntity.badRequest().body("Error deleting TrackeTag by tag id" + tagId);
        }

        trackedTagService.deleteTrackedTagByTagIdAndUserId(tagId, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "Delete Tag from IgnoredTag table by tag id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "IgnoredTag deleted successfully"),
            @ApiResponse(code = 400, message = "IgnoredTag not found")})
    @DeleteMapping("/{id}/ignored")
    public ResponseEntity<?> deleteIgnoredTagByTagId(@PathVariable("id") Long tagId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!ignoredTagService.getTagIfNotExist(tagId, user.getId())) {
            return ResponseEntity.badRequest().body("Error deleting IgnoredTag by tag id" + tagId);
        }

        ignoredTagService.deleteIgnoredTagByTagIdAndUserId(tagId, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
