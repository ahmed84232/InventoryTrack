package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dto.ProductsDTO;
import com.yasser.InventoryTrack.service.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ServiceRepository serviceRepository;

    public ProductController(ServiceRepository serviceRepository) {

        this.serviceRepository = serviceRepository;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductsDTO getProduct(@PathVariable int id) throws ResponseStatusException {
        return serviceRepository.getProductById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductsDTO> getAllProduct() {
        return serviceRepository.getProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductsDTO addProduct(@RequestBody @Valid ProductsDTO product) {
        return serviceRepository.addProduct(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) throws ResponseStatusException{
        serviceRepository.deleteProduct(id);
    }

}
