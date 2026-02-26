package com.example.optimizer.service;

import com.example.optimizer.dto.ProductRequest;
import com.example.optimizer.dto.ProductResponse;
import com.example.optimizer.entity.ProductEntity;
import com.example.optimizer.entity.ProductMaterialEntity;
import com.example.optimizer.entity.RawMaterialEntity;
import com.example.optimizer.exception.DuplicateResourceException;
import com.example.optimizer.exception.ResourceNotFoundException;
import com.example.optimizer.mapper.Mapper;
import com.example.optimizer.repository.ProductRepository;
import com.example.optimizer.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;
    @Inject
    RawMaterialRepository rawMaterialRepository;
    @Inject
    Mapper mapper;

    @Transactional
    public List<ProductResponse> findAll() {
        return productRepository.listAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse findById(UUID id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return mapper.toDto(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Product with code " + request.getCode() + " already exists.");
        }

        ProductEntity product = ProductEntity.builder()
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .build();

        if (request.getComposition() != null) {
            Set<UUID> rawMaterialIds = new HashSet<>();
            List<ProductMaterialEntity> composition = new ArrayList<>();

            for (var item : request.getComposition()) {
                if (!rawMaterialIds.add(item.getRawMaterialId())) {
                    throw new IllegalArgumentException("Duplicate raw material in composition: " + item.getRawMaterialId());
                }

                RawMaterialEntity rawMaterial = rawMaterialRepository.findById(item.getRawMaterialId());
                if (rawMaterial == null) {
                    throw new ResourceNotFoundException("Raw Material not found with id: " + item.getRawMaterialId());
                }

                ProductMaterialEntity materialEntity = ProductMaterialEntity.builder()
                        .product(product)
                        .rawMaterial(rawMaterial)
                        .quantityRequiredPerUnit(item.getQuantityRequiredPerUnit())
                        .build();
                
                composition.add(materialEntity);
            }
            product.setComposition(composition);
        }

        productRepository.persist(product);
        return mapper.toDto(product);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        if (!product.getCode().equals(request.getCode()) && productRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Product with code " + request.getCode() + " already exists.");
        }

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        product.getComposition().clear();

        if (request.getComposition() != null) {
            Set<UUID> rawMaterialIds = new HashSet<>();
            
            for (var item : request.getComposition()) {
                if (!rawMaterialIds.add(item.getRawMaterialId())) {
                    throw new IllegalArgumentException("Duplicate raw material in composition: " + item.getRawMaterialId());
                }

                RawMaterialEntity rawMaterial = rawMaterialRepository.findById(item.getRawMaterialId());
                if (rawMaterial == null) {
                    throw new ResourceNotFoundException("Raw Material not found with id: " + item.getRawMaterialId());
                }

                ProductMaterialEntity materialEntity = ProductMaterialEntity.builder()
                        .product(product)
                        .rawMaterial(rawMaterial)
                        .quantityRequiredPerUnit(item.getQuantityRequiredPerUnit())
                        .build();

                product.getComposition().add(materialEntity);
            }
        }

        return mapper.toDto(product);
    }

    @Transactional
    public void delete(UUID id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.delete(product);
    }
}
