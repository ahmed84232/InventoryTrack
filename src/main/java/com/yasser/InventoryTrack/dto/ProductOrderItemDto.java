package com.yasser.InventoryTrack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderItemDto {

    @NotNull(message = "")
    private Integer id;

    private ProductDto product;

    @Min(value = 1, message = "quantity must be at least 1.")
    private Integer quantity;
}
