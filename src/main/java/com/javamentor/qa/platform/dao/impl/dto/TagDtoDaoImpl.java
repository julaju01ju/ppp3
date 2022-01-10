package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<TagDto> getIgnoredTags(Long userId) {
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagDto(it.id, it.ignoredTag.name, it.ignoredTag.description) " +
                        "from IgnoredTag it where it.user.id = :userId", TagDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
