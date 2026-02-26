package com.example.optimizer.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductCompositionItemResponse {
    private UUID id;
    private UUID rawMaterialId;
    private String rawMaterialName;
    private String rawMaterialUnit;
    private BigDecimal quantityRequiredPerUnit;
}
