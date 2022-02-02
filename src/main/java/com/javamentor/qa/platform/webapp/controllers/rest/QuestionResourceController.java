package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.service.abstracts.model.*;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Ali Veliev 10.12.2021
 */

@RestController
@RequestMapping("/api/user/question")
@Api("Rest Controller for Question")
public class QuestionResourceController {

    private TagService tagService;
    private QuestionDtoService questionDtoService;
    private QuestionConverter questionConverter;
    private TagConverter tagConverter;
    private ReputationService reputationService;
    private QuestionService questionService;
    private VoteOnQuestionService voteOnQuestionService;
    private QuestionViewedService questionViewedService;

    @Autowired
    public QuestionResourceController(TagService tagService, QuestionDtoService questionDtoService, ReputationService reputationService, QuestionService questionService, QuestionConverter questionConverter, TagConverter tagConverter, VoteOnQuestionService voteOnQuestionService, QuestionViewedService questionViewedService) {
        this.tagService = tagService;
        this.questionDtoService = questionDtoService;
        this.reputationService = reputationService;
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
        this.voteOnQuestionService = voteOnQuestionService;
        this.questionViewedService = questionViewedService;
    }

    @PostMapping("/{id}/view")
    @ApiOperation("Добавление авторизованного пользователя в QuestionViewed, при переходе на вопрос")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Вопрос просмотрен впервые"),
            @ApiResponse(code = 404, message = "Вопрос с id не найден"),
            @ApiResponse(code = 400, message = "Вопрос уже был просмотрен")
    })
    public ResponseEntity<?> insertAuthUserToQuestionViewedByQuestionId(@PathVariable("id") Long id) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(id);

        if (!question.isPresent()) {
            return new ResponseEntity<>("Вопрос с id " + id + " не найден", HttpStatus.NOT_FOUND);
        }

        if (!questionViewedService.getListOfUsersIdFromQuestionViewedByQuestionIdCache(id).contains(userPrincipal.getId())) {
            questionViewedService.persist(new QuestionViewed(null, userPrincipal, question.get(), LocalDateTime.now()));
            questionViewedService.refreshCache(id);
            return new ResponseEntity<>("Вопрос просмотрен впервые", HttpStatus.OK);
        }

        return new ResponseEntity<>("Вопрос уже был просмотрен", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {
        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Возвращает вопрос и тэги относящиеся к этому вопросу, по ИД вопроса.")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") Long id) {

        Optional<QuestionDto> questionDto = questionDtoService.getQuestionById(id);
        return questionDto.isEmpty()
                ? new ResponseEntity<>("Wrong Question ID!", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation("запись в БД голосования за вопрос со значением UP")
    public ResponseEntity<?> insertUpVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                VoteQuestion upVoteQuestion = new VoteQuestion(sender, question, VoteType.UP_VOTE);
                voteOnQuestionService.persist(upVoteQuestion);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Такого question не существует", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("запись в БД голосования за вопрос со значением DOWN")
    public ResponseEntity<?> insertDownVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                VoteQuestion downVoteQuestion = new VoteQuestion(sender, question, VoteType.DOWN_VOTE);
                voteOnQuestionService.persist(downVoteQuestion);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Такого question не существует", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    @ApiOperation("API создания вопроса. Получает объект QuestionCreateDto. " +
            "Возвращает объект QuestionDto. Поля Title, Description, Tag должны быть заполнены." +
            "Если хотя бы одно поле не заполнено возвращается HttpStatus.BAD_REQUEST." +
            "Проверяет есть ли присланный Tag в базе. Если нет - создает.")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setUser((User) authentication.getPrincipal());
        question.setDescription(questionCreateDto.getDescription());
        question.setTags(tagConverter.listTagDtoToListTag(questionCreateDto.getTags()));

        questionService.persist(question);

        return new ResponseEntity<>(questionConverter.questionToQuestionDto(question), HttpStatus.OK);
    }


    @GetMapping()
    @ApiOperation("Получение пагинации QuestionDto с тэгами. " +
            "В качестве параметров принимает page, items, список trackedTag и ignoredTag" +
            "page - обязательный параметр" +
            "items - не обязательный на фронте, по умолчанию на бэк 10" +
            "trackedTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых есть хотя бы один из переданных тэгов" +
            "ignoredTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых нет данных тэгов.")
    public ResponseEntity<PageDto<QuestionDto>> getQuestions(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);


        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsWithGivenTags", params), HttpStatus.OK);
    }

    @GetMapping("/noAnswer")
    @ApiOperation("Получение пагинации QuestionDto, где не на один вопрос не был дан ответ с тэгами. " +
            "В качестве параметров принимает page, items, список trackedTag и ignoredTag" +
            "page - обязательный параметр" +
            "items - не обязательный на фронте, по умолчанию на бэк 10" +
            "trackedTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых есть хотя бы один из переданных тэгов" +
            "ignoredTag - не обязательный параметр, если что-то передали, то отдаются те вопросы," +
            " в которых нет данных тэгов.")
    public ResponseEntity<PageDto<QuestionDto>> getQuestionsNoAnswer(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsNoAnswer", params), HttpStatus.OK);
    }

    @GetMapping("/new")
    @ApiOperation("Получение всех QuestionDto с тэгами, отсортированное по дате добавление, сначала самые новые. " +
            "В качестве параметров принимает page, items, список trackedTag и ignoredTag " +
            "page - обязателен параметр " +
            "items - не обязательный на фронте, по умолчанию на бэк 10 " +
            "trackedTag - не обязательный параметр, " +
            "если что-то передали то мы должны отдавать те вопросы в которых есть хотя бы один из переданных тэгов " +
            "ignoredTag - не обязательный параметр, " +
            "если что-то передали то мы должны отдавать те вопросы в которых нету данных тэгов.")
    public ResponseEntity<PageDto<QuestionDto>> getAllQuestionDtoSortedByPersistDate(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsWithTagsSortedByPersistDate", params), HttpStatus.OK);

    }
}