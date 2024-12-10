package ru.gav19770210.javapro.task06.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task06.dto.PaymentRequest;
import ru.gav19770210.javapro.task06.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    public static String msgPaymentSuccess = "Платёж выполнен успешно";
    @Getter
    public final String productBaseUrl;
    @Getter
    public final String productGetByIdUri;
    @Getter
    public final String productGetByUserUri;
    @Getter
    public final String productUpdateUri;
    private final RestTemplate restTemplate;

    @Autowired
    public PaymentServiceImpl(
            RestTemplate restTemplate,
            @Value("${service-url.product.base-url}") String productBaseUrl,
            @Value("${service-url.product.get-by-user}") String productGetByUserUri,
            @Value("${service-url.product.get-by-id}") String productGetByIdUri,
            @Value("${service-url.product.update}") String productUpdateUri) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
        this.productGetByIdUri = productGetByIdUri;
        this.productGetByUserUri = productGetByUserUri;
        this.productUpdateUri = productUpdateUri;
    }

    @Override
    public List<ProductEntity> getUserProducts(Long userId) {
        var url = productBaseUrl + productGetByUserUri.replace("{user_id}", userId.toString());
        var response = restTemplate.getForEntity(url, ProductEntity[].class).getBody();

        assert response != null;
        return Arrays.stream(response).toList();
    }

    @Override
    public ProductEntity getProductById(Long id) {
        var url = productBaseUrl + productGetByIdUri.replace("{id}", id.toString());
        var response = restTemplate.getForEntity(url, ProductEntity.class);

        return response.getBody();
    }

    @Override
    public void availableDebit(ProductEntity productEntity, BigDecimal summa) {
        if (productEntity.getBalance().subtract(summa).compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счёте для списания");
        }
    }

    private ProductEntity updateProduct(ProductEntity productEntity) {
        var url = productBaseUrl + productUpdateUri;
        var response = restTemplate.postForEntity(url, productEntity, ProductEntity.class);

        return response.getBody();
    }

    @Override
    public PaymentResponse debit(PaymentRequest paymentRequest) {
        var product = getProductById(paymentRequest.getProductId());
        availableDebit(product, paymentRequest.getSumma());
        product.setBalance(product.getBalance().subtract(paymentRequest.getSumma()));
        var productUpdate = updateProduct(product);

        return new PaymentResponse(productUpdate.getId(), productUpdate.getBalance(), "0", msgPaymentSuccess);
    }

    @Override
    public PaymentResponse credit(PaymentRequest paymentRequest) {
        var product = getProductById(paymentRequest.getProductId());
        product.setBalance(product.getBalance().add(paymentRequest.getSumma()));
        var productUpdate = updateProduct(product);

        return new PaymentResponse(productUpdate.getId(), productUpdate.getBalance(), "0", msgPaymentSuccess);
    }
}
