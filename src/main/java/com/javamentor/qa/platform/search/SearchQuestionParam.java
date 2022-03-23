package com.javamentor.qa.platform.search;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class SearchQuestionParam {
    private SearchQuestionInputParam listWordsParam = new SearchQuestionInputParam();
    private SearchQuestionInputParam keyWordsParam = new SearchQuestionInputParam();

    public SearchQuestionParam() {
    }

    public Map<String, Object> getAllParam(String params) {
        List<String> listWords = listWordsParam.getListWords(params);
        Map<String, Object> keyWords = keyWordsParam.getKeyWords();
        SearchQuestionOutputParam searchQuestionOutputParam = new SearchQuestionOutputParam(listWords, keyWords);
        return searchQuestionOutputParam.getOutputParam();
    }
}