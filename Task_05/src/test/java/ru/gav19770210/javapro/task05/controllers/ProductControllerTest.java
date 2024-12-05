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
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task05.entities.ProductType;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.services.ProductService;
import ru.gav19770210.javapro.task05.services.ProductServiceImpl;
import ru.gav19770210.javapro.task05.services.UserServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    private final UserEntity userTest = new UserEntity(1L, "Bob");
    private final ProductEntity productTest = new ProductEntity(1L, 1L, "40817810100000000001", BigDecimal.ZERO, ProductType.CARD);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("getAllProducts. Получение списка продуктов")
    public void getAllProductsTest() throws Exception {
        List<ProductEntity> productEntities = List.of(productTest);

        Mockito.when(productService.getAllProducts()).thenReturn(productEntities);

        mockMvc.perform(get("/product/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].accountNumber", Matchers.is(productTest.getAccountNumber())));

        Mockito.verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("getUserProducts. Получение списка продуктов пользователя")
    public void getUserProductsTest() throws Exception {
        List<ProductEntity> productEntities = List.of(productTest);

        Mockito.when(productService.getUserProducts(productTest.getUserId())).thenReturn(productEntities);

        mockMvc.perform(get("/product/" + productTest.getUserId() + "/get-by-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].accountNumber", Matchers.is(productTest.getAccountNumber())));

        Mockito.verify(productService).getUserProducts(productTest.getUserId());
    }

    @Test
    @DisplayName("getProductById. Получение продукта с заданным <id>")
    public void getProductByIdTest() throws Exception {

        Mockito.when(productService.getProductById(productTest.getId())).thenReturn(productTest);

        mockMvc.perform(get("/product/" + productTest.getId() + "/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(productTest.getId()))
                .andExpect(jsonPath("accountNumber", Matchers.is(productTest.getAccountNumber())));

        Mockito.verify(productService).getProductById(productTest.getId());
    }

    @Test
    @DisplayName("getProductById. Проверка на отсутствие продукта с заданным <id>")
    public void getProductByIdTestNotExistId() throws Exception {
        var errorMessage = String.format(ProductServiceImpl.msgProductNotFoundById, productTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(productService).getProductById(ArgumentMatchers.any());

        mockMvc.perform(get("/product/" + productTest.getId() + "/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).getProductById(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("createProduct. Создание продукта")
    public void createProductTest() throws Exception {

        Mockito.when(productService.createProduct(productTest)).thenReturn(productTest);

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accountNumber", Matchers.is(productTest.getAccountNumber())));

        Mockito.verify(productService).createProduct(productTest);
    }

    @Test
    @DisplayName("createProduct. Проверка на отсутствие пользователя с заданным <userId>")
    public void createProductTestNotExistUserId() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundById, productTest.getUserId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(productService).createProduct(ArgumentMatchers.any());

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).createProduct(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("createProduct. Проверка на существование продукта с заданным <accountNumber>")
    public void createProductTestExistAccNum() throws Exception {
        var errorMessage = String.format(ProductServiceImpl.msgProductExistByAccNum, productTest.getAccountNumber());

        Mockito.doThrow(new IllegalArgumentException(errorMessage))
                .when(productService).createProduct(ArgumentMatchers.any());

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).createProduct(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("updateProduct. Обновление продукта")
    public void updateProductTest() throws Exception {

        Mockito.when(productService.updateProduct(productTest)).thenReturn(productTest);

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(productTest.getId()));

        Mockito.verify(productService).updateProduct(productTest);
    }

    @Test
    @DisplayName("updateProduct. Проверка на отсутствие продукта с заданным <id>")
    public void updateProductTestNotExistId() throws Exception {
        var errorMessage = String.format(ProductServiceImpl.msgProductNotFoundById, productTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(productService).updateProduct(ArgumentMatchers.any());

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).updateProduct(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("updateProduct. Проверка на отсутствие пользователя с заданным <userId>")
    public void updateProductTestNotExistUserId() throws Exception {
        var errorMessage = String.format(UserServiceImpl.msgUserNotFoundById, productTest.getUserId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(productService).updateProduct(ArgumentMatchers.any());

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).updateProduct(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("updateProduct. Проверка на существование продукта с заданным <accountNumber>")
    public void updateProductTestExistName() throws Exception {
        var errorMessage = String.format(ProductServiceImpl.msgProductExistByAccNum, productTest.getAccountNumber());

        Mockito.doThrow(new IllegalArgumentException(errorMessage))
                .when(productService).updateProduct(ArgumentMatchers.any());

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).updateProduct(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("deleteProductById. Проверка на отсутствие продукта с заданным <id>")
    public void deleteProductByIdTestNotExistId() throws Exception {
        var errorMessage = String.format(ProductServiceImpl.msgProductNotFoundById, productTest.getId());

        Mockito.doThrow(new NoSuchElementException(errorMessage))
                .when(productService).deleteProductById(ArgumentMatchers.any());

        mockMvc.perform(delete("/product/" + productTest.getId() + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(errorMessage));

        Mockito.verify(productService).deleteProductById(ArgumentMatchers.any());
    }
}
