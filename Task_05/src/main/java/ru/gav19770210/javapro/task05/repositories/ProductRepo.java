package ru.gav19770210.javapro.task05.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gav19770210.javapro.task05.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends CrudRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByAccountNumber(String accountNumber);

    List<ProductEntity> getAllByUserId(Long id);
}