package ru.gav19770210.javapro.task05.services;

import ru.gav19770210.javapro.task05.entities.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> getAllProducts();

    List<ProductEntity> getUserProducts(Long userId);

    ProductEntity getProductById(Long id);

    ProductEntity createProduct(ProductEntity productEntity);

    ProductEntity updateProduct(ProductEntity productEntity);

    void deleteProductById(Long id);
}
