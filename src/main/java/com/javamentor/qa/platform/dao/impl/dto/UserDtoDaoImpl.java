package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        String queryQ = "select u.id, u.email, u.full_name, u.image_link, u.city," +
                "                (SELECT COALESCE(SUM(reputation.count),0) FROM reputation WHERE reputation.author_id = u.id) as Row0," +
                "        (select count(id) from answer as a where a.persist_date >= NOW() - INTERVAL '7 DAY' and a.user_id = u.id) as Row1," +
                "        ((select count(voa.user_id) from votes_on_answers as voa where voa.user_id = u.id and voa.vote = 'UP_VOTE') -" +
                "                (select count(voa.user_id) from votes_on_answers as voa where voa.user_id = u.id and voa.vote = 'DOWN_VOTE')) as Row2" +
                "        from user_entity as u" +
                "        where u.is_deleted = false" +
                "        ORDER BY Row1 desc, Row2 desc" +
                "        LIMIT 10;";

        List list = entityManager.createNativeQuery(queryQ).getResultList();

        List<UserDto> resList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Object[] o = (Object[]) list.get(i);

            UserDto userDto = new UserDto(((BigInteger) (o[0])).longValue(),
                    (String) o[1],
                    (String) o[2],
                    (String) o[3],
                    (String) o[4],
                    ((BigInteger) o[5]).intValue());

            resList.add(userDto);
        }

        return resList;
    }


}
