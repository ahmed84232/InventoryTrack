package com.yasser.InventoryTrack.service;

import com.yasser.InventoryTrack.dao.OrderDao;
import com.yasser.InventoryTrack.dao.OrderItemDao;
import com.yasser.InventoryTrack.dao.ProductDao;
import com.yasser.InventoryTrack.dto.ProductDto;
import com.yasser.InventoryTrack.dto.ProductOrderDto;
import com.yasser.InventoryTrack.dto.ProductOrderItemDto;
import com.yasser.InventoryTrack.entity.ProductOrder;
import com.yasser.InventoryTrack.entity.ProductOrderItem;
import com.yasser.InventoryTrack.entity.Product;
import com.yasser.InventoryTrack.util.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrderDao orderDAO;
    private final OrderItemDao orderItemDAO;
    private final ProductDao productDAO;
    private final SecurityContext securityContext;

    public OrdersService(OrderDao orderDAO, OrderItemDao orderItemDAO, ProductDao productDAO, SecurityContext securityContext) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.productDAO = productDAO;
        this.securityContext = securityContext;
    }

    // ORDER METHODS
    public List<ProductOrderDto> getOrders() {
        List<ProductOrder> productOrders = orderDAO.findAll();
        List<ProductOrderDto> productOrderDtoList = new ArrayList<>();

        for (ProductOrder productOrder : productOrders)
            productOrderDtoList.add(orderToOrderDto(productOrder));

        return productOrderDtoList;
    }

    // ORDER METHODS
    public ProductOrderDto getOrderById(Integer id) {
        ProductOrder productOrder = orderDAO
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return orderToOrderDto(productOrder);

    }

    public ProductOrderDto addOrder(ProductOrderDto productOrderDTO) {
        String userId = this.securityContext.getUserId();
        String userName = this.securityContext.getUserName();

        List<ProductOrderItemDto> productOrderItemDtoList = productOrderDTO.getProductOrderItems();

        // adding products to list
        List<ProductOrderItem> productOrderItemList = new ArrayList<>();
        for (ProductOrderItemDto orderItemDTOProduct : productOrderItemDtoList) {
            ProductOrderItem productOrderItem = new ProductOrderItem();
            productOrderItem.setQuantity(orderItemDTOProduct.getQuantity());

            Product product = productDAO.findById(orderItemDTOProduct.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            productOrderItem.setProduct(product);
            productOrderItem.setPrice(product.getPrice() * productOrderItem.getQuantity());
            productOrderItem.setName(product.getName());

            // decreasing the amount of quantity in db
            product.setStockQuantity(product.getStockQuantity() - productOrderItem.getQuantity());
            productDAO.save(product);

            productOrderItemList.add(productOrderItem);
        }

        // Creating Order
        ProductOrder productOrder = new ProductOrder();
        productOrder.setUserId(userId);
        productOrder.setUserName(userName);
        productOrder.setProductOrderItems(productOrderItemList);

        Double totalPrice = productOrderItemList.stream().mapToDouble(ProductOrderItem::getPrice).sum();
        productOrder.setTotalPrice(totalPrice);

        ProductOrder savedProductOrder = orderDAO.save(productOrder);

        for (ProductOrderItem item : productOrderItemList) {
            item.setProductOrder(productOrder);
            orderItemDAO.save(item);
        }

        return orderToOrderDto(savedProductOrder);

    }

    // ORDER METHODS
    public void deleteOrder(Integer id) {
        ProductOrder productOrder = orderDAO
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order id Not Found"));

        orderDAO.delete(productOrder);
    }

    private ProductOrderDto orderToOrderDto(ProductOrder productOrder) {
        ProductOrderDto productOrderDto = new ProductOrderDto();
        productOrderDto.setId(productOrder.getId());
        productOrderDto.setUserId(productOrder.getUserId());
        productOrderDto.setUserName(productOrder.getUserName());

        productOrderDto.setProductOrderItems(
            productOrder.getProductOrderItems()
                .stream()
                .map(
                    (ProductOrderItem productOrderItem) -> {
                        ProductOrderItemDto productOrderItemDto = ProductOrderItemDto.builder()
                            .id(productOrderItem.getId())
                            .product(productToProductDto(productOrderItem.getProduct()))
                            .quantity(productOrderItem.getQuantity())
                            .build();
                        return productOrderItemDto;
                    }
                )
                .collect(Collectors.toList())
        );

        productOrderDto.setTotalPrice(productOrder.getTotalPrice());

        return productOrderDto;
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
