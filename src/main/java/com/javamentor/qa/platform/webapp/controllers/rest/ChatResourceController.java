package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api("Chats Api")
@RequestMapping("api/user/chat")
public class ChatResourceController {


    private final SingleChatDtoServiceImpl singleChatDtoService;
    private final SingleChatService singleChatService;
    private final MessageDtoService messageDtoService;

    @Autowired
    public ChatResourceController(
            SingleChatDtoServiceImpl singleChatDtoService,
            SingleChatService singleChatService,
            MessageDtoService messageDtoService){
        this.singleChatDtoService = singleChatDtoService;
        this.singleChatService = singleChatService;
        this.messageDtoService = messageDtoService;
    }


    @GetMapping("/single")
    @ApiOperation("Возращает SingleChatDtos авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все SingleChatDtos авторизованного пользователя"),
            @ApiResponse(code = 400, message = "Неправильные параметры запроса"),
    })
    public ResponseEntity<PageDto<SingleChatDto>> receiveAllSingleChatOfUser(
            @RequestParam("page") Integer page,
            @RequestParam("items") Integer items) {

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("userId", userId);

        return new ResponseEntity<>(singleChatDtoService.getPageDto("paginationAllSingleChatsOfUser",
                params), HttpStatus.OK);
    }

    @GetMapping("/{id}/single/message")
    @ApiOperation("Возращает все сообщения singleChat как объект класса PageDto<MessageDto> с учетом заданных параметров пагинации, " +
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
            @PathVariable("id") Long chatId) {

        if (!singleChatService.existsById(chatId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Чат с данным ID = " + chatId + ", не найден.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("chatId", chatId);

        return new ResponseEntity<>(messageDtoService.getPageDto(
                "paginationAllMessagesSortedByPersistDate", params), HttpStatus.OK);
    }
}