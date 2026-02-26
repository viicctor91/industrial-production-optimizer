package com.example.optimizer.service;

import com.example.optimizer.dto.ProductionSuggestionResponse;
import com.example.optimizer.dto.ProductionSuggestionResponse.SuggestedItem;
import com.example.optimizer.dto.RawMaterialResponse;
import com.example.optimizer.entity.ProductEntity;
import com.example.optimizer.entity.ProductMaterialEntity;
import com.example.optimizer.entity.RawMaterialEntity;
import com.example.optimizer.mapper.Mapper;
import com.example.optimizer.repository.ProductRepository;
import com.example.optimizer.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductionPlannerService {

    @Inject
    ProductRepository productRepository;
    @Inject
    RawMaterialRepository rawMaterialRepository;
    @Inject
    Mapper mapper;

    /**
     * Generates a production plan to maximize total sales value.
     * <p>
     * Algorithm (Greedy Strategy):
     * 1. Sort products by Unit Price (DESC) -> Higher return first.
     * 2. Tie-breakers: Composition size (ASC), Product Code (ASC).
     * 3. Iteratively allocate production based on remaining stock.
     */
    @Transactional
    public ProductionSuggestionResponse suggestPlan() {
        List<ProductEntity> products = productRepository.listAll();
        List<RawMaterialEntity> rawMaterials = rawMaterialRepository.listAll();

        // Map stock by RawMaterial ID for quick access and updates
        Map<UUID, BigDecimal> currentStock = new HashMap<>();
        for (RawMaterialEntity rm : rawMaterials) {
            currentStock.put(rm.getId(), rm.getQuantityInStock());
        }

        // 1. Sort products
        List<ProductEntity> sortedProducts = products.stream()
                .sorted(Comparator.comparing(ProductEntity::getPrice).reversed() // Higher price first
                        .thenComparingInt(p -> p.getComposition().size())        // Simpler composition first
                        .thenComparing(ProductEntity::getCode))                  // Deterministic order
                .collect(Collectors.toList());

        List<SuggestedItem> suggestedItems = new ArrayList<>();
        BigDecimal totalSalesValue = BigDecimal.ZERO;

        // 2. Iterate and allocate
        for (ProductEntity product : sortedProducts) {
            if (product.getComposition().isEmpty()) continue;

            // Calculate max units we can produce with CURRENT stock
            int maxUnits = Integer.MAX_VALUE;

            for (ProductMaterialEntity pm : product.getComposition()) {
                BigDecimal available = currentStock.getOrDefault(pm.getRawMaterial().getId(), BigDecimal.ZERO);
                BigDecimal required = pm.getQuantityRequiredPerUnit();

                if (required.compareTo(BigDecimal.ZERO) <= 0) continue; // Should be validated elsewhere

                // floor(available / required)
                int possible = available.divide(required, 0, RoundingMode.FLOOR).intValue();
                maxUnits = Math.min(maxUnits, possible);
            }

            if (maxUnits > 0) {
                // "Produce" the items
                BigDecimal units = BigDecimal.valueOf(maxUnits);
                BigDecimal value = product.getPrice().multiply(units);
                
                totalSalesValue = totalSalesValue.add(value);

                suggestedItems.add(SuggestedItem.builder()
                        .productId(product.getId())
                        .productCode(product.getCode())
                        .productName(product.getName())
                        .unitsToProduce(maxUnits)
                        .unitPrice(product.getPrice())
                        .totalValue(value)
                        .build());

                // Deduct from stock
                for (ProductMaterialEntity pm : product.getComposition()) {
                    BigDecimal consumed = pm.getQuantityRequiredPerUnit().multiply(units);
                    UUID rmId = pm.getRawMaterial().getId();
                    currentStock.put(rmId, currentStock.get(rmId).subtract(consumed));
                }
            }
        }

        // Prepare remaining stock response
        List<RawMaterialResponse> remainingStockList = rawMaterials.stream()
                .map(rm -> {
                    RawMaterialResponse dto = mapper.toDto(rm);
                    dto.setQuantityInStock(currentStock.get(rm.getId()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ProductionSuggestionResponse.builder()
                .suggestedItems(suggestedItems)
                .totalValue(totalSalesValue)
                .remainingStock(remainingStockList)
                .build();
    }
}
