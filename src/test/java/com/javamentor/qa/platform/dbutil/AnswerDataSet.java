package com.javamentor.qa.platform.dbutil;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;

public class AnswerDataSet
        implements DataSetProvider {
    @Override
    public IDataSet provide() throws DataSetException {
        return new DataSetBuilder()
                .table("answer")
                .columns("id", "persist_date", "update_date", "question_id", "user_id", "htmlBody", "is_helpful",
                        "is_deleted", "is_deleted_by_moderator", "date_accept_time")
                .values(1, "2020-01-01", "2020-02-02", 1, 1, "body", true, false, false, "2020-01-01")
                .build();
    }
}
