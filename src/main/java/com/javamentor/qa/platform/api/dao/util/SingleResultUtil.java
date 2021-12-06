package com.javamentor.qa.platform.api.dao.util;

import javax.persistence.TypedQuery;
import java.util.Optional;

public class SingleResultUtil {
    public static <T> Optional<T> getSingleResultOrNull(TypedQuery<T> var) {
        try {
            return Optional.of(var.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
