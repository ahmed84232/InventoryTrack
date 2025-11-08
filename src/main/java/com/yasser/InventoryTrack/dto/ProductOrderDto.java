package com.yasser.InventoryTrack.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDto {
    private Integer id;

    private String userId;
    private String userName;
    private Double totalPrice;

    @NotEmpty(message = "At least one product is required.")
    private List<ProductOrderItemDto> productOrderItems;

}
