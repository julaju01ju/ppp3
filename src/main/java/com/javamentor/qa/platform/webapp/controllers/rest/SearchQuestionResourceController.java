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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

@Api("Rest Controller for Search Question")
public class SearchQuestionResourceController {


    private final SearchQuestion searchQuestion;
    private final SearchQuestionParam searchQuestionParam;

    public SearchQuestionResourceController(SearchQuestion searchQuestion, SearchQuestionParam searchQuestionParam) {
        this.searchQuestion = searchQuestion;
        this.searchQuestionParam = searchQuestionParam;
    }

    @GetMapping("/api/search")
    @ApiOperation("Поиск вопросов")
    public ResponseEntity<List<QuestionViewDto>> getQuestion(@RequestParam(value = "request") String request) {

        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("request", request);
        Map<String, Object> param = searchQuestionParam.getAllParam(inputParams);
        return new ResponseEntity<>(searchQuestion.getItems(param), HttpStatus.OK);
    }
}

@Component
class SearchQuestion {
    @PersistenceContext
    private EntityManager entityManager;

    public List<QuestionViewDto> getItems(Map<String, Object> inputParams) {
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
//                                "WHERE CASE " +
//                                "   WHEN -1 IN :ignoredTag AND -1 IN :trackedTag THEN TRUE " +
//                                "   WHEN -1 IN :ignoredTag THEN qht.tag_id IN :trackedTag " +
//                                "   WHEN -1 IN :trackedTag THEN q.id NOT IN " +
//                                "   (" +
//                                "       SELECT q_ign.id FROM question q_ign " +
//                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
//                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
//                                "   ) " +
//                                "   ELSE qht.tag_id IN :trackedTag AND q.id NOT IN " +
//                                "   (" +
//                                "       SELECT q_ign.id FROM question q_ign " +
//                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
//                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
//                                "   ) " +
//                                "   END " +
                                " where q.title like concat ('%', :title, '%')" +
                                " and q.description like concat ('%', :body, '%')" +
                                " and u.full_name like concat ('%', :userName, '%')" +

                                " and (q.title like concat ('%', :request, '%')"+
                                " or q.description like concat ('%', :request, '%'))"+
                                "ORDER BY q.id ")

                .setParameter("title", inputParams.get("title"))
                .setParameter("body", inputParams.get("body"))
                .setParameter("request", inputParams.get("request"))
                .setParameter("userName",inputParams.get("user"))
//                .setParameter("tag", inputParams.get("tag"))
//                .setParameter("fullMatch", inputParams.get("fullMatch"))
                .getResultList();
    }
}

@Component
class SearchQuestionOutputParam {
    private final List<String> listWords;
    private final Map<String, Object> keyWords;
    private Map<String, Object> outputParam = new HashMap<>();
    private Matcher matcher;
    private Pattern pattern;

    public SearchQuestionOutputParam(List<String> listWord, Map<String, Object> keyWords) {
        this.listWords = listWord;
        this.keyWords = keyWords;
    }

    public void keyWordCheck() {
        for (String keyWord : keyWords.keySet()){
            pattern = Pattern.compile((String) keyWords.get(keyWord));
            for (Iterator<String> listWordIterator = listWords.listIterator();
                 listWordIterator.hasNext();) {
                String nextListWord = listWordIterator.next();
                matcher = pattern.matcher(nextListWord);
                if (matcher.find()) {
                    if(((String) keyWords.get(keyWord)).contains("*")) {
                        outputParam.put(keyWord, nextListWord.substring(1, nextListWord.length()-1));
                    } else {
                        outputParam.put(keyWord, nextListWord.substring(matcher.end()));
                    }
                    listWordIterator.remove();
                }
            }
        }

        for (String word : listWords) {
            outputParam.merge("request", word, (a,b) -> a + " " + b);
        }

    }

    public Map<String, Object> getOutputParam(){
        return outputParam;
    }
}

@Component
class SearchQuestionInputParam {

    private Map<String, Object> keyWords = new HashMap<>();
    private List<String> listWords = new ArrayList<>();

    public SearchQuestionInputParam() {
    }

    public Map<String, Object> getKeyWords() {
        keyWords.put("user", "User:");
        keyWords.put("body", "Body:");
        keyWords.put("title", "Title:");
        keyWords.put("tag", "^[\\[](.*)[\\]]$");
        keyWords.put("fullMatch", "^[\"](.*)[\"]$");
        return keyWords;
    }

    public List<String> getListWords(Map<String, String> params) {
        for (String oneWord : params.get("request").split("\\s")) {
            listWords.add(oneWord);
        }
        return listWords;
    }
}

@Component
class SearchQuestionParam {

    private SearchQuestionInputParam listWordsParam = new SearchQuestionInputParam();
    private SearchQuestionInputParam keyWordsParam = new SearchQuestionInputParam();

    public SearchQuestionParam() {
    }

    public Map<String, Object> getAllParam(Map<String, String> params) {
        List<String> listWords = listWordsParam.getListWords(params);
        Map<String, Object> keyWords = keyWordsParam.getKeyWords();
        SearchQuestionOutputParam searchQuestionOutputParam = new SearchQuestionOutputParam(listWords, keyWords);
        searchQuestionOutputParam.keyWordCheck();
        return searchQuestionOutputParam.getOutputParam();
    }
}