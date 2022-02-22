package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select q.id, " +
                                "q.title, " +
                                "u.id, " +
                                "u.fullName, " +
                                "u.imageLink," +
                                "q.description, " +
                                "q.persistDateTime, " +
                                "q.lastUpdateDateTime, " +
                                "coalesce((select sum(r.count) from Reputation r where r.author.id = u.id), 0), " +
                                "coalesce((select sum(case v.vote  when 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id=q.id), 0), " +
                                "(select count(qv.id) from QuestionViewed qv where qv.question.id = q.id), " +
                                "(select count(a.id) from Answer a where a.question.id = q.id), " +
                                "t.id, " +
                                "t.name, " +
                                "t.description " +
                                "from Question q " +
                                "join q.user u " +
                                "join q.tags t " +
                                "where q.id =:id ")
                .setParameter("id", id)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuple, String[] aliases) {
                        QuestionDto questionDto = new QuestionDto();
                        TagDto tagDto = new TagDto();
                        tagDto.setId((Long) tuple[12]);
                        tagDto.setName((String) tuple[13]);
                        tagDto.setDescription((String) tuple[14]);
                        List<TagDto> tagDtoList = new ArrayList<>();
                        tagDtoList.add(tagDto);
                        questionDto.setId((Long) tuple[0]);
                        questionDto.setTitle((String) tuple[1]);
                        questionDto.setAuthorId((Long) tuple[2]);
                        questionDto.setAuthorName((String) tuple[3]);
                        questionDto.setAuthorImage((String) tuple[4]);
                        questionDto.setDescription((String) tuple[5]);
                        questionDto.setPersistDateTime((LocalDateTime) tuple[6]);
                        questionDto.setLastUpdateDateTime((LocalDateTime) tuple[7]);
                        questionDto.setAuthorReputation((Long) tuple[8]);
                        questionDto.setCountValuable(((Long) tuple[9]).intValue());
                        questionDto.setViewCount(((Long) tuple[10]).intValue());
                        questionDto.setCountAnswer(((Long) tuple[11]).intValue());
                        questionDto.setListTagDto(tagDtoList);
                        return questionDto;
                    }

                    @Override
                    public List transformList(List list) {

                        List<TagDto> tagDtoList = new ArrayList<>();

                        for (Object a : list) {
                            tagDtoList.add(((QuestionDto) a).getListTagDto().get(0));
                        }

                        QuestionDto questionDto = (QuestionDto) list.get(0);
                        questionDto.setListTagDto(tagDtoList);
                        for (int i = list.size() - 1; i != 0; i--) {
                            list.remove(i);
                        }
                        return list;
                    }
                }));
    }
}
