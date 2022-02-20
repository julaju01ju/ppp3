package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author Ali Veliev 10.12.2021
 */

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Map<Long,QuestionDto> questionDtoMap = new TreeMap<>();
    private Map<Long,TagDto> tagDtoMap = new TreeMap<>();
    private Map<Long,CommentDto> commentDtoMap = new TreeMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {

        Optional<QuestionDto> questionDto = SingleResultUtil.getSingleResultOrNull(entityManager.createNativeQuery(
                        "select q.id , q.title, q.description,  q.last_redaction_date,  q.persist_date,  u.id as u_id,  u.full_name,  u.image_link, " +
                                "(select sum(r.count) from reputation r where r.author_id = u.id) as reputation, " +
                                "(select count(up.vote) from votes_on_questions up where up.vote = 'UP_VOTE' and up.question_id = q.id) - (select count(down.vote) from votes_on_questions down where down.vote = 'DOWN_VOTE' and down.question_id = q.id) as votes, " +
                                "(select count(a.id) from answer a where a.question_id = q.id) as answers, " +
                                "(select u.full_name from user_entity u where u.id = c.user_id) as uc_full_name, " +
                                "(select sum(r.count) from reputation r where r.sender_id = c.user_id) as reputation_u_c, " +
                                "c.id as com_id, c.text as com_text, c.user_id as com_user_id, c.persist_date as com_persist_date,  " +
                                "t.id as t_id, t.name as t_name, t.description as t_desc " +
                                "from question q join user_entity u on u.id = q.user_id " +
                                "join comment_question cq on q.id = cq.question_id " +
                                "join comment c on cq.comment_id = c.id " +
                                "join question_has_tag qht on q.id = qht.question_id " +
                                "join tag t on qht.tag_id = t.id " +
                                "where q.id =:id")
                .setParameter("id", id)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                                          @Override
                                          public Object transformTuple(Object[] tuple, String[] aliases) {

                                              TagDto tagDto = new TagDto();
                                              tagDto.setId(((BigInteger) tuple[17]).longValue());
                                              tagDto.setName((String) tuple[18]);
                                              tagDto.setDescription((String) tuple[19]);
                                              if (!tagDtoMap.containsKey(tagDto.getId())){
                                                  tagDtoMap.put(tagDto.getId(), tagDto);
                                              }

                                              CommentDto commentDto = new CommentDto();
                                              commentDto.setFullName((String) tuple[11]);
                                              commentDto.setReputation(((BigInteger) tuple[12]).longValue());
                                              commentDto.setId(((BigInteger) tuple[13]).longValue());
                                              commentDto.setComment((String) tuple[14]);
                                              commentDto.setUserId(((BigInteger) tuple[15]).longValue());
                                              commentDto.setDateAdded(((Timestamp) tuple[16]).toLocalDateTime());
                                              if (!commentDtoMap.containsKey(commentDto.getId())){
                                                  commentDtoMap.put(commentDto.getId(), commentDto);
                                              }

                                              QuestionDto questionDto = new QuestionDto();
                                              questionDto.setId(((BigInteger) tuple[0]).longValue());
                                              questionDto.setTitle((String) tuple[1]);
                                              questionDto.setDescription((String) tuple[2]);
                                              questionDto.setLastUpdateDateTime(((Timestamp) tuple[3]).toLocalDateTime());
                                              questionDto.setPersistDateTime(((Timestamp) tuple[4]).toLocalDateTime());
                                              questionDto.setAuthorId(((BigInteger) tuple[5]).longValue());
                                              questionDto.setAuthorName((String) tuple[6]);
                                              questionDto.setAuthorImage((String) tuple[7]);
                                              questionDto.setAuthorReputation(((BigInteger) tuple[8]).longValue());
                                              questionDto.setCountValuable(((BigInteger) tuple[9]).intValue());
                                              questionDto.setCountAnswer(((BigInteger) tuple[10]).intValue());
                                              questionDto.setViewCount(0);
                                              if (!questionDtoMap.containsKey(id)){
                                                  questionDtoMap.put(questionDto.getId(), questionDto);
                                              }
//
                                              return questionDtoMap.get(id);
                                          }

                                          @Override
                                          public List transformList(List list) {

                                              QuestionDto questionDto = questionDtoMap.get(id);

                                              List<TagDto> tagDtoList = new ArrayList<>(tagDtoMap.values());
                                              List<CommentDto> commentDtoList = new ArrayList<>(commentDtoMap.values());

                                              questionDto.setListTagDto(tagDtoList);
                                              questionDto.setListCommentDto(commentDtoList);

                                              for (int i = list.size() - 1; i != 0; i--) {
                                                  list.remove(i);
                                              }

                                              return list;
                                          }
                                      }
                ));
        return questionDto;
    }
}
