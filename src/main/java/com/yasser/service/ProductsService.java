package com.yasser.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yasser.dao.ProductDao;
import com.yasser.dto.ProductDto;
import com.yasser.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yasser.dto.ProductDto.productDtoToProduct;
import static com.yasser.dto.ProductDto.productToProductDto;

@Service
public class ProductsService {
    private final ProductDao productDAO;

    public ProductsService(ProductDao productDAO) {
        this.productDAO = productDAO;
    }

    // PRODUCTS METHODS
    public List<ProductDto> getProducts() {

        return productDAO.findAll().stream().map(ProductDto::productToProductDto).toList();
    }

    // PRODUCTS METHODS
    public ProductDto getProductById(Integer id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        return productToProductDto(product);

    }

    public List<ProductDto> getProductsByName(String name) {
        return productDAO.findByNameIgnoreCaseContaining(name).stream().map(ProductDto::productToProductDto).collect(Collectors.toList());
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
    public ProductDto deleteProduct(Integer id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        productDAO.delete(product);
        return productToProductDto(product);

    }


    public ProductDto patch(Map<String, Object> patchPayload,
                         int id) {

        Product product = productDAO.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));


        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee ID is not allowed to be changed " + id);
        }

        ProductDto dto = productToProductDto(product);


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.updateValue(dto, patchPayload);

        } catch (JsonMappingException e) {

            throw new RuntimeException("Failed to apply patch", e);
        }

        Product patchedProduct = productDtoToProduct(dto);
        productDAO.save(patchedProduct);

        return dto;
    }

}
