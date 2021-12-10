package com.javamentor.qa.platform.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {

    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;

//    public PageDto(int itemsOnPage, int currentPageNumber, P param) {
//        if (itemsOnPage > 0) {
//            this.itemsOnPage = itemsOnPage;
//        } else {
//            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
//        }
//        this.currentPageNumber = currentPageNumber;
//        totalResultCount = getTotalResultCount(param);
//        totalPageCount =(int) Math.ceil(totalResultCount / itemsOnPage);
//        if (currentPageNumber > totalPageCount) {
//            throw new IllegalArgumentException("Страницы под номером "
//                    + currentPageNumber + " пока не существует");
//        }
//    }

}