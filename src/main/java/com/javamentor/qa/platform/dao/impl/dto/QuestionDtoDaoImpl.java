package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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

        Optional<QuestionDto> questionDto = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select q.id, " +
                                "q.title, " +
                                "q.description,  " +
                                "q.lastUpdateDateTime,  " +
                                "q.persistDateTime,  " +
                                "u.id,  " +
                                "u.fullName,  " +
                                "u.imageLink, " +
                                "coalesce((select sum(r.count) from Reputation r where r.author.id = u.id), 0), " +
                                "coalesce((select sum(case v.vote  when 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id=q.id), 0), " +
                                "(select count(qv.id) from QuestionViewed qv where qv.question.id = q.id), " +
                                "(select count(a.id) from Answer a where a.question.id = q.id), " +
                                "(select quv.vote from VoteQuestion quv where quv.question.id = q.id and quv.user.id =:userId), " +
                                "coalesce((select sum(r.count) from Reputation r where r.author.id = cq.comment.user.id), 0), " +
                                "cq.id, " +
                                "cq.comment.text, " +
                                "cq.comment.user.id, " +
                                "cq.comment.persistDateTime,  " +
                                "cq.comment.user.fullName, " +
                                "t.id, " +
                                "t.name, " +
                                "t.description " +
                                "from Question q " +
                                "join q.user u " +
                                "join q.tags t " +
                                "join q.commentQuestions cq  " +
                                "where q.id =:id")
                .setParameter("id", id)
                .setParameter("userId", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                                          @Override
                                          public Object transformTuple(Object[] tuple, String[] aliases) {

                                              TagDto tagDto = new TagDto();
                                              tagDto.setId(((Long) tuple[19]));
                                              tagDto.setName((String) tuple[20]);
                                              tagDto.setDescription((String) tuple[21]);
                                              if (!tagDtoMap.containsKey(tagDto.getId())){
                                                  tagDtoMap.put(tagDto.getId(), tagDto);
                                              }

                                              CommentDto commentDto = new CommentDto();
                                              commentDto.setReputation(((Long) tuple[13]));
                                              commentDto.setId(((Long) tuple[14]));
                                              commentDto.setComment((String) tuple[15]);
                                              commentDto.setUserId(((Long) tuple[16]));
                                              commentDto.setDateAdded(((LocalDateTime) tuple[17]));
                                              commentDto.setFullName((String) tuple[18]);
                                              if (!commentDtoMap.containsKey(commentDto.getId())){
                                                  commentDtoMap.put(commentDto.getId(), commentDto);
                                              }

                                              QuestionDto questionDto = new QuestionDto();
                                              questionDto.setId(((Long) tuple[0]));
                                              questionDto.setTitle((String) tuple[1]);
                                              questionDto.setDescription((String) tuple[2]);
                                              questionDto.setLastUpdateDateTime(((LocalDateTime) tuple[3]));
                                              questionDto.setPersistDateTime(((LocalDateTime) tuple[4]));
                                              questionDto.setAuthorId(((Long) tuple[5]));
                                              questionDto.setAuthorName((String) tuple[6]);
                                              questionDto.setAuthorImage((String) tuple[7]);
                                              questionDto.setAuthorReputation(((Long) tuple[8]));
                                              questionDto.setCountValuable(((Long) tuple[9]).intValue());
                                              questionDto.setViewCount(((Long) tuple[10]).intValue());
                                              questionDto.setCountAnswer(((Long) tuple[11]).intValue());
                                              questionDto.setIsUserVote((Enum<VoteType>) tuple[12]);

                                              if (!questionDtoMap.containsKey(id)){
                                                  questionDtoMap.put(questionDto.getId(), questionDto);
                                              }

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