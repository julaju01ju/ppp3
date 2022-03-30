package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserProfileQuestionsDtoDao;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class UserProfileQuestionsDtoDaoImpl implements UserProfileQuestionsDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

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
