package com.yasser.InventoryTrack.service;

import com.yasser.InventoryTrack.dao.ProductDao;
import com.yasser.InventoryTrack.dto.ProductDto;
import com.yasser.InventoryTrack.entity.Product;
import com.yasser.InventoryTrack.util.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
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

        List<Product> products = productDAO.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product productDetails : products) {
            ProductDto dto = new ProductDto();

            dto.setId(productDetails.getId());
            dto.setName(productDetails.getName());
            dto.setPrice(productDetails.getPrice());
            dto.setDescription(productDetails.getDescription());
            dto.setQuantity(productDetails.getStockQuantity());
            productDtos.add(dto);
        }
        return productDtos;
    }

    // PRODUCTS METHODS
    public ProductDto getProductById(Integer id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getStockQuantity());

        return dto;

    }

    public List<ProductDto> getProductsByName(String name) {
        return productDAO.findByNameContaining(name).stream().map(this::productToProductDto).collect(Collectors.toList());
    }

    // PRODUCTS METHODS
    public ProductDto addProduct(ProductDto productDTO) {

        Product theProduct = new Product();

        theProduct.setName(productDTO.getName());
        theProduct.setDescription(productDTO.getDescription());
        theProduct.setPrice(productDTO.getPrice());
        theProduct.setStockQuantity(productDTO.getQuantity());

        Product savedProduct = productDAO.save(theProduct);

        ProductDto dto = new ProductDto();

        dto.setName(savedProduct.getName());
        dto.setId(savedProduct.getId());
        dto.setDescription(savedProduct.getDescription());
        dto.setPrice(savedProduct.getPrice());
        dto.setQuantity(savedProduct.getStockQuantity());


        return dto;
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
        productDto.setDescription(product.getDescription());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getStockQuantity());

        return productDto;
    }

}
