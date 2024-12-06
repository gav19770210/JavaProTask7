package ru.gav19770210.javapro.task05.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.repositories.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepo userRepo;
    @InjectMocks
    UserServiceImpl userService;
    private UserEntity userTest;

    @BeforeEach
    public void beforeEach() {
        userTest = new UserEntity(1L, "Bob");
    }

    @AfterEach
    public void afterEach() {
        userTest = null;
    }

    @Test
    @DisplayName("getAllUsers. Получение списка пользователей")
    public void getAllUsersTest() {
        List<UserEntity> userEntities = List.of(userTest);

        Mockito.when(userRepo.findAll())
                .thenReturn(userEntities);

        List<UserEntity> userEntitiesFind = userService.getAllUsers();

        Assertions.assertEquals(userEntities.size(), userEntitiesFind.size());

        Mockito.verify(userRepo).findAll();
    }

    @Test
    @DisplayName("getUserById. Получение пользователя с заданным <id>")
    public void getUserByIdTest() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.of(userTest));

        var userGet = userService.getUserById(userTest.getId());

        Assertions.assertEquals(userTest, userGet);

        Mockito.verify(userRepo).findById(userTest.getId());
    }

    @Test
    @DisplayName("getUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void getUserByIdTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.getUserById(userTest.getId()),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");

        Mockito.verify(userRepo).findById(userTest.getId());
    }

    @Test
    @DisplayName("getUserByName. Получение пользователя с заданным <name>")
    public void getUserByNameTest() {

        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.of(userTest));

        var userGet = userService.getUserByName(userTest.getName());

        Assertions.assertEquals(userTest, userGet);

        Mockito.verify(userRepo).findByName(userTest.getName());
    }

    @Test
    @DisplayName("getUserByName. Проверка на отсутствие пользователя с заданным <name>")
    public void getUserByNameTestNotExistName() {

        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.getUserByName(userTest.getName()),
                "Не сработала проверка на отсутствие пользователя с заданным <name>");

        Mockito.verify(userRepo).findByName(userTest.getName());
    }

    @Test
    @DisplayName("createUser. Проверка на существование пользователя с заданным <name>")
    public void createUserTestExistName() {

        Mockito.when(userRepo.findByName(userTest.getName()))
                .thenReturn(Optional.of(userTest));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(userTest),
                "Не сработала проверка на существование пользователя с заданным <name>");

        Mockito.verify(userRepo).findByName(userTest.getName());
    }

    @Test
    @DisplayName("updateUser. Проверка на отсутствие пользователя с заданным <id>")
    public void updateUserTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(userTest),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");

        Mockito.verify(userRepo).findById(userTest.getId());
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

        Mockito.verify(userRepo).findById(userTest.getId());
        Mockito.verify(userRepo).findByName(userTest.getName());
    }

    @Test
    @DisplayName("deleteUserById. Проверка на отсутствие пользователя с заданным <id>")
    public void deleteUserByIdTestNotExistId() {

        Mockito.when(userRepo.findById(userTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.deleteUserById(userTest.getId()),
                "Не сработала проверка на отсутствие пользователя с заданным <id>");

        Mockito.verify(userRepo).findById(userTest.getId());
    }
}
