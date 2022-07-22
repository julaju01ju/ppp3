package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.models.entity.chat.*;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.dto.SingleChatDtoServiceImpl;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import com.javamentor.qa.platform.webapp.converters.SingleChatConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@Api("Chats Api")
@RequestMapping("api/user/chat")
public class ChatResourceController {


    private final SingleChatDtoServiceImpl singleChatDtoService;
    private final SingleChatService singleChatService;
    private final GroupChatDtoService groupChatDtoService;
    private final MessageDtoService messageDtoService;
    private final UserService userService;
    private final GroupChatService groupChatService;
    private final MessageService messageService;
    private final SingleChatConverter singleChatConverter;

    @Autowired
    public ChatResourceController(SingleChatDtoServiceImpl singleChatDtoService, SingleChatService singleChatService, GroupChatDtoService groupChatDtoService, MessageDtoService messageDtoService, UserService userService, GroupChatService groupChatService, MessageService messageService, SingleChatConverter singleChatConverter) {
        this.singleChatDtoService = singleChatDtoService;
        this.singleChatService = singleChatService;
        this.groupChatDtoService = groupChatDtoService;
        this.messageDtoService = messageDtoService;
        this.userService = userService;
        this.groupChatService = groupChatService;
        this.messageService = messageService;
        this.singleChatConverter = singleChatConverter;
    }






    @GetMapping("/single")
    @ApiOperation("Возвращает SingleChatDtos авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все SingleChatDtos авторизованного пользователя"),
            @ApiResponse(code = 400, message = "Неправильные параметры запроса"),
    })
    public ResponseEntity<PageDto<SingleChatDto>> receiveAllSingleChatOfUser(
            @RequestParam("page") Integer page,
            @RequestParam("items") Integer items,
            @RequestParam(value = "sortAscendingFlag", required = false, defaultValue = "false") Boolean sortAscendingFlag)
    {

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("userId", userId);
        params.put("sortAscendingFlag", sortAscendingFlag);

        return new ResponseEntity<>(singleChatDtoService.getPageDto("paginationAllSingleChatsOfUser",
                params), HttpStatus.OK);
    }

    @GetMapping("/group")
    @ApiOperation("Возвращает все сообщения как объект класса GroupChatDto с учетом заданных параметров пагинации.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения с учетом заданных параметров пагинации."),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы."),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getGroupChatOutPutWithAllMessage(
            @RequestParam("page") Integer currentPage,
            @RequestParam(value = "items", defaultValue = "10") Integer items)
    {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPage);
        params.put("itemsOnPage", items);
        params.put("userId", userId);

        return new ResponseEntity<>(groupChatDtoService.getOptionalGroupChatDto(
                "paginationGroupChatMessages", params), HttpStatus.OK);
    }

    @GetMapping("/{id}/single/message")
    @ApiOperation("Возвращает все сообщения singleChat как объект класса PageDto<MessageDto> с учетом заданных параметров пагинации, " +
            "Сообщения сортируются по дате добавления: сначала самые новые.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения, отсортированные по дате добавление, сначала самые новые " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<MessageDto>> getAllMessageDtoInSingleChatSortedByPersistDate(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "sortAscendingFlag", required = false, defaultValue = "false") Boolean sortAscendingFlag,
            @PathVariable("id") Long chatId){

        if (!singleChatService.existsById(chatId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Чат с данным ID = " + chatId + ", не найден.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("chatId", chatId);
        params.put("sortAscendingFlag", sortAscendingFlag);

        return new ResponseEntity<>(messageDtoService.getPageDto(
                "paginationAllMessagesSortedByPersistDate", params), HttpStatus.OK);
    }

    @PostMapping("/group")
    @ApiOperation("Добавление нового группового чата. В RequestBody ожидает объект CreateGroupChatDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Групповой чат успешно добавлен"),
            @ApiResponse(code = 400, message = "Поле объекта CreateGroupChatDto " +
                    "userIds должно быть заполнено")
    })
    public ResponseEntity<?> addGroupChat(@Valid @RequestBody CreateGroupChatDto createGroupChatDto) {
        GroupChat groupChat = new GroupChat();
        Set<User> users = new HashSet<>();
        users.addAll(userService.getUsersByIds(createGroupChatDto.getUserIds()));
        groupChat.setUsers(users);
        Chat chat = new Chat(ChatType.GROUP);
        chat.setTitle(createGroupChatDto.getChatName());
        groupChat.setChat(chat);
        groupChatService.persist(groupChat);
        return new ResponseEntity<>("Групповой чат успешно добавлен", HttpStatus.OK);
    }

    @PostMapping(value = "/single")
    @ResponseBody
    @ApiOperation(value = "Добавление singleChat")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SingleChat добавлен"),
            @ApiResponse(code = 400, message = "SingleChat не был добавлен")
    })
    public ResponseEntity<?> addSingleChat(@Valid @RequestBody CreateSingleChatDto createSingleChatDto, @RequestParam(name = "message", required=false)  String message){
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        createSingleChatDto.setUserSenderId(user.getId());
        SingleChat singleChat = singleChatConverter.createSingleChatDtoToSingleChat(createSingleChatDto);
        singleChatService.persist(singleChat);
        messageService.persist(new Message(message, user, singleChat.getChat()));
        Map<String, String> maps = new HashMap<>();
        maps.put("message", message);
        maps.put("userSender", user.getId().toString());
        SingleChatDto singleChatDto = singleChatConverter.singleChatToSingleChatDto(singleChat);
        singleChatDto.setLastMessage(message);
        return new ResponseEntity<>("SingleChat чат успешно добавлен", HttpStatus.OK);
    }

}