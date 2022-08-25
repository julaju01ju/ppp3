package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@Api("Message Api")
@RequestMapping("api/user/message")
public class MessageResourceController {


    private final MessageStarService messageStarService;

    @Autowired
    public MessageResourceController(MessageStarService messageStarService) {
        this.messageStarService = messageStarService;
    }

    @DeleteMapping("/star")
    @ApiOperation(value = "Удаление сообщения из избранных пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Сообщение удаленно из избранных"),
            @ApiResponse(code = 404, message = "Сообщение не найдено")
    })
    @ApiParam(name = "id", value = "message id", required = true)
    public ResponseEntity<?> deleteMessageStarById(@NotNull @RequestParam("id") Long id){

        Optional<MessageStar> messageStar = messageStarService.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (messageStar.isPresent() && messageStar.get().getUser().getId() == user.getId()){
            messageStarService.deleteById(id);
            return new ResponseEntity<>("Сообщение удаленно из избранных", HttpStatus.OK);
        }

        return new ResponseEntity<>("Сообщение не найдено", HttpStatus.NOT_FOUND);
    }



}
