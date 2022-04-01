package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserDto (" +
                                "user.id, " +
                                "user.email, " +
                                "user.fullName, " +
                                "user.imageLink, " +
                                "user.city, " +
                                "(select coalesce (max(rep.count),0) from Reputation rep where rep.author.id =: id) " +
                                ") " +
                                "from User user where user.id =: id "
                        , UserDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }
    @Override
    public List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id) {
        final String query =
                "select q.id, " +
                        "q.title, " +
                        "(select count(ans.id) from Answer as ans where ans.question.id = q.id), " +
                        "q.persistDateTime " +
                        "from Question q where q.user.id = :id";


        return (List<UserProfileQuestionDto>) entityManager.createQuery(query)
                .setParameter("id", id)
                .unwrap(Query.class)
                .setResultTransformer(
                        new ResultTransformer() {
                            @Override
                            public Object transformTuple(Object[] tuple, String[] aliases) {
                                UserProfileQuestionDto userProfileQuestionDto =new UserProfileQuestionDto();
                                userProfileQuestionDto.setQuestionId((Long) tuple[0]);
                                userProfileQuestionDto.setTitle((String) tuple[1]);
                                userProfileQuestionDto.setCountAnswer((Long) tuple[2]);
                                userProfileQuestionDto.setPersistDate((LocalDateTime) tuple[3]);

                                return userProfileQuestionDto;
                            }

                            @Override
                            public List transformList(List list) {
                                return list;
                            }
                        }
                ).getResultList();
    }
}
