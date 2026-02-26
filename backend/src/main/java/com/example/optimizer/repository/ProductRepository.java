package com.example.optimizer.repository;

import com.example.optimizer.entity.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, java.util.UUID> {
    public boolean existsByCode(String code) {
        return find("code", code).firstResultOptional().isPresent();
    }
}
