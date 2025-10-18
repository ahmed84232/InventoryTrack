package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dto.order.OrderCreateDTO;
import com.yasser.InventoryTrack.dto.order.OrderResponseDTO;
import com.yasser.InventoryTrack.service.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ServiceRepository serviceRepository;

    public OrderController(ServiceRepository serviceRepository) {

        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDTO> getOrders() {
        return serviceRepository.getOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDTO getOrder(@PathVariable int id) throws ResponseStatusException {
        return serviceRepository.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO addOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO) throws ResponseStatusException {
        return serviceRepository.addOrder(orderCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable int id) throws ResponseStatusException {
        serviceRepository.deleteOrder(id);
    }
}
