package com.yasser.InventoryTrack.dto.order;

import com.yasser.InventoryTrack.dto.orderitem.OrderProductDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {

    @NotNull(message = "User id is required.")
    private Integer userId;

    @NotEmpty(message = "At least one product is required.")
    private List<OrderProductDTO> products;
}
