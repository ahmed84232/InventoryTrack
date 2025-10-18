package com.yasser.InventoryTrack.dto.orderitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {

    @NotNull(message = "at lease one product id is required.")
    private Integer productId;

    @Min(value = 1, message = "quantity must be at least 1.")
    private Integer quantity;
}
