package com.example.optimizer.mapper;

import com.example.optimizer.dto.ProductCompositionItemResponse;
import com.example.optimizer.dto.ProductResponse;
import com.example.optimizer.dto.RawMaterialResponse;
import com.example.optimizer.entity.ProductEntity;
import com.example.optimizer.entity.ProductMaterialEntity;
import com.example.optimizer.entity.RawMaterialEntity;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Mapper {

    public RawMaterialResponse toDto(RawMaterialEntity entity) {
        if (entity == null) return null;
        RawMaterialResponse dto = new RawMaterialResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setUnit(entity.getUnit());
        dto.setQuantityInStock(entity.getQuantityInStock());
        return dto;
    }

    public ProductResponse toDto(ProductEntity entity) {
        if (entity == null) return null;
        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        
        List<ProductCompositionItemResponse> composition = entity.getComposition() == null 
            ? Collections.emptyList() 
            : entity.getComposition().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        dto.setComposition(composition);
        return dto;
    }

    public ProductCompositionItemResponse toDto(ProductMaterialEntity entity) {
        if (entity == null) return null;
        ProductCompositionItemResponse dto = new ProductCompositionItemResponse();
        dto.setId(entity.getId());
        dto.setRawMaterialId(entity.getRawMaterial().getId());
        dto.setRawMaterialName(entity.getRawMaterial().getName());
        dto.setRawMaterialUnit(entity.getRawMaterial().getUnit());
        dto.setQuantityRequiredPerUnit(entity.getQuantityRequiredPerUnit());
        return dto;
    }
}
