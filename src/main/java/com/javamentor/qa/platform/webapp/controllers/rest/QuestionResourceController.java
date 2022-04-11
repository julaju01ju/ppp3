package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.models.entity.Comment;
import com.javamentor.qa.platform.models.entity.CommentType;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.service.abstracts.model.*;
import com.javamentor.qa.platform.webapp.converters.CommentConverter;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Ali Veliev 10.12.2021
 */

@RestController
@RequestMapping("/api/user/question")
@Api("Question Api")
public class QuestionResourceController {

    private TagService tagService;
    private QuestionDtoService questionDtoService;
    private QuestionConverter questionConverter;
    private TagConverter tagConverter;
    private ReputationService reputationService;
    private QuestionService questionService;
    private VoteOnQuestionService voteOnQuestionService;
    private QuestionViewedService questionViewedService;
    private BookMarksService bookMarksService;
    private CommentService commentService;
    private CommentConverter commentConverter;

    @Autowired
    public QuestionResourceController(TagService tagService, QuestionDtoService questionDtoService, ReputationService reputationService, QuestionService questionService, QuestionConverter questionConverter, TagConverter tagConverter, VoteOnQuestionService voteOnQuestionService, QuestionViewedService questionViewedService, BookMarksService bookMarksService, CommentService commentService, CommentConverter commentConverter) {
        this.tagService = tagService;
        this.questionDtoService = questionDtoService;
        this.reputationService = reputationService;
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.tagConverter = tagConverter;
        this.voteOnQuestionService = voteOnQuestionService;
        this.questionViewedService = questionViewedService;
        this.bookMarksService = bookMarksService;
        this.commentService = commentService;
        this.commentConverter = commentConverter;
    }

    @GetMapping("/sortedQuestions")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по голосам, ответам и просмотрам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по голосам, ответам и просмотрам"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getQuestionsSortedByVotesAndAnswersAndQuestionViewed(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByVoteAndAnswerAndQuestionView", params), HttpStatus.OK);
    }

    @PostMapping("/{questionId}/view")
    @ApiOperation("При переходе на вопрос c questionId=* авторизованного пользователя, вопрос добавляется в QuestionViewed")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Вопрос просмотрен впервые"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден"),
            @ApiResponse(code = 400, message = "Вопрос уже был просмотрен, либо формат введенного questionId является не верным")
    })
    public ResponseEntity<?> insertAuthUserToQuestionViewedByQuestionId(@PathVariable("questionId") Long questionId) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(questionId);

        if (!question.isPresent()) {
            return new ResponseEntity<>("Вопрос с id=" + questionId + " не найден", HttpStatus.NOT_FOUND);
        }
        if (!questionViewedService.isUserViewedQuestion(userPrincipal.getEmail(), question.get().getId())) {
            questionViewedService.persistQuestionViewed(new QuestionViewed(userPrincipal, question.get(), LocalDateTime.now()));
            return new ResponseEntity<>("Вопрос просмотрен впервые", HttpStatus.OK);
        }

        return new ResponseEntity<>("Вопрос уже был просмотрен", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    @ApiResponse(code = 200, message = "Получено количество вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {
        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    @ApiOperation("Возвращает вопрос как объект QuestionDto и тэги, относящиеся к этому вопросу по ИД вопроса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Показан вопрос с questionId=* и тэги, относящиеся к этому вопросу"),
            @ApiResponse(code = 400, message = "Формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> getQuestionById(@PathVariable("questionId") Long questionId) {

        Optional<QuestionDto> questionDto = questionDtoService.getQuestionById(questionId);
        return questionDto.isEmpty()
                ? new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation("Запись в БД голосования со значением UP за вопрос c questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Поднятие репутации вопроса с questionId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
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
        return new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("Запись в БД голосования со значением DOWN за вопрос c questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Понижение репутации вопроса с questionId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
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
        return new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    @ApiOperation("Создание нового вопроса от пользователя. В RequestBody ожидает объект QuestionCreateDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ваш вопрос успешно создан"),
            @ApiResponse(code = 400, message = "Объект QuestionCreateDto не передан в RequestBody. Поля объекта QuestionCreateDto title, " +
                    "description должны быть заполнены, в tags должен содержаться как минимум один объект класса TagDto")
    })
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
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с тэгами по ним с учетом заданных параметров пагинации.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров пагинациим"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getQuestions(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);


        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsWithGivenTags", params), HttpStatus.OK);
    }

    @GetMapping("/mostPopularWeek")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> за неделю с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по наибольшей популярности")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы за неделю с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по наибольшей популярности"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> mostPopularQuestionsWeek(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsMostPopularWeek", params), HttpStatus.OK);
    }

    @GetMapping("/noAnswer")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto>, по которым не было ответа " +
            "с тэгами с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы, по которым не было ответа с тэгами " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getQuestionsNoAnswer(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsNoAnswer", params), HttpStatus.OK);
    }

    @GetMapping("/new")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с учетом заданных параметров пагинации, " +
            "Вопросы сотртируются по дате добавление: сначала самые новые.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами, отсортированные по дате добавление, сначала самые новые " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getAllQuestionDtoSortedByPersistDate(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsWithTagsSortedByPersistDate", params), HttpStatus.OK);

    }

    @GetMapping("/sortedQuestionsByMonth")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> за месяц с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по голосам, ответам и просмотрам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы за месяц с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по голосам, ответам и просмотрам"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getQuestionsSortedByVotesAndAnswersAndViewsByMonth(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно переданы тэги в списки trackedTag или ignoredTag");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByVoteAndAnswerAndViewsByMonth", params), HttpStatus.OK);
    }

    @PostMapping("/{id}/bookmark")
    @ApiOperation("При переходе на вопрос c questionId=*, вопрос добавляется в BookMarks авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Вопрос успешно добавлен в закладки"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден"),
            @ApiResponse(code = 400, message = "Вопрос уже был добавлен, либо формат введенного questionId является не верным")
    })
    public ResponseEntity<?> insertQuestionToBookmarksByQuestionId(@PathVariable("id") Long questionId) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(questionId);

        if (question.isEmpty()) {
            return new ResponseEntity<>("Вопрос с id=" + questionId + " не найден", HttpStatus.NOT_FOUND);
        }

        if (bookMarksService.isQuestionAlreadyExistOnUserBookmarks(userPrincipal.getId(), question.get().getId())) {
            return new ResponseEntity<>("Вопрос уже был добавлен в закладки", HttpStatus.BAD_REQUEST);
        }

        BookMarks bookMark = new BookMarks();
        bookMark.setUser(userPrincipal);
        bookMark.setQuestion(question.get());
        bookMarksService.persist(bookMark);
        return new ResponseEntity<>("Вопрос успешно добавлен в закладки", HttpStatus.OK);
    }

    @PostMapping("/{id}/comment")
    @ApiOperation("Добавление комментария в вопрос")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Комментарий успешно добавлен в вопрос")
    })
    public ResponseEntity<?> addCommentByQuestionId(@PathVariable("id") Long id, @Valid @RequestBody String text) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Comment comment = new Comment();
        comment.setText(text);
        comment.setCommentType(CommentType.QUESTION);
        comment.setUser(sender);

        commentService.persist(comment);

        return new ResponseEntity<>(commentConverter.commentToCommentDto(comment), HttpStatus.OK);
    }
}