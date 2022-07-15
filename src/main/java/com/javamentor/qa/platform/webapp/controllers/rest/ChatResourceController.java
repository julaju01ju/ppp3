package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.dto.CreateGroupChatDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.FindChatByStringDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.dto.SingleChatDtoServiceImpl;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    private final FindChatByStringDtoService findChatByStringDtoService;

    @Autowired
    public ChatResourceController(
            SingleChatDtoServiceImpl singleChatDtoService,
            GroupChatDtoService groupChatDtoService,
            SingleChatService singleChatService,
            MessageDtoService messageDtoService,
            FindChatByStringDtoService findChatByStringDtoService,
            UserService userService,
            GroupChatService groupChatService){
        this.singleChatDtoService = singleChatDtoService;
        this.groupChatDtoService = groupChatDtoService;
        this.singleChatService = singleChatService;
        this.messageDtoService = messageDtoService;
        this.findChatByStringDtoService = findChatByStringDtoService;
        this.userService = userService;
        this.groupChatService = groupChatService;
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
    @GetMapping
    @ApiOperation("Возвращает сообщения в Single и Group чатам по заданному параметру")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Выполнен поиск по заданной строке!"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр поиска!"),
            @ApiResponse(code = 500, message = "По данному запросу ничего не было найдено")
    })
    public ResponseEntity<List<ChatDto>> findStringInSingleAndGroupChats(
            @RequestParam(value = "findMessage") String findMessages) {
        if (findChatByStringDtoService.ifNotExistSearchedString(findMessages)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Чатов содержащих сообщение: "+ findMessages+" не было найдено!");
        }
        return new ResponseEntity<>(findChatByStringDtoService.getChatByString(findMessages), HttpStatus.OK);
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
}