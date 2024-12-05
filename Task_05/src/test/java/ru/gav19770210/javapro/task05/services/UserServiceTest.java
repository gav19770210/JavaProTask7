package ru.gav19770210.javapro.task05.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.repositories.UserRepo;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final UserEntity userTest = new UserEntity(1L, "Bob");
    @Mock
    UserRepo userRepo;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("getUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void getUserByIdTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.getUserById(userTest.getId()),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");
    }

    @Test
    @DisplayName("getUserByName. Проверка на отсутствие пользователя с заданным <name>")
    public void getUserByNameTestNotExistName() {

        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.getUserByName(userTest.getName()),
                "Не сработала проверка на отсутствие пользователя с заданным <name>");
    }

    @Test
    @DisplayName("createUser. Проверка на существование пользователя с заданным <name>")
    public void createUserTestExistName() {

        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.of(userTest));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(userTest),
                "Не сработала проверка на существование пользователя с заданным <name>");
    }

    @Test
    @DisplayName("updateUser. Проверка на отсутствие пользователя с заданным <id>")
    public void updateUserTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(userTest),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");
    }

    @Test
    @DisplayName("updateUser. Проверка на существование пользователя с заданным <name>")
    public void updateUserTestExistName() {

        var userByName = new UserEntity();
        userByName.setId(userTest.getId() + 1L);
        userByName.setName(userTest.getName());

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.of(userTest));
        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.of(userByName));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(userTest),
                "Не сработала проверка на существование пользователя с заданным <name>");
    }

    @Test
    @DisplayName("deleteUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void deleteUserByIdTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.deleteUserById(userTest.getId()),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");
    }
}
