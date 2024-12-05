package ru.gav19770210.javapro.task06.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task06.dto.PaymentRequest;
import ru.gav19770210.javapro.task06.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final RestClient restClient;
    private final String productBaseUrl;
    private final String productGetByIdUri;
    private final String productGetByUserUri;
    private final String productUpdateUri;

    public PaymentServiceImpl(@Value("${service-url.product.base-url}") String productBaseUrl,
                              @Value("${service-url.product.get-by-user}") String productGetByUserUri,
                              @Value("${service-url.product.get-by-id}") String productGetByIdUri,
                              @Value("${service-url.product.update}") String productUpdateUri) {
        this.productBaseUrl = productBaseUrl;
        this.productGetByIdUri = productGetByIdUri;
        this.productGetByUserUri = productGetByUserUri;
        this.productUpdateUri = productUpdateUri;
        this.restClient = RestClient.builder().baseUrl(this.productBaseUrl).build();
    }

    @Override
    public List<ProductEntity> getUserProducts(Long userId) {
        var url = productGetByUserUri.replace("{user_id}", userId.toString());
        var response = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(List.class);

        return response;
    }

    @Override
    public ProductEntity getProductById(Long id) {
        var url = productGetByIdUri.replace("{id}", id.toString());
        var response = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(ProductEntity.class);

        return response.getBody();
    }

    @Override
    public void availableDebit(ProductEntity productEntity, BigDecimal summa) {
        if (productEntity.getBalance().subtract(summa).compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счёте для списания");
        }
    }

    private ProductEntity updateProduct(ProductEntity productEntity) {
        var response = restClient
                .post()
                .uri(productUpdateUri)
                .body(productEntity)
                .retrieve()
                .toEntity(ProductEntity.class);

        return response.getBody();
    }

    @Override
    public PaymentResponse debit(PaymentRequest paymentRequest) {
        var product = getProductById(paymentRequest.getProductId());
        availableDebit(product, paymentRequest.getSumma());
        product.setBalance(product.getBalance().subtract(paymentRequest.getSumma()));
        var productUpdate = updateProduct(product);

        return new PaymentResponse("0", "Платёж выполнен успешно");
    }

    @Override
    public PaymentResponse credit(PaymentRequest paymentRequest) {
        var product = getProductById(paymentRequest.getProductId());
        product.setBalance(product.getBalance().add(paymentRequest.getSumma()));
        var productUpdate = updateProduct(product);

        return new PaymentResponse("0", "Платёж выполнен успешно");
    }
}
