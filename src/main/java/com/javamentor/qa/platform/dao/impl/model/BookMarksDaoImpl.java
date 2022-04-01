package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookMarksDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.BookMarks;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class BookMarksDaoImpl extends ReadWriteDaoImpl<BookMarks, Long>implements BookMarksDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Optional<BookMarks> getBookmarkByUserEmailAndQuestionId(long userId, long questionId){
        String hql = "select BookMarks from BookMarks bookmark where bookmark.question.id = :questionId and bookmark.user.id = :userId";
        TypedQuery<BookMarks> query = entityManager.createQuery(hql, BookMarks.class).setParameter("userId", userId).setParameter("questionId", questionId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
