package com.example.optimizer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class RawMaterialRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String unit;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal quantityInStock;
}
