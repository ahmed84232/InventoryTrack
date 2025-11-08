package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dto.ProductsDTO;
import com.yasser.InventoryTrack.service.ServiceRepository;
import com.yasser.InventoryTrack.util.SecurityContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ServiceRepository serviceRepository;
    private final SecurityContext securityContext;

    public ProductController(ServiceRepository serviceRepository, SecurityContext securityContext) {

        this.serviceRepository = serviceRepository;
        this.securityContext = securityContext;
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
    public ResponseEntity<?> deleteProduct(@PathVariable int id) throws ResponseStatusException{
        List<String> roles = this.securityContext.getRoles();
        if (roles.contains("owner")) {
            serviceRepository.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
