package com.example.optimizer.repository;

import com.example.optimizer.entity.ProductMaterialEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMaterialRepository implements PanacheRepositoryBase<ProductMaterialEntity, java.util.UUID> {
}
