package com.javamentor.qa.platform.dao.abstracts.dto;

import java.util.List;

public interface PageDtoDao <T, P>{

    public abstract List<T> getItems(P param);

    public abstract int getTotalResultCount(P param);
}
