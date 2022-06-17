package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "q.id, " +
                                "q.title, " +
                                "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                                "q.persistDateTime)" +

                                "from Question q where q.user.id = :id",

                        UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }


    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "q.id, " +
                                "q.title, " +
                                "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                                "q.persistDateTime)" +
                                "from Question q where q.user.id = :id and q.isDeleted = true", UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }


    @Override
    public List<UserDto> getTop10UserDtoForAnswer() {
        String queryS =
                "select u.id,\n" +
                        "        (select count(id) from answer as a where a.persist_date >= NOW() - INTERVAL '7 DAY' and a.user_id = u.id) as Row1,\n" +
                        "\n" +
                        "\n" +
                        "       ((select count(voa.user_id) from votes_on_answers as voa where voa.user_id = u.id and voa.answer_id = answer_id and voa.vote = 'UP_VOTE') -\n" +
                        "        (select count(voa.user_id) from votes_on_answers as voa where voa.user_id = u.id and voa.answer_id = answer_id and voa.vote = 'DOWN_VOTE')) as Row2\n" +
                        "\n" +
                        "\n" +
                        "from user_entity u\n" +
                        "where u.is_deleted = false\n" +
                        "ORDER BY Row1 desc, Row2 desc\n" +
                        "LIMIT 10;\n";
        List list = entityManager.createNativeQuery(
                queryS
        ).getResultList();

        List<UserDto> resList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            long id = ((BigInteger) ((Object[]) list.get(i))[0]).longValue();

            UserDto userDto = getUserById(id).get();
            resList.add(userDto);
        }

        return resList;
    }


}
