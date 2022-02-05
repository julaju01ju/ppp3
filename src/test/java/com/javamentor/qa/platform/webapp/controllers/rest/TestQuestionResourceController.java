package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestQuestionResourceController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DataSet(value = {"dataset/question/questionQuestionApi.yml", "dataset/question/user.yml"}, disableConstraints = true)
    public void getQuestionCount() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        get("/api/user/question/count")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("8"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml",
            "dataset/QuestionResourceController/comment.yml",
            "dataset/QuestionResourceController/comment_question.yml"})
    void getQuestionById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description to 101"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.authorId").value(101))
                .andExpect(jsonPath("$.authorName").value("Constantin"))
                .andExpect(jsonPath("$.authorImage").value("link"))
                .andExpect(jsonPath("$.authorReputation").value(50))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andExpect(jsonPath("$.countValuable").value(2))
                .andExpect(jsonPath("$.countAnswer").value(1))

                .andExpect(jsonPath("$.listTagDto.[0].id").value(101))
                .andExpect(jsonPath("$.listTagDto.[0].name").value("TAG101"))
                .andExpect(jsonPath("$.listCommentDto[0].id").value(102))
                .andExpect(jsonPath("$.listCommentDto[0].comment").value("Some text of comment"))
                .andExpect(jsonPath("$.listCommentDto[0].userId").value(102));
        
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void shouldNotGetQuestionById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/105")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithoutTitle() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithoutDescription() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithoutTags() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithEmptyTitle() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setDescription("Description");
        questionCreateDto.setTitle("");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithEmptyDescription() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithEmptyTags() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        questionCreateDto.setTags(new ArrayList<>());

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithNameTagWhenExist() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("TAG100");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        String sqlCount = "select CAST(count(tag.id) as int) from Tag tag where tag.name = 'TAG100'";
        int rowCount = (int) entityManager.createQuery(sqlCount).getSingleResult();
        Assertions.assertTrue(rowCount == 1);


        String sql = "select tag.id from Tag tag where tag.name = 'TAG100'";
        Long tagId = (long) entityManager.createQuery(sql).getSingleResult();
        Assertions.assertTrue(tagId == 100L);
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionCreateDtoWithNameTagWhenNotExist() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Title");
        questionCreateDto.setDescription("Description");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        String sqlCount = "select CAST(count(tag.id) as int) from Tag tag where tag.name = 'Test'";
        int rowCount = (int) entityManager.createQuery(sqlCount).getSingleResult();
        Assertions.assertTrue(rowCount == 1);

    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionHasBeenCreated() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("questionHasBeenCreated");
        questionCreateDto.setDescription("questionHasBeenCreated");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");


        String sql = "select CAST(count(question.id) as int) from Question question where question.id =: questionDtoId";
        int rowCount = (int) entityManager.createQuery(sql).setParameter("questionDtoId", id.longValue()).getSingleResult();
        Assertions.assertTrue(rowCount == 1);
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void questionHasBeenCreated_CheckTagList() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("questionHasBeenCreated_CheckTagList");
        questionCreateDto.setDescription("questionHasBeenCreated_CheckTagList");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");
        List<HashMap> tagListQuestionDto = JsonPath.read(questionDtoJsonString, "$.listTagDto");
        List<Integer> listId = tagListQuestionDto.stream().map(list -> (int) list.get("id")).collect(Collectors.toList());

        String sql = "select CAST(question_has_tag.tag_id as int)" +
                " from question_has_tag where question_has_tag.question_id = ?";
        List<Integer> listTag = entityManager.createNativeQuery(sql).setParameter(1, id).getResultList();

        Assertions.assertArrayEquals(listTag.toArray(), listId.toArray());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/role.yml",
            "dataset/QuestionResourceController/user_entity.yml",
            "dataset/QuestionResourceController/tag.yml"}, disableConstraints = true, cleanBefore = true)
    void checkFieldsQuestionInReturnedQuestionDto() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("checkFieldsReturnedQuestionDto");
        questionCreateDto.setDescription("checkFieldsReturnedQuestionDto");

        TagDto tagDto = new TagDto();
        tagDto.setName("Test");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("TAG100");
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("TAG101");
        List<TagDto> listTagDto = new ArrayList<>();
        listTagDto.add(tagDto);
        listTagDto.add(tagDto2);
        listTagDto.add(tagDto3);
        questionCreateDto.setTags(listTagDto);

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String questionDtoJsonString = mockMvc.perform(
                        post("/api/user/question/")
                                .header(AUTHORIZATION, USER_TOKEN)
                                .content(new ObjectMapper().writeValueAsString(questionCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(questionDtoJsonString, "$.id");

        String sql = "select question from Question question join fetch question.user where question.id =: questionDtoId";
        Question questionFromBase = entityManager.createQuery(sql, Question.class).setParameter("questionDtoId", id.longValue()).getSingleResult();

        Assertions.assertEquals(questionFromBase.getId(), id.longValue());
        Assertions.assertEquals(questionFromBase.getTitle(),
                (JsonPath.read(questionDtoJsonString, "$.title")));
        Assertions.assertEquals(questionFromBase.getDescription(),
                (JsonPath.read(questionDtoJsonString, "$.description")));
        Assertions.assertEquals(questionFromBase.getUser().getNickname(),
                (JsonPath.read(questionDtoJsonString, "$.authorName")));
        Assertions.assertEquals(questionFromBase.getUser().getId().intValue(),
                (int) JsonPath.read(questionDtoJsonString, "$.authorId"));
        Assertions.assertEquals(questionFromBase.getUser().getImageLink(),
                (JsonPath.read(questionDtoJsonString, "$.authorImage")));
        Assertions.assertEquals(questionFromBase.getPersistDateTime().toString().substring(0, 23),
                (JsonPath.read(questionDtoJsonString, "$.persistDateTime").toString().substring(0, 23)));
        Assertions.assertEquals(questionFromBase.getLastUpdateDateTime().toString().substring(0, 23),
                (JsonPath.read(questionDtoJsonString, "$.lastUpdateDateTime").toString().substring(0, 23)));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.viewCount"));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.countAnswer"));
        Assertions.assertEquals(0, (int) JsonPath.read(questionDtoJsonString, "$.countValuable"));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithoutTagsInParams() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question?page=1&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("TAG101"))
                .andExpect(jsonPath("$.items[1].listTagDto.size()").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].title").value("title"))
                .andExpect(jsonPath("$.items[0].description").value("description to 101"))
                .andExpect(jsonPath("$.items[0].lastUpdateDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.items[0].persistDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(2))
                .andExpect(jsonPath("$.items[0].countAnswer").value(1));

    }
    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedAndIgnoredTagsInParams() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question?page=1&trackedTag=101,102,104&ignoredTag=103&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(102))
                .andExpect(jsonPath("$.items[2].listTagDto[0].id").value(104));
    }
    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedTagsInParams() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question?page=1&trackedTag=102&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(102));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithIgnoredTagsInParams() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question?page=1&ignoredTag=102&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.items[0].listTagDto[?(@.id == 102)]").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.items[1].listTagDto[?(@.id == 102)]").doesNotHaveJsonPath())
                ;
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/typesOfVote.yml",
            "dataset/QuestionResourceController/user.yml",
            "dataset/QuestionResourceController/voteQuestionApi.yml"},
            disableConstraints = true, transactional = true)

    public void postUpVoteQuestion() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/101/upVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(entityManager.createQuery("select vp from VoteQuestion vp WHERE vp.question.id =:questionId and vp.user.id =: userId", VoteQuestion.class)
                .setParameter("questionId", 101L)
                .setParameter("userId", 101L));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/typesOfVote.yml",
            "dataset/QuestionResourceController/user.yml",
            "dataset/QuestionResourceController/voteQuestionApi.yml"},
            disableConstraints = true, transactional = true)

    public void postDownVoteQuestion() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/101/downVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(entityManager.createQuery("select vp from VoteQuestion vp WHERE vp.question.id =:questionId and vp.user.id =: userId", VoteQuestion.class)
                .setParameter("questionId", 101L)
                .setParameter("userId", 101L));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/typesOfVote.yml",
            "dataset/QuestionResourceController/user.yml",
            "dataset/QuestionResourceController/voteQuestionApi.yml"},
            disableConstraints = true, transactional = true, cleanBefore = true)

    public void postWrongIdQuestion() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/333/downVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/typesOfVote.yml",
            "dataset/QuestionResourceController/user.yml",
            "dataset/QuestionResourceController/voteQuestionApi.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml"},
            disableConstraints = true, transactional = true)

    public void repeatVotingForQuestion() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("USER");
        authenticationRequest.setUsername("user@mail.ru");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(
                        post("/api/user/question/101/downVote")
                                .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithoutTagsInParamsNoAnswer() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[0].title").value("title"))
                .andExpect(jsonPath("$.items[0].description").value("description to 104"))
                .andExpect(jsonPath("$.items[0].lastUpdateDateTime").value("2021-12-06T07:00:00"))
                .andExpect(jsonPath("$.items[0].persistDateTime").value("2021-12-06T07:00:00"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(0))
                .andExpect(jsonPath("$.items[0].countAnswer").value(0));

    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedAndIgnoredTagsInParamsNoAnswer() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTag=101,102,104&ignoredTag=103&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(104))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name").value("Some another name here"))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[0].title").value("title"))
                .andExpect(jsonPath("$.items[0].description").value("description to 104"))
                .andExpect(jsonPath("$.items[0].lastUpdateDateTime").value("2021-12-06T07:00:00"))
                .andExpect(jsonPath("$.items[0].persistDateTime").value("2021-12-06T07:00:00"))
                .andExpect(jsonPath("$.items[0].authorId").value(101))
                .andExpect(jsonPath("$.items[0].authorName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].authorImage").value("link"))
                .andExpect(jsonPath("$.items[0].authorReputation").value(50))
                .andExpect(jsonPath("$.items[0].viewCount").value(0))
                .andExpect(jsonPath("$.items[0].countValuable").value(0))
                .andExpect(jsonPath("$.items[0].countAnswer").value(0));
    }
    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedTagsInParamsNoAnswer() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&trackedTag=102&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(0));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithIgnoredTagsInParamsNoAnswer() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/noAnswer?page=1&ignoredTag=102&items=3")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(1))
                .andExpect(jsonPath("$.items[0].listTagDto[?(@.id == 102)]").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.items[1].listTagDto[?(@.id == 102)]").doesNotHaveJsonPath());
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/GetQuestionsSortedByPersistDate/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"},disableConstraints = true, cleanBefore = true)
    void getQuestionsWithoutTagsInParamsSortedByPersistDateDESC() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&items=4")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(4))
                .andExpect(jsonPath("$.itemsOnPage").value(4))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");

        Assertions.assertTrue((int) list.get(0).get("id") == 104);
        Assertions.assertTrue(list.get(0).get("description").equals("description to 104"));
        Assertions.assertTrue(list.get(0).get("persistDateTime").equals("2021-12-09T03:00:00"));
        Assertions.assertTrue((int) list.get(0).get("authorId") == 101);

        Assertions.assertTrue((int) list.get(1).get("id") == 103);
        Assertions.assertTrue(list.get(1).get("description").equals("description to 103"));
        Assertions.assertTrue(list.get(1).get("persistDateTime").equals("2021-12-08T03:00:00"));
        Assertions.assertTrue((int) list.get(1).get("authorId") == 103);

        Assertions.assertTrue((int) list.get(2).get("id") == 102);
        Assertions.assertTrue(list.get(2).get("description").equals("description to 102"));
        Assertions.assertTrue(list.get(2).get("persistDateTime").equals("2021-12-07T03:00:00"));
        Assertions.assertTrue((int) list.get(2).get("authorId") == 102);

        Assertions.assertTrue((int) list.get(3).get("id") == 101);
        Assertions.assertTrue(list.get(3).get("description").equals("description to 101"));
        Assertions.assertTrue(list.get(3).get("persistDateTime").equals("2021-12-06T03:00:00"));
        Assertions.assertTrue((int) list.get(3).get("authorId") == 101);
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/GetQuestionsSortedByPersistDate/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedAndIgnoredTagsInParamsSortedByPersistDateDESC() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&trackedTag=101,102,104&ignoredTag=103&items=4")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(3))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(104))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id").value(102))
                .andExpect(jsonPath("$.items[2].listTagDto[0].id").value(101));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/GetQuestionsSortedByPersistDate/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithTrackedTagsInParamsSortedByPersistDateDESC() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&trackedTag=102&items=4")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.items[0].id").value(103))
                .andExpect(jsonPath("$.items[1].id").value(102));
    }

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/GetQuestionsSortedByPersistDate/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionsWithIgnoredTagsInParamsSortedByPersistDateDESC() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/new?page=1&ignoredTag=102&items=4")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(2))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[1].id").value(101));
    }
}
