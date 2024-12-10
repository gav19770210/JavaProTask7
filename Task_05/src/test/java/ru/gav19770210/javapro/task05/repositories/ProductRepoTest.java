package ru.gav19770210.javapro.task05.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task05.entities.ProductType;
import ru.gav19770210.javapro.task05.entities.UserEntity;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTest {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepo productRepo;
    private UserEntity userTest;
    private ProductEntity productTest;

    @BeforeEach
    public void beforeEach() {
        userTest = new UserEntity();
        userTest.setName("Test_0123456789");
        var userCreate = userRepo.save(userTest);

        productTest = new ProductEntity();
        productTest.setUserId(userCreate.getId());
        productTest.setAccountNumber("Test_0123456789");
        productTest.setBalance(BigDecimal.ZERO);
        productTest.setType(ProductType.CARD);
    }

    @AfterEach
    public void afterEach() {
        productRepo.deleteAll();
        productTest = null;

        userRepo.deleteAll();
        userTest = null;
    }

    @Test
    @DisplayName("findAll. Получение всех продуктов")
    void findAllTest() {
        var productEntityList = (List<ProductEntity>) productRepo.findAll();
        var listCount = productEntityList.size();
        var productCreate = productRepo.save(productTest);
        productEntityList = (List<ProductEntity>) productRepo.findAll();

        Assertions.assertNotNull(productCreate);
        Assertions.assertEquals(listCount + 1, productEntityList.size());
    }

    @Test
    @DisplayName("getAllByUserId. Получение всех продуктов")
    void getAllByUserIdTest() {
        var productEntityList = (List<ProductEntity>) productRepo.getAllByUserId(userTest.getId());
        var listCount = productEntityList.size();
        var productCreate = productRepo.save(productTest);
        productEntityList = productRepo.getAllByUserId(userTest.getId());

        Assertions.assertNotNull(productCreate);
        Assertions.assertEquals(listCount + 1, productEntityList.size());
    }

    @Test
    @DisplayName("findById. Получение продукта с заданным <id>")
    void findByIdTest() {
        var productCreate = productRepo.save(productTest);
        var productGet = productRepo.findById(productCreate.getId()).get();

        Assertions.assertNotNull(productCreate);
        Assertions.assertEquals(productCreate, productGet);
    }

    @Test
    @DisplayName("findByAccountNumber. Получение продукта с заданным <AccountNumber>")
    void findByNameTest() {
        var productCreate = productRepo.save(productTest);
        var productGet = productRepo.findByAccountNumber(productCreate.getAccountNumber()).get();

        Assertions.assertNotNull(productCreate);
        Assertions.assertEquals(productCreate, productGet);
    }

    @Test
    @DisplayName("updateName. Обновление продукта")
    void updateNameTest() {
        var productCreate = productRepo.save(productTest);
        var newAccountNumber = productCreate.getAccountNumber() + "$";
        productCreate.setAccountNumber(newAccountNumber);
        var productUpdate = productRepo.save(productCreate);

        Assertions.assertNotNull(productCreate);
        Assertions.assertEquals(productCreate.getAccountNumber(), productUpdate.getAccountNumber());
    }

    @Test
    @DisplayName("deleteById. Удаление продукта с заданным <id>")
    void deleteByIdTest() {
        var productCreate = productRepo.save(productTest);
        productRepo.deleteById(productCreate.getId());
        var productGet = productRepo.findById(productCreate.getId());

        Assertions.assertNotNull(productCreate);
        Assertions.assertTrue(productGet.isEmpty());
    }
}
