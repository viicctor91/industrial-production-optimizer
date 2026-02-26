package com.example.optimizer.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductResponse {
    private UUID id;
    private String code;
    private String name;
    private BigDecimal price;
    private List<ProductCompositionItemResponse> composition;
}
