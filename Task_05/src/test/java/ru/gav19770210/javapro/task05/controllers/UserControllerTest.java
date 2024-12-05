package ru.gav19770210.javapro.task05.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.services.UserService;
import ru.gav19770210.javapro.task05.services.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private final UserEntity userTest = new UserEntity(1L, "Bob");
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("getAllUsers. Получение списка пользователей")
    public void getAllUsersTest() throws Exception {
        List<UserEntity> userEntities = List.of(userTest);

        Mockito.when(userService.getAllUsers()).thenReturn(userEntities);

        mockMvc.perform(get("/user/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(userTest.getName())));
    }

    @Test
    @DisplayName("getUserById. Получение пользователя с заданным <id>")
    public void getUserByIdTest() throws Exception {

        Mockito.when(userService.getUserById(userTest.getId())).thenReturn(userTest);

        mockMvc.perform(get("/user/" + userTest.getId() + "/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userTest.getId()))
                .andExpect(jsonPath("name", Matchers.is(userTest.getName())));
    }

    @Test
    @DisplayName("getUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void getUserByIdTestNotExistId() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundById, userTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(userService).getUserById(ArgumentMatchers.any());

        mockMvc.perform(get("/user/" + userTest.getId() + "/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("getUserByName. Получение пользователя с заданным <name>")
    public void getUserByNameTest() throws Exception {

        Mockito.when(userService.getUserByName(userTest.getName())).thenReturn(userTest);

        mockMvc.perform(get("/user/" + userTest.getName() + "/get-by-name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userTest.getId()))
                .andExpect(jsonPath("name", Matchers.is(userTest.getName())));
    }

    @Test
    @DisplayName("getUserByName. Проверка на отсутствие пользователя с заданным <name>")
    public void getUserByNameTestNotExistByName() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundByName, userTest.getName());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(userService).getUserByName(ArgumentMatchers.any());

        mockMvc.perform(get("/user/" + userTest.getName() + "/get-by-name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("createUser. Создание пользователя")
    public void createUserTest() throws Exception {

        Mockito.when(userService.createUser(userTest)).thenReturn(userTest);

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.is(userTest.getName())));
    }

    @Test
    @DisplayName("createUser. Проверка на существование пользователя с заданным <name>")
    public void createUserTestExistName() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserExistByName, userTest.getName());

        Mockito.doThrow(new IllegalArgumentException(errorMessage))
                .when(userService).createUser(ArgumentMatchers.any());

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("updateUser. Обновление пользователя")
    public void updateUserTest() throws Exception {

        Mockito.when(userService.updateUser(userTest)).thenReturn(userTest);

        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userTest.getId()));
    }

    @Test
    @DisplayName("updateUser. Проверка на отсутствие пользователя с заданным <id>")
    public void updateUserTestNotExistId() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundById, userTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(userService).updateUser(ArgumentMatchers.any());

        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("updateUser. Проверка на существование пользователя с заданным <name>")
    public void updateUserTestExistName() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserExistByName, userTest.getName());

        Mockito.doThrow(new IllegalArgumentException(errorMessage))
                .when(userService).updateUser(ArgumentMatchers.any());

        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));
    }

    @Test
    @DisplayName("deleteUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void deleteUserByIdTestNotExistId() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundById, userTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(userService).deleteUserById(ArgumentMatchers.any());

        mockMvc.perform(delete("/user/" + userTest.getId() + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));
    }
}