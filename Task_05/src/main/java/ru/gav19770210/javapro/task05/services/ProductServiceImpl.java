package ru.gav19770210.javapro.task05.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task05.repositories.ProductRepo;
import ru.gav19770210.javapro.task05.repositories.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {
    public static final String msgProductNotFoundById = "Не найден продукт с id = %d";
    public static final String msgProductExistByAccNum = "Уже существует продукт с account_number = %s";
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, UserRepo userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        var productEntities = new ArrayList<ProductEntity>();
        productRepo.findAll().forEach(productEntities::add);
        return productEntities;
    }

    @Override
    public List<ProductEntity> getUserProducts(Long userId) {
        return productRepo.getAllByUserId(userId);
    }

    @Override
    public ProductEntity getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(msgProductNotFoundById, id)));
    }

    @Override
    @Transactional
    public ProductEntity createProduct(ProductEntity productEntity) {
        if (userRepo.findById(productEntity.getUserId()).isEmpty()) {
            throw new NoSuchElementException(String.format(UserServiceImpl.msgUserNotFoundById, productEntity.getUserId()));
        }
        if (productRepo.findByAccountNumber(productEntity.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException(String.format(msgProductExistByAccNum, productEntity.getAccountNumber()));
        }
        return productRepo.save(productEntity);
    }

    @Override
    @Transactional
    public ProductEntity updateProduct(ProductEntity productEntity) {
        var productById = productRepo.findById(productEntity.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format(msgProductNotFoundById, productEntity.getId())));

        if (userRepo.findById(productEntity.getUserId()).isEmpty()) {
            throw new NoSuchElementException(String.format(UserServiceImpl.msgUserNotFoundById, productEntity.getUserId()));
        }
        var productByAccount = productRepo.findByAccountNumber(productEntity.getAccountNumber()).orElse(null);
        if (productByAccount != null && !productByAccount.getId().equals(productEntity.getId())) {
            throw new IllegalArgumentException(String.format(msgProductExistByAccNum, productEntity.getAccountNumber()));
        }
        if (!productById.equals(productEntity)) {
            return productRepo.save(productEntity);
        }
        return productById;
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        if (productRepo.findById(id).isEmpty()) {
            throw new NoSuchElementException(String.format(msgProductNotFoundById, id));
        }
        productRepo.deleteById(id);
    }
}
