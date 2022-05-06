package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagDtoDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RelatedTagDtoDaoImpl extends ReadWriteDaoImpl<RelatedTag,Long> implements RelatedTagDtoDao {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public List<Tag> getAllChildTagsById(long id) {
        Query query = entityManager.createNativeQuery("SELECT child_tag FROM related_tag WHERE main_tag = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
}
