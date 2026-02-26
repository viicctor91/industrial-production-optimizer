package com.example.optimizer.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductionSuggestionResponse {
    private List<SuggestedItem> suggestedItems;
    private BigDecimal totalValue;
    private List<RawMaterialResponse> remainingStock;

    @Data
    @Builder
    public static class SuggestedItem {
        private UUID productId;
        private String productCode;
        private String productName;
        private Integer unitsToProduce;
        private BigDecimal unitPrice;
        private BigDecimal totalValue;
    }
}
