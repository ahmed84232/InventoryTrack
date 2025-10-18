package com.yasser.InventoryTrack.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDTO {

    private Integer orderId;
    private Integer userId;
    private String user;
    private Map<String, Integer> productNames;
    private Double totalPrice;

}
