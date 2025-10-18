package com.yasser.InventoryTrack.service;


import com.yasser.InventoryTrack.dao.OrderDAO;
import com.yasser.InventoryTrack.dao.OrderItemDAO;
import com.yasser.InventoryTrack.dao.ProductDAO;
import com.yasser.InventoryTrack.dao.UserDAO;
import com.yasser.InventoryTrack.dto.UserDTO;
import com.yasser.InventoryTrack.dto.order.OrderCreateDTO;
import com.yasser.InventoryTrack.dto.ProductsDTO;
import com.yasser.InventoryTrack.dto.order.OrderResponseDTO;
import com.yasser.InventoryTrack.dto.orderitem.OrderProductDTO;
import com.yasser.InventoryTrack.entity.Order;
import com.yasser.InventoryTrack.entity.OrderItem;
import com.yasser.InventoryTrack.entity.Product;
import com.yasser.InventoryTrack.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceRepository {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final OrderItemDAO orderItemDAO;

    public ServiceRepository(OrderDAO orderDAO, ProductDAO productDAO, UserDAO userDAO, OrderItemDAO orderItemDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.orderItemDAO = orderItemDAO;
    }

    // ORDER METHODS
    public List<OrderResponseDTO> getOrders() {

        List<Order> orders = orderDAO.findAll();
        List<OrderResponseDTO> orderResponseDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setUserId(order.getUser().getId());

            dto.setProductNames(order.getOrderItems()
                    .stream()
                    .collect(Collectors.toMap(
                            OrderItem::getItemName,
                            OrderItem::getQuantity
                    )));

            dto.setTotalPrice(order.getTotalPrice());
            orderResponseDTOs.add(dto);

        }
        return orderResponseDTOs;
    }

    // ORDER METHODS
    public OrderResponseDTO getOrderById(Integer id) {

        Order order = orderDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setUserId(order.getUser().getId());
        dto.setUser(order.getUser().getName());

        dto.setProductNames(order.getOrderItems()
                .stream()
                .collect(Collectors.toMap(
                        OrderItem::getItemName,
                        OrderItem::getQuantity
                )));

        dto.setTotalPrice(order.getTotalPrice());

        return dto;

    }


    // ORDER METHODS
    public OrderResponseDTO addOrder(OrderCreateDTO orderCreateDTO) {

        // Creating User
        User user = userDAO.findById(orderCreateDTO.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Creating OrderedItems
        List<OrderProductDTO> orderedProducts = orderCreateDTO.getProducts();
        List<OrderItem> orderedItems = new ArrayList<>();

        for (OrderProductDTO orderProductDTO : orderedProducts) {

            OrderItem orderItem = new OrderItem();

            orderItem.setQuantity(orderProductDTO.getQuantity());

            Product product = productDAO.findById(orderProductDTO.getProductId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
            orderItem.setItemName(product.getName());


            product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
            productDAO.save(product);


            orderedItems.add(orderItem);

        }

        // Creating Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderedItems);
        Double totalPrice = orderedItems.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderDAO.save(order);

        for (OrderItem item : orderedItems) {
            item.setOrder(order);
            orderItemDAO.save(item);
        }

        // Creating response
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(savedOrder.getId());
        response.setUserId(user.getId());
        response.setUser(user.getName());

        response.setProductNames(savedOrder.getOrderItems()
                .stream()
                .collect(Collectors.toMap(
                OrderItem::getItemName,
                OrderItem::getQuantity
        )));

        response.setTotalPrice(totalPrice);

        return response;

    }

    // ORDER METHODS
    public void deleteOrder(Integer id) {
        Order order = orderDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order id Not Found"));

        orderDAO.delete(order);
    }






    // PRODUCTS METHODS
    public List<ProductsDTO> getProducts() {

        List<Product> products = productDAO.findAll();
        List<ProductsDTO> productsDTOS = new ArrayList<>();

        for (Product productDetails : products) {
            ProductsDTO dto = new ProductsDTO();

            dto.setId(productDetails.getId());
            dto.setName(productDetails.getName());
            dto.setPrice(productDetails.getPrice());
            dto.setDescription(productDetails.getDescription());
            dto.setQuantity(productDetails.getStockQuantity());
            productsDTOS.add(dto);
        }
        return productsDTOS;
    }

    // PRODUCTS METHODS
    public ProductsDTO getProductById(Integer id) {

        Product product = productDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));

        ProductsDTO dto = new ProductsDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getStockQuantity());

        return dto;

    }

    // PRODUCTS METHODS
    public ProductsDTO addProduct(ProductsDTO productsDTO) {

        Product theProduct = new Product();

        theProduct.setName(productsDTO.getName());
        theProduct.setDescription(productsDTO.getDescription());
        theProduct.setPrice(productsDTO.getPrice());
        theProduct.setStockQuantity(productsDTO.getQuantity());

        Product savedProduct = productDAO.save(theProduct);

        ProductsDTO dto = new ProductsDTO();

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





    // USER METHODS
    public List<UserDTO> getUsers() {

        List<User> users = userDAO.findAll();
        List<UserDTO> usersDTOS = new ArrayList<>();
        for (User user : users) {

            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setRole(user.getRole());
            dto.setUserName(user.getUserName());
            usersDTOS.add(dto);
        }
        return usersDTOS;
    }

    // USER METHODS
    public UserDTO getUserById(Integer id) {

        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id Not Found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setRole(user.getRole());
        dto.setUserName(user.getUserName());
        return dto;
    }

    // USER METHODS
    public UserDTO addUser(UserDTO userDTO) {

        User user = new User();

        user.setName(userDTO.getName());
        user.setRole(userDTO.getRole());
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());

        User saveUser = userDAO.save(user);

        UserDTO dto = new UserDTO();

        dto.setId(saveUser.getId());
        dto.setName(userDTO.getName());
        dto.setRole(saveUser.getRole());
        dto.setUserName(saveUser.getUserName());

        return dto;
    }

    public void deleteUser(Integer id) {

        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id Not Found"));

        userDAO.delete(user);
    }

}
