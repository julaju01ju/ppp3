package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;

import java.time.LocalDateTime;


public class AnswerDto {
    private Long id;
    private Long userId;
    private Long userReputation;
    private Long questionId;
    private String body;
    private LocalDateTime persistDate;
    private Boolean isHelpful;
    private LocalDateTime dateAccept;
    private Long countValuable;
    private String image;
    private String nickName;

    public AnswerDto(){}

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getUserReputation() {
        return userReputation;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getPersistDate() {
        return persistDate;
    }

    public Boolean getHelpful() {
        return isHelpful;
    }

    public LocalDateTime getDateAccept() {
        return dateAccept;
    }

    public Long getCountValuable() {
        return countValuable;
    }

    public String getImage() {
        return image;
    }

    public String getNickName() {
        return nickName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserReputation(Long userReputation) {
        this.userReputation = userReputation;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPersistDate(LocalDateTime persistDate) {
        this.persistDate = persistDate;
    }

    public void setHelpful(Boolean helpful) {
        isHelpful = helpful;
    }

    public void setDateAccept(LocalDateTime dateAccept) {
        this.dateAccept = dateAccept;
    }

    public void setCountValuable(Long countValuable) {
        this.countValuable = countValuable;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public static AnswerDto toDto(Answer answer){
        return new AnswerDto(){{
            setId(answer.getId());
            setUserId(answer.getUser().getId());
            setUserReputation(answer.getUser().getReputationCount());
            setQuestionId(answer.getQuestion().getId());
            setBody(answer.getHtmlBody());
            setPersistDate(answer.getPersistDateTime());
            setHelpful(answer.getIsHelpful());
            setDateAccept(answer.getDateAcceptTime());
            setCountValuable((long) answer.getVoteAnswers().size());
            setImage(answer.getUser().getImageLink());
        }};
    }
}
