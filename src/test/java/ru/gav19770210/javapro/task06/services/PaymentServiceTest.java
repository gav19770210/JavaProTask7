package ru.gav19770210.javapro.task06.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task05.entities.ProductType;
import ru.gav19770210.javapro.task06.dto.PaymentRequest;
import ru.gav19770210.javapro.task06.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

@RestClientTest(PaymentService.class)
public class PaymentServiceTest {
    private final String productBaseUrl;
    private final String productGetByIdUri;
    private final String productGetByUserUri;
    private final String productUpdateUri;
    @Autowired
    public PaymentService paymentService;
    @Autowired
    public ObjectMapper objectMapper;
    @Autowired
    public RestTemplate restTemplate;
    private ProductEntity productTest;
    private MockRestServiceServer mockRestServiceServer;

    public PaymentServiceTest(@Value("${service-url.product.base-url}") String productBaseUrl,
                              @Value("${service-url.product.get-by-user}") String productGetByUserUri,
                              @Value("${service-url.product.get-by-id}") String productGetByIdUri,
                              @Value("${service-url.product.update}") String productUpdateUri) {
        this.productBaseUrl = productBaseUrl;
        this.productGetByIdUri = productGetByIdUri;
        this.productGetByUserUri = productGetByUserUri;
        this.productUpdateUri = productUpdateUri;
    }

    @BeforeEach
    public void beforeEach() {
        productTest = new ProductEntity(1L, 1L, "40817810100000000001", BigDecimal.TEN, ProductType.CARD);

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @AfterEach
    public void afterEach() {
        productTest = null;
    }

    @Test
    public void getUserProductsTest() throws Exception {
        List<ProductEntity> productEntities = List.of(productTest);

        var url = productBaseUrl + productGetByUserUri.replace("{user_id}", productTest.getUserId().toString());
        System.out.println(url);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(objectMapper.writeValueAsString(productEntities),
                        MediaType.APPLICATION_JSON));

        var productEntitiesGet = paymentService.getUserProducts(productTest.getUserId());

        Assertions.assertEquals(productEntities.size(), productEntitiesGet.size());

        mockRestServiceServer.verify();
    }

    @Test
    public void getProductByIdTest() throws Exception {
        var url = productBaseUrl + productGetByIdUri.replace("{id}", productTest.getId().toString());
        System.out.println(url);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(objectMapper.writeValueAsString(productTest),
                        MediaType.APPLICATION_JSON));

        var productGet = paymentService.getProductById(productTest.getId());

        Assertions.assertEquals(productTest, productGet);

        mockRestServiceServer.verify();
    }

    @Test
    public void debitTest() throws Exception {
        var url = productBaseUrl + productGetByIdUri.replace("{id}", productTest.getId().toString());
        System.out.println(url);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(objectMapper.writeValueAsString(productTest),
                        MediaType.APPLICATION_JSON));

        var url2 = productBaseUrl + productUpdateUri;
        System.out.println(url2);

        var paymentRequest = new PaymentRequest(productTest.getId(), BigDecimal.valueOf(2));
        var productUpdate = new ProductEntity(productTest.getId(),
                productTest.getUserId(),
                productTest.getAccountNumber(),
                productTest.getBalance().subtract(paymentRequest.getSumma()),
                productTest.getType());
        var paymentResponse = new PaymentResponse(productTest.getId(),
                productUpdate.getBalance(),
                "0",
                PaymentServiceImpl.msgPaymentSuccess);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(url2))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(productUpdate)))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(productUpdate)));

        var paymentResponseGet = paymentService.debit(paymentRequest);

        Assertions.assertEquals(paymentResponse, paymentResponseGet);

        mockRestServiceServer.verify();
    }
}
