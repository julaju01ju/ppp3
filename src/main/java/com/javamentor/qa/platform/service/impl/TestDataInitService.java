package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.dao.abstracts.model.RelatedTagDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.models.entity.chat.*;
import com.javamentor.qa.platform.models.entity.question.*;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestDataInitService {

    private RoleService roleService;

    private UserService userService;

    private AnswerService answerService;

    private QuestionService questionService;

    private ReputationService reputationService;

    private TagService tagService;

    private TrackedTagService trackedTagService;

    private IgnoredTagService ignoredTagService;

    private VoteOnAnswerService voteOnAnswerService;

    private VoteOnQuestionService voteOnQuestionService;

    private BookMarksService bookMarksService;

    private RelatedTagService relatedTagService;

    private GroupChatService groupChatService;

    private SingleChatService singleChatService;

    private MessageService messageService;
    public TestDataInitService() {
    }

    @Autowired
    public TestDataInitService(
            @Lazy RoleService roleService,
            @Lazy UserService userService,
            @Lazy AnswerService answerService,
            @Lazy QuestionService questionService,
            @Lazy ReputationService reputationService,
            @Lazy TagService tagService,
            @Lazy TrackedTagService trackedTagService,
            @Lazy IgnoredTagService ignoredTagService,
            @Lazy VoteOnAnswerService voteOnAnswerService,
            @Lazy VoteOnQuestionService voteOnQuestionService,
            @Lazy BookMarksService bookMarksService,
            @Lazy RelatedTagService relatedTagService,
            @Lazy GroupChatService groupChatService,
            @Lazy SingleChatService singleChatService,
            @Lazy MessageService messageService) {
        this.roleService = roleService;
        this.userService = userService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.reputationService = reputationService;
        this.tagService = tagService;
        this.trackedTagService = trackedTagService;
        this.ignoredTagService = ignoredTagService;
        this.voteOnAnswerService = voteOnAnswerService;
        this.voteOnQuestionService = voteOnQuestionService;
        this.bookMarksService = bookMarksService;
        this.relatedTagService = relatedTagService;
        this.groupChatService = groupChatService;
        this.singleChatService = singleChatService;
        this.messageService = messageService;
    }

    public void createRole() {
        Role role = new Role("ROLE_USER");
        roleService.persist(role);
    }

    public void createUser(int count) {
        for (int i = 0; i <= count; i++) {
            User user = new User();
            user.setEmail((i + "@mail.com"));
            user.setPassword(("pass" + i));
            user.setFullName(("fullName" + i));
            user.setPersistDateTime(LocalDateTime.of(2021, 12, 3, 0, 0));
            user.setIsEnabled(true);
            user.setIsDeleted(false);
            user.setCity(("City" + i));
            user.setLinkSite(("linkSite" + i));
            user.setLinkGitHub(("linkGitHub" + i));
            user.setLinkVk(("linkVk" + i));
            user.setAbout(("About" + i));
            user.setImageLink(("imageLink" + i));
            user.setLastUpdateDateTime(LocalDateTime.of(2021, 12, 4, 0, 0));
            user.setNickname(("nickName" + i));
            user.setRole(roleService.getAll().get(0));
            userService.persist(user);
        }
    }

    public void createTag(int count) {
        for (int i = 1; i <= count; i++) {
            Tag tag = new Tag();
            tag.setDescription("Tag Description " + i);
            tag.setName("Tag " + i);
            tag.setPersistDateTime(LocalDateTime.of(2021, 10, 01, 14, 05, 00));
            tagService.persist(tag);
        }
    }

    public void createQuestion(int count) {
        //Question c 1 Tag`ом
        Question question1 = new Question();
        question1.setDescription("Question Description 0");
        question1.setIsDeleted(false);
        question1.setLastUpdateDateTime(LocalDateTime.of(2021, 10, 01, 18, 05, 00));
        question1.setTitle("Question Title 0");
        List<Tag> tagList1 = new ArrayList<>();
        tagList1.add(tagService.getAll().get(0));
        question1.setTags(tagList1);
        question1.setUser(userService.getAll().get(1));
        questionService.persist(question1);

        //Question c 4 Tag`ами
        Question question2 = new Question();
        question2.setDescription("Question Description 1");
        question2.setIsDeleted(false);
        question2.setLastUpdateDateTime(LocalDateTime.of(2021, 10, 01, 18, 05, 00));
        question2.setTitle("Question Title 1");
        List<Tag> tagList2 = new ArrayList<>();
        for (int k = 0; k < 4; k++) {
            tagList2.add(tagService.getAll().get(k));
        }
        question2.setTags(tagList2);
        question2.setUser(userService.getAll().get(1));
        questionService.persist(question2);

        //Question c рандомным кол-вом Tag`в
        for (int i = 2; i <= count; i++) {
            int random = (int) (Math.random() * 5);
            Question question3 = new Question();
            question3.setDescription("Question Description " + i);
            question3.setIsDeleted(false);
            question3.setLastUpdateDateTime(LocalDateTime.of(2021, 10, 01, 18, 05, 00));
            question3.setTitle("Question Title " + i);

            Set<Tag> tagSet = new HashSet<>();
            for (int j = 0; j <= random; j++) {

                tagSet.add(tagService.getAll().get((int) (Math.random() * 4)));
            }
            List<Tag> tagList = new ArrayList<>(tagSet);

            question3.setTags(tagList);
            question3.setUser(userService.getAll().get(i));
            questionService.persist(question3);
        }
    }


    public void createAnswer() {
        List<Question> questionList = questionService.getAll();

        // У Question'a c id=1 нет answer
        for (int i = 1; i < questionList.size(); i++) {
            int random = (int) (Math.random() * 7);

            for (int j = 1; j <= random; j++) {
                Answer answer = new Answer();
                answer.setDateAcceptTime(LocalDateTime.of(2021, 12, 01, 14, 05, 00));
                answer.setHtmlBody("Answer Body " + j);
                answer.setIsDeleted(false);
                answer.setIsDeletedByModerator(false);
                answer.setIsHelpful(true);
                answer.setPersistDateTime(LocalDateTime.of(2021, 12, 01, 14, 05, 00));
                answer.setUpdateDateTime(LocalDateTime.of(2021, 12, 01, 14, 05, 00));
                answer.setUser(userService.getAll().get((int) (Math.random() * 50)));
                answer.setQuestion(questionList.get(i));
                answerService.persist(answer);
            }
        }
    }

    public void createReputation() {
        List<User> userList = userService.getAll();

        questionService.getAll().stream()
                .filter(question -> (long) LocalDateTime.now().getNano() % question.getId() == 0)
                .map(question -> {
                    VoteQuestion voteQuestion = new VoteQuestion();
                    Collections.shuffle(userList);
                    voteQuestion.setLocalDateTime(LocalDateTime.of(2021, 12, 01, 14, 05, 00));
                    voteQuestion.setQuestion(question);
                    voteQuestion.setUser(
                            userList.stream()
                                    .filter(user -> !user.getId().equals(question.getUser().getId()))
                                    .collect(Collectors.toList()).get(0)
                    );
                    int random = (int) (Math.random() * 3);
                    if (random == 1) {
                        voteQuestion.setVote(VoteType.UP_VOTE);
                    } else {
                        voteQuestion.setVote(VoteType.DOWN_VOTE);
                    }
                    return voteQuestion;
                }).forEach(voteOnQuestionService::persist);

        answerService.getAll().stream()
                .filter(answer -> (long) LocalDateTime.now().getNano() % answer.getId() == 0)
                .map(answer -> {
                    VoteAnswer voteAnswer = new VoteAnswer();
                    Collections.shuffle(userList);
                    voteAnswer.setPersistDateTime(LocalDateTime.of(2021, 12, 01, 14, 05, 00));
                    voteAnswer.setUser(
                            userList.stream()
                                    .filter(user -> !user.getId().equals(answer.getUser().getId()))
                                    .collect(Collectors.toList()).get(0)
                    );
                    int random = (int) (Math.random() * 3);
                    if (random == 1) {
                        voteAnswer.setVote(VoteType.UP_VOTE);
                    } else {
                        voteAnswer.setVote(VoteType.DOWN_VOTE);
                    }
                    voteAnswer.setAnswer(answer);
                    return voteAnswer;
                }).forEach(voteOnAnswerService::persist);
    }

    public void createTrackedTag() {
        List<User> userList = userService.getAll();

        // У User'a c id=1 нет TrackedTag
        for (int i = 1; i < userList.size(); i++) {
            int random = (int) (Math.random() * 4);

            for (int j = 1; j <= random; j++) {
                TrackedTag trackedTag = new TrackedTag();
                trackedTag.setPersistDateTime(LocalDateTime.of(2022, 01, 06, 12, 30, 00));
                trackedTag.setTrackedTag(tagService.getAll().get(j));
                trackedTag.setUser(userService.getAll().get(i));
                trackedTagService.persist(trackedTag);
            }
        }
    }

    public void createIgnoredTag() {
        List<User> userList = userService.getAll();

        // У User'a c id=1 нет IgnoredTag
        for (int i = 1; i < userList.size(); i++) {
            int random = (int) (Math.random() * 4);

            for (int j = 1; j <= random; j++) {
                IgnoredTag ignoredTag = new IgnoredTag();
                ignoredTag.setPersistDateTime(LocalDateTime.of(2022, 01, 06, 12, 30, 00));
                ignoredTag.setIgnoredTag(tagService.getAll().get(j));
                ignoredTag.setUser(userService.getAll().get(i));
                ignoredTagService.persist(ignoredTag);
            }
        }
    }

    public void createBookmark(int count) {
        for (int i = 0; i <= count; i++) {
            BookMarks bookMarks = new BookMarks();
            bookMarks.setPersistDateTime(LocalDateTime.of(2023, 03, 20, 0, 0));
            List<User> userList = new ArrayList<>();
            userList.add(userService.getAll().get(1));
            bookMarks.setUser(userService.getAll().get((int) (Math.random() * 50)));
            List<Question> questionList = new ArrayList<>();
            questionList.add(questionService.getAll().get(1));
            bookMarks.setQuestion(questionService.getAll().get(i));
            bookMarksService.persist(bookMarks);
        }
    }

    public void createRelatedTags() {
        List<Tag> count = tagService.getAll();
        for (long i = 1; i <= count.size(); i++) {
            if (tagService.getById(i).isPresent()) {
                for (int k = 0; k < 2; k++) {
                    RelatedTag relatedTag = new RelatedTag();
                    relatedTag.setMainTag(tagService.getById(i).get());
                    Tag childTag = new Tag();
                    childTag.setDescription("Child tag Description " + i + k);
                    childTag.setName("Child tag " + i + k);
                    childTag.setPersistDateTime(LocalDateTime.of(2022, 05, 10, 10, 10));
                    tagService.persist(childTag);
                    relatedTag.setChildTag(childTag);
                    relatedTagService.persist(relatedTag);
                }
            }
        }
    }

    public void createSingleChat(long count) {
        for (long i = 1; i <= count; i++) {
            SingleChat singleChat = new SingleChat();
            Chat chat = new Chat(ChatType.SINGLE);
            chat.setTitle("Some single chat " + i);
            singleChat.setChat(chat);
            singleChat.setUserOne(userService.getById(i).get());
            singleChat.setUseTwo(userService.getById(i + 1).get());
            singleChatService.persist(singleChat);
            Message messageUserOne =new Message("Some message in single chat " + i, userService.getById(i).get(), chat);
            Message messageUserTwo =new Message("Some message in single chat " + (i+1), userService.getById(i+1).get(), chat);
            List<Message> saveMessages = new ArrayList<>();
            saveMessages.add(messageUserOne);
            saveMessages.add(messageUserTwo);
            messageService.persistAll(saveMessages);
        }
    }

    public void createGroupChat(long count) {
        for (long i = 1; i <= count; i++) {
            GroupChat groupChat = new GroupChat();
            Chat chat = new Chat(ChatType.GROUP);
            chat.setTitle("Some group chat " + i);
            Set<User> groupChatUsers = new HashSet<>();
            List<Message> messages = new ArrayList<>();
            for (long k = 1; k < 5; k++) {
                groupChatUsers.add(userService.getById(k + i).get());
                messages.add(new Message("Some message in group chat " + k, userService.getById(k + i).get(), chat));
            }
            groupChat.setChat(chat);
            groupChat.setUsers(groupChatUsers);
            groupChatService.persist(groupChat);
            messageService.persistAll(messages);
        }
    }

    public void init() {
        createRole();
        createUser(50);
        createTag(4);
        createQuestion(50);
        createAnswer();
        createReputation();
        createTrackedTag();
        createIgnoredTag();
        createBookmark(50);
        createRelatedTags();
        createSingleChat(4);
        createGroupChat(2);
    }
}
