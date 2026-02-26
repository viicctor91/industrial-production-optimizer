package com.example.optimizer.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class RawMaterialResponse {
    private UUID id;
    private String code;
    private String name;
    private String unit;
    private BigDecimal quantityInStock;
}
