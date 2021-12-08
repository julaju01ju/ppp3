package com.javamentor.qa.platform.models.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class PageDtp<T, P> {

    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;

    public PageDtp(int itemsOnPage, int currentPageNumber, P param) {
        if (itemsOnPage > 0) {
            this.itemsOnPage = itemsOnPage;
        } else {
            throw new IllegalArgumentException("Неверно выбрано количество записей на страницу");
        }
        this.currentPageNumber = currentPageNumber;
        totalResultCount = getTotalResultCount(param);
        totalPageCount = totalResultCount <= itemsOnPage ? 1
                : (totalResultCount % itemsOnPage) == 0 ? totalResultCount / itemsOnPage
                : (totalResultCount / itemsOnPage) + 1;
        if (currentPageNumber > totalPageCount) {
            throw new IllegalArgumentException("Страницы под номером "
                    + currentPageNumber + " пока не существует");
        }
    }

    public abstract List<T> getItems(P param);

    public abstract int getTotalResultCount(P param);
}