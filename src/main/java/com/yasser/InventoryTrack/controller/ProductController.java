package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dto.ProductDto;
import com.yasser.InventoryTrack.enums.UserRole;
import com.yasser.InventoryTrack.service.ProductsService;
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

    private final ProductsService productsService;
    private final SecurityContext securityContext;

    public ProductController(ProductsService productsService, SecurityContext securityContext) {

        this.productsService = productsService;
        this.securityContext = securityContext;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable int id) throws ResponseStatusException {
        return productsService.getProductById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProduct(@RequestParam(required = false) String query) {

        if (query != null && !query.isEmpty()) {
            return productsService.getProductsByName(query.toLowerCase());
        } else {
            return productsService.getProducts();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto addProduct(@RequestBody @Valid ProductDto product) {
        return productsService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) throws ResponseStatusException{
        List<String> roles = this.securityContext.getRoles();

        if (roles.contains(UserRole.OWNER.getValue())) {
            productsService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
