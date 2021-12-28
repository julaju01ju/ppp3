package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@SpringBootTest(classes = JmApplication.class)
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@TestPropertySource(properties = "test/resources/application.properties")
class TestQuestionResourceController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

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
    @DataSet(value = {"dataset/question/questionQuestionApi.yml", "dataset/question/user.yml"},
            disableConstraints = true, cleanBefore = true)

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
}