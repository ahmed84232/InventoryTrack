package com.yasser.controller;

import com.yasser.dto.ProductDto;
import com.yasser.service.ProductsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductsService productsService;

    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
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
    @PreAuthorize("hasRole(T(com.yasser.enums.UserRole).DATA_ENTRY.getValue())")
    public ProductDto addProduct(@RequestBody @Valid ProductDto product) {
        return productsService.addProduct(product);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.yasser.enums.UserRole).DATA_ENTRY.getValue())")
    public ProductDto updateProduct(@PathVariable int id, @RequestBody Map<String, Object> patchPayload) {
        return productsService.patch(patchPayload, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(com.yasser.enums.UserRole).DATA_ENTRY.getValue())")
    public ProductDto deleteProduct(@PathVariable int id) throws ResponseStatusException{
        return productsService.deleteProduct(id);
    }

}
