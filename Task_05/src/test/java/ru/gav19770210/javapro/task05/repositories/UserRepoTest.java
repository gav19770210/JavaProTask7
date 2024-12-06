package ru.gav19770210.javapro.task05.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gav19770210.javapro.task05.entities.UserEntity;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {
    @Autowired
    UserRepo userRepo;
    private UserEntity userTest;

    @BeforeEach
    public void beforeEach() {
        userTest = new UserEntity();
        userTest.setName("Test_0123456789");
    }

    @AfterEach
    public void afterEach() {
        userRepo.deleteAll();
        userTest = null;
    }

    @Test
    @DisplayName("findAll. Получение всех пользователей")
    void findAllTest() {
        var userEntityList = (List<UserEntity>) userRepo.findAll();
        var listCount = userEntityList.size();
        var userCreate = userRepo.save(userTest);
        userEntityList = (List<UserEntity>) userRepo.findAll();

        Assertions.assertNotNull(userCreate);
        Assertions.assertEquals(listCount + 1, userEntityList.size());
    }

    @Test
    @DisplayName("findById. Получение пользователя с заданным <id>")
    void findByIdTest() {
        var userCreate = userRepo.save(userTest);
        var userGet = userRepo.findById(userCreate.getId()).get();

        Assertions.assertNotNull(userCreate);
        Assertions.assertEquals(userCreate, userGet);
    }

    @Test
    @DisplayName("findByName. Получение пользователя с заданным <name>")
    void findByNameTest() {
        var userCreate = userRepo.save(userTest);
        var userGet = userRepo.findByName(userCreate.getName()).get();

        Assertions.assertNotNull(userCreate);
        Assertions.assertEquals(userCreate, userGet);
    }

    @Test
    @DisplayName("updateName. Обновление пользователя")
    void updateNameTest() {
        var userCreate = userRepo.save(userTest);
        var newUserName = userCreate.getName() + "$";
        userCreate.setName(newUserName);
        var userUpdate = userRepo.save(userCreate);

        Assertions.assertNotNull(userCreate);
        Assertions.assertEquals(userCreate.getName(), userUpdate.getName());
    }

    @Test
    @DisplayName("deleteById. Удаление пользователя с заданным <id>")
    void deleteByIdTest() {
        var userCreate = userRepo.save(userTest);
        userRepo.deleteById(userCreate.getId());
        var userGet = userRepo.findById(userCreate.getId());

        Assertions.assertNotNull(userCreate);
        Assertions.assertTrue(userGet.isEmpty());
    }
}
