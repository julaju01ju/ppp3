package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 29.11.2021
 */

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserById(Long id) {

        List<TagDto> topTags = entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.TagDto(" +
                                "t.id, " +
                                "t.name, " +
                                "t.description) " +
                                "from Tag t join t.questions tq join tq.answers qa where qa.user.id =: id group by t.id order by count(t.id)"
                        , TagDto.class)
                .setParameter("id", id)
                .setMaxResults(3)
                .getResultList();
        UserDto userDto = entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserDto (" +
                                "rep.id, " +
                                "rep.author.email, " +
                                "rep.author.fullName, " +
                                "rep.author.imageLink, " +
                                "rep.author.city, " +
                                "rep.count) " +
                                "from Reputation rep where rep.author.id =: id "
                        , UserDto.class)
                .setParameter("id", id)
                .getSingleResult();

        return Optional.of(new UserDto(userDto.getId(), userDto.getEmail(), userDto.getFullName(), userDto.getLinkImage(), userDto.getCity(), userDto.getReputation(), topTags));
    }
}
