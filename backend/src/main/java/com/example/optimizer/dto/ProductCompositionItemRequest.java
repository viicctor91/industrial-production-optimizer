package com.example.optimizer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductCompositionItemRequest {
    @NotNull
    private UUID rawMaterialId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal quantityRequiredPerUnit;
}
