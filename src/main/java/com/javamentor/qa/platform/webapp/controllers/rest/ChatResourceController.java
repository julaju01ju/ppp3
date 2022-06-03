package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.service.abstracts.dto.SingleChatDtoService;
import com.javamentor.qa.platform.webapp.converters.SinglechatConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@Api("SingleChatDtosOfUser Api")
@RequestMapping("api/user/chat")
public class ChatResourceController {

    private SingleChatDtoService singleChatDtoService;
    private SinglechatConverter singlechatConverter;

    @Autowired
    public ChatResourceController(SingleChatDtoService singleChatDtoService, SinglechatConverter singlechatConverter){
        this.singleChatDtoService = singleChatDtoService;
        this.singlechatConverter = singlechatConverter;
    }

    @GetMapping("/single")
    @ApiOperation("Возращает ")
    public ResponseEntity<?> receiveAllSingleChatsOfUser(Principal principal){
        List<SingleChat> singleChats = singleChatDtoService.receiveSingleChatsByUsername(principal.getName());
        LinkedHashMap<SingleChat, Message> chatAndMessage = new LinkedHashMap<>();
        for(SingleChat singleChat: singleChats){
            chatAndMessage.put(singleChat, singleChatDtoService.receiveLastMessageBySingkeChatId(singleChat.getId()));
        }
        ArrayList<SingleChatDto> singleChatDtos = new ArrayList<>();
        chatAndMessage.forEach((chat, message) -> singleChatDtos
                .add(singlechatConverter.singleChatToSingleChatDto(chat, message))
        );

        return new ResponseEntity<>(singleChatDtos, HttpStatus.OK);
    }

}
