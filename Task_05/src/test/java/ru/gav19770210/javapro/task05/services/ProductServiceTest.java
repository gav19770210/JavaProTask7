package ru.gav19770210.javapro.task05.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private final UserEntity userTest = new UserEntity(1L, "Bob");
    private final ProductEntity productTest = new ProductEntity(1L, 1L, "40817810100000000001", BigDecimal.ZERO, ProductType.CARD);
    @Mock
    UserRepo userRepo;
    @Mock
    ProductRepo productRepo;
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @DisplayName("getProductById. Проверка на отсутствие продукта с заданным <id>")
    public void getProductByIdTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.getProductById(productTest.getId()),
                "Не сработала проверка на отсутствие продукта с заданным <id>");
    }

    @Test
    @DisplayName("createProduct. Проверка на отсутствие пользователя с заданным <userId>")
    public void createProductTestNotExistUserId() {

        Mockito.when(userRepo.findById(productTest.getUserId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.createProduct(productTest),
                "Не сработала проверка на отсутствие пользователя с заданным <userId>");
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
    }

    @Test
    @DisplayName("updateProduct. Проверка на отсутствие продукта с заданным <id>")
    public void updateProductTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.updateProduct(productTest),
                "Не сработала проверка на отсутствие продукта с заданным <id>");
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
    }

    @Test
    @DisplayName("deleteProductById. Проверка на отсутствие продукта с заданным <id>")
    public void deleteProductByIdTestNotExistId() {

        Mockito.when(productRepo.findById(productTest.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> productService.deleteProductById(productTest.getId()),
                "Не сработала проверка на отсутствие продукта с заданным <id>");
    }
}
