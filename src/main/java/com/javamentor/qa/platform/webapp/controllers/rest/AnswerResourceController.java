package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api("Answer Api")
@RequestMapping("api/user/question")
public class AnswerResourceController {

    private final AnswerDtoService answerDtoService;

    @Autowired
    public AnswerResourceController(AnswerDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }


    @GetMapping("/{questionId}/answer")
    @ApiOperation(
            value = "Returns List of AnswerDtos corresponding questionId",
            notes = "Returns HTTP 404 if the questionId is not found")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid List of AnswerDtos found"),
            @ApiResponse(code = 404, message = "Answers with id not found")})
    public ResponseEntity<?> getAllAnswerByQuestionId(@PathVariable("questionId") Long id) {
        List<AnswerDto> answerDtos = answerDtoService.getAllAnswersByQuestionId(id);

        return answerDtos.isEmpty() ?
                new ResponseEntity<>("Answers with id " + id + " not found!", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(answerDtos, HttpStatus.OK);
    }


    @DeleteMapping("/{questionId}/answer/{answerId}")
    @ApiOperation(value = "Удаление ответа answerId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid  Answer ID found"),
            @ApiResponse(code = 404, message = "Answer ID with id not found"),
            @ApiResponse(code = 400, message = "Invalid Answer ID entry")})
    public ResponseEntity<?> deleteAnswerById(@ApiParam(name = "answerId") @PathVariable Long answerId) {
        if (answerDtoService.getById(answerId) == null) {
            return new ResponseEntity("Answer Id " + answerId + " not found!", HttpStatus.NOT_FOUND);
        } else if (answerId != null) {
            answerDtoService.deleteAnswerByAnswerId(answerId);
            return ResponseEntity.ok().build();
        } else
        return ResponseEntity.badRequest().body("Error deleting an answer Id: " + answerId);
    }
}
