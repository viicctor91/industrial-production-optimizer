package com.example.optimizer.service;

import com.example.optimizer.dto.RawMaterialRequest;
import com.example.optimizer.dto.RawMaterialResponse;
import com.example.optimizer.entity.RawMaterialEntity;
import com.example.optimizer.exception.DuplicateResourceException;
import com.example.optimizer.exception.ResourceNotFoundException;
import com.example.optimizer.mapper.Mapper;
import com.example.optimizer.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;
    @Inject
    Mapper mapper;

    @Transactional
    public List<RawMaterialResponse> findAll() {
        return repository.listAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RawMaterialResponse findById(UUID id) {
        RawMaterialEntity entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw Material not found with id: " + id);
        }
        return mapper.toDto(entity);
    }

    @Transactional
    public RawMaterialResponse create(RawMaterialRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Raw Material with code " + request.getCode() + " already exists.");
        }

        RawMaterialEntity entity = RawMaterialEntity.builder()
                .code(request.getCode())
                .name(request.getName())
                .unit(request.getUnit())
                .quantityInStock(request.getQuantityInStock())
                .build();

        repository.persist(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public RawMaterialResponse update(UUID id, RawMaterialRequest request) {
        RawMaterialEntity entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw Material not found with id: " + id);
        }

        if (!entity.getCode().equals(request.getCode()) && repository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Raw Material with code " + request.getCode() + " already exists.");
        }

        entity.setCode(request.getCode());
        entity.setName(request.getName());
        entity.setUnit(request.getUnit());
        entity.setQuantityInStock(request.getQuantityInStock());
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(UUID id) {
        RawMaterialEntity entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw Material not found with id: " + id);
        }
        repository.delete(entity);
    }
}
