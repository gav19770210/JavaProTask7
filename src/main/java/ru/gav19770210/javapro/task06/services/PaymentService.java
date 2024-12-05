package ru.gav19770210.javapro.task06.services;

import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task06.dto.PaymentRequest;
import ru.gav19770210.javapro.task06.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    List<ProductEntity> getUserProducts(Long userId);

    ProductEntity getProductById(Long id);

    void availableDebit(ProductEntity productEntity, BigDecimal summa);

    PaymentResponse debit(PaymentRequest paymentRequest);

    PaymentResponse credit(PaymentRequest paymentRequest);
}
