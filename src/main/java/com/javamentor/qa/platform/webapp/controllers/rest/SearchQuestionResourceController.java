package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

@Api("Rest Controller for Search Question")
public class SearchQuestionResourceController {


    private final GetQuestion getQuestion;
    private final GetParam getParam;

    public SearchQuestionResourceController(GetQuestion getQuestion, GetParam getParam) {
        this.getQuestion = getQuestion;
        this.getParam = getParam;
    }

    @GetMapping("/api/search")
    @ApiOperation("Поиск вопросов")
    public ResponseEntity<List<QuestionViewDto>> getQuestion(@RequestParam(value = "request") String request) {

        Map<String, String> params = new HashMap<>();
        params.put("request", request);
        Map<String, Object> par = getParam.getParam(params);
        List<QuestionViewDto> allUser = getQuestion.getItems(par);
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }
}
@Component
class GetQuestion {
    @PersistenceContext
    private EntityManager entityManager;

    public List<QuestionViewDto> getItems(Map<String, Object> resultParams) {
        return entityManager.createNativeQuery(
                        "SELECT " +
                                "distinct q.id AS q_id, " +
                                "q.title, " +
                                "q.description, " +
                                "q.last_redaction_date, " +
                                "q.persist_date, " +
                                "u.id , " +
                                "u.full_name, " +
                                "u.image_link, " +
                                "(SELECT coalesce(sum(r.count),0) FROM reputation r " +
                                "   WHERE r.author_id = u.id) AS reputation, " +
                                "(SELECT coalesce(count(up.vote), 0) FROM votes_on_questions up " +
                                "   WHERE up.vote = 'UP_VOTE' AND up.question_id = q.id) " +
                                "- " +
                                "(SELECT coalesce(count(down.vote), 0) FROM votes_on_questions down " +
                                "   WHERE down.vote = 'DOWN_VOTE' AND down.question_id = q.id) AS votes, " +
                                "(SELECT coalesce(count(a.id),0) FROM answer a " +
                                "   WHERE a.question_id = q.id) AS answers " +
                                "FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "where q.title like concat('%', :title, '%')" +
                                "or q.description like concat('%', :body, '%')" +
//                                "or (u.id like :userId and q.title like concat('%', :requestUser, '%'))" +
                                "ORDER BY q.id")
//                .setParameter("request", resultParams.get("request"))
                .setParameter("title", resultParams.get("Title:"))
                .setParameter("body", resultParams.get("Body:"))
//                .setParameter("userId", resultParams.get("User:"))
//                .setParameter("requestUser", resultParams.get("requestUser"))
                .getResultList();
    }
}
@Component
class GetParam{
    public Map<String, Object> getParam(Map<String, String> params){
        String input = params.get("request");
        //надо делать split не через пробел
        //очень дыряво, не считывает последующие слова
        String[] allString = input.split("\\s");
        List<String> patt = new ArrayList<>();
        patt.add("Title:");
        patt.add("Body:");
        patt.add("User:");

        Map<String, Object> param = new HashMap<>();

        for (String oneMatch : patt){
            for(String singleInput : allString) {
                Pattern pattern = Pattern.compile(oneMatch);
                Matcher matcher = pattern.matcher(singleInput);
                if (matcher.find()) {
                    param.put(oneMatch, matcher.replaceAll(""));
                }
                //все равно много if начинает появляться
                //добавляет ПОСЛЕДНЕЕ слово
                //
                if (param.containsKey("User:")){
                    param.put("requestUser", singleInput);
                }
            }
        }
        if (param.isEmpty()){
            param.put("request", input);
        }
        return param;
    }
}

