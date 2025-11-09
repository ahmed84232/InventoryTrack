package com.yasser.InventoryTrack.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yasser.InventoryTrack.dao.ProductDao;
import com.yasser.InventoryTrack.dto.ProductDto;
import com.yasser.InventoryTrack.entity.Product;
import com.yasser.InventoryTrack.util.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private final ProductDao productDAO;
    private final SecurityContext securityContext;

    public ProductsService(ProductDao productDAO, SecurityContext securityContext) {
        this.productDAO = productDAO;
        this.securityContext = securityContext;
    }

    // PRODUCTS METHODS
    public List<ProductDto> getProducts() {

        return productDAO.findAll().stream().map(this::productToProductDto).toList();
    }

    // PRODUCTS METHODS
    public ProductDto getProductById(Integer id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        return productToProductDto(product);

    }

    public List<ProductDto> getProductsByName(String name) {
        return productDAO.findByNameIgnoreCaseContaining(name).stream().map(this::productToProductDto).collect(Collectors.toList());
    }

    // PRODUCTS METHODS
    public ProductDto addProduct(ProductDto productDTO) {

        Product theProduct = new Product();

        theProduct.setName(productDTO.getName());
        theProduct.setPrice(productDTO.getPrice());
        theProduct.setDescription(productDTO.getDescription());
        theProduct.setStockQuantity(productDTO.getQuantity());

        Product product = productDAO.save(theProduct);

        return productToProductDto(product);
    }

    // PRODUCTS METHODS
    public void deleteProduct(Integer id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        productDAO.delete(product);

    }

    private ProductDto productToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setQuantity(product.getStockQuantity());

        return productDto;
    }

    private Product patch(Map<String, Object> patchPayload,
                         int id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));


        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee ID is not allowed to be changed " + id);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.updateValue(product, patchPayload);

        } catch (JsonMappingException e) {

            throw new RuntimeException("Failed to apply patch", e);
        }

        return product;
    }

    public ProductDto applyPatch(int id, Map<String, Object> patchPayload) {

        Product patchedProduct = patch(patchPayload, id);
        Product product = productDAO.save(patchedProduct);

        return productToProductDto(product);
    };

}
