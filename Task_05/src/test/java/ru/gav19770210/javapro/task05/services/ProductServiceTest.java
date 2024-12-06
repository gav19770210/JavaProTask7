package ru.gav19770210.javapro.task05.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task05.entities.ProductType;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.repositories.ProductRepo;
import ru.gav19770210.javapro.task05.repositories.UserRepo;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    UserRepo userRepo;
    @Mock
    ProductRepo productRepo;
    @InjectMocks
    ProductServiceImpl productService;
    private UserEntity userTest;
    private ProductEntity productTest;

    @BeforeEach
    public void beforeEach() {
        userTest = new UserEntity(1L, "Bob");
        productTest = new ProductEntity(1L, 1L, "40817810100000000001", BigDecimal.ZERO, ProductType.CARD);
    }

    @AfterEach
    public void afterEach() {
        productTest = null;
        userTest = null;
    }

    @Test
    @DisplayName("findAll. Получение списка продуктов")
    public void getAllProductsTest() {
        List<ProductEntity> productEntities = List.of(productTest);

        Mockito.when(productRepo.findAll()).thenReturn(productEntities);

        List<ProductEntity> productEntitiesGet = productService.getAllProducts();

        Assertions.assertEquals(productEntities.size(), productEntitiesGet.size());

        Mockito.verify(productRepo).findAll();
    }

    @Test
    @DisplayName("getProductById. Получение продукта с заданным <id>")
    public void getProductByIdTest() {

        Mockito.when(productRepo.findById(productTest.getId())).thenReturn(Optional.of(productTest));

        var productGet = productService.getProductById(productTest.getId());

        Assertions.assertEquals(productTest, productGet);

        Mockito.verify(productRepo).findById(productTest.getId());
    }

    @Test
    @DisplayName("getProductById. Проверка на отсутствие продукта с заданным <id>")
    public void getProductByIdTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.getProductById(productTest.getId()),
                "Не сработала проверка на отсутствие продукта с заданным <id>");

        Mockito.verify(productRepo).findById(productTest.getId());
    }

    @Test
    @DisplayName("createProduct. Проверка на отсутствие пользователя с заданным <userId>")
    public void createProductTestNotExistUserId() {

        Mockito.when(userRepo.findById(productTest.getUserId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.createProduct(productTest),
                "Не сработала проверка на отсутствие пользователя с заданным <userId>");

        Mockito.verify(userRepo).findById(productTest.getUserId());
    }

    @Test
    @DisplayName("createProduct. Проверка на существование продукта с заданным <accountNumber>")
    public void createProductTestExistAccountNumber() {

        Mockito.when(userRepo.findById(productTest.getUserId()))
                .thenReturn(Optional.of(userTest));
        Mockito.when(productRepo.findByAccountNumber(productTest.getAccountNumber()))
                .thenReturn(Optional.of(productTest));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(productTest),
                "Не сработала проверка на существование продукта с заданным <accountNumber>");

        Mockito.verify(userRepo).findById(productTest.getUserId());
        Mockito.verify(productRepo).findByAccountNumber(productTest.getAccountNumber());
    }

    @Test
    @DisplayName("updateProduct. Проверка на отсутствие продукта с заданным <id>")
    public void updateProductTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.updateProduct(productTest),
                "Не сработала проверка на отсутствие продукта с заданным <id>");

        Mockito.verify(productRepo).findById(productTest.getId());
    }

    @Test
    @DisplayName("updateProduct. Проверка на отсутствие пользователя с заданным <userId>")
    public void updateProductTestNotExistUserId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.of(productTest));
        Mockito.when(userRepo.findById(productTest.getUserId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.updateProduct(productTest),
                "Не сработала проверка на отсутствие пользователя с заданным <userId>");

        Mockito.verify(productRepo).findById(productTest.getId());
        Mockito.verify(userRepo).findById(productTest.getUserId());
    }

    @Test
    @DisplayName("updateProduct. Проверка на существование продукта с заданным <accountNumber>")
    public void updateProductTestExistAccountNumber() {

        var productByAccNum = new ProductEntity();
        productByAccNum.setId(productTest.getId() + 1L);
        productByAccNum.setUserId(productTest.getUserId());
        productByAccNum.setAccountNumber(productTest.getAccountNumber());

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.of(productTest));
        Mockito.when(userRepo.findById(productTest.getUserId()))
                .thenReturn(Optional.of(userTest));
        Mockito.when(productRepo.findByAccountNumber(productTest.getAccountNumber()))
                .thenReturn(Optional.of(productByAccNum));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> productService.updateProduct(productTest),
                "Не сработала проверка на существование продукта с заданным <accountNumber>");

        Mockito.verify(productRepo).findById(productTest.getId());
        Mockito.verify(userRepo).findById(productTest.getUserId());
        Mockito.verify(productRepo).findByAccountNumber(productTest.getAccountNumber());
    }

    @Test
    @DisplayName("deleteProductById. Проверка на отсутствие продукта с заданным <id>")
    public void deleteProductByIdTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.deleteProductById(productTest.getId()),
                "Не сработала проверка на отсутствие продукта с заданным <id>");

        Mockito.verify(productRepo).findById(productTest.getId());
    }
}
