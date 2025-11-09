package com.yasser.controller;

import com.yasser.dto.ProductOrderDto;
import com.yasser.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrdersService ordersService;

    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductOrderDto> getOrders() {
        return ordersService.getOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductOrderDto getOrder(@PathVariable int id) throws ResponseStatusException {
        return ordersService.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(com.yasser.enums.UserRole).EMPLOYEE.getValue())")
    public ProductOrderDto addOrder(@RequestBody @Valid ProductOrderDto productOrderDTO) throws ResponseStatusException {
        return ordersService.addOrder(productOrderDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(com.yasser.enums.UserRole).EMPLOYEE.getValue())")
    public void deleteOrder(@PathVariable int id) throws ResponseStatusException {
        ordersService.deleteOrder(id);
    }
}
