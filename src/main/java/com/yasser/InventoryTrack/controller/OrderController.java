package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dto.ProductOrderDto;
import com.yasser.InventoryTrack.enums.UserRole;
import com.yasser.InventoryTrack.service.OrdersService;
import com.yasser.InventoryTrack.util.SecurityContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrdersService ordersService;
    private final SecurityContext securityContext;

    public OrderController(OrdersService ordersService, SecurityContext securityContext) {
        this.ordersService = ordersService;
        this.securityContext = securityContext;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductOrderDto> getOrders() {
        return ordersService.getOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductOrderDto getOrder(@PathVariable int id) throws ResponseStatusException {
        List<String> roles = this.securityContext.getRoles();

        if (roles.contains(UserRole.EMPLOYEE.getValue())) {
            return ordersService.getOrderById(id);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductOrderDto addOrder(@RequestBody @Valid ProductOrderDto productOrderDTO) throws ResponseStatusException {
        List<String> roles = this.securityContext.getRoles();

        if (roles.contains(UserRole.EMPLOYEE.getValue())) {
            return ordersService.addOrder(productOrderDTO);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable int id) throws ResponseStatusException {
        List<String> roles = this.securityContext.getRoles();

        if (roles.contains(UserRole.EMPLOYEE.getValue())) {
            ordersService.deleteOrder(id);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
