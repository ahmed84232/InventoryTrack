package com.yasser.service;

import com.yasser.dao.OrderDao;
import com.yasser.dao.OrderItemDao;
import com.yasser.dao.ProductDao;
import com.yasser.dto.ProductOrderDto;
import com.yasser.dto.ProductOrderItemDto;
import com.yasser.entity.ProductOrder;
import com.yasser.entity.ProductOrderItem;
import com.yasser.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yasser.dto.ProductDto.productToProductDto;
import static com.yasser.utils.AuthUtils.getUserId;
import static com.yasser.utils.AuthUtils.getUserName;

@Service
public class OrdersService {

    private final OrderDao orderDAO;
    private final OrderItemDao orderItemDAO;
    private final ProductDao productDAO;

    public OrdersService(OrderDao orderDAO, OrderItemDao orderItemDAO, ProductDao productDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.productDAO = productDAO;
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
        String userId = getUserId();
        String userName = getUserName();

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
                        return ProductOrderItemDto.builder()
                            .id(productOrderItem.getId())
                            .product(productToProductDto(productOrderItem.getProduct()))
                            .quantity(productOrderItem.getQuantity())
                            .build();
                    }
                )
                .collect(Collectors.toList())
        );

        productOrderDto.setTotalPrice(productOrder.getTotalPrice());
        productOrderDto.setCreatedAt(productOrder.getCreatedAt());

        return productOrderDto;
    }
}
