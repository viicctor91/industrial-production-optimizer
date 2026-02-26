package com.example.optimizer.repository;

import com.example.optimizer.entity.RawMaterialEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RawMaterialRepository implements PanacheRepositoryBase<RawMaterialEntity, java.util.UUID> {
    public boolean existsByCode(String code) {
        return find("code", code).firstResultOptional().isPresent();
    }
}
