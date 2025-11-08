//package com.yasser.InventoryTrack.service;
//
//
//import com.yasser.InventoryTrack.dao.OrderDao;
//import com.yasser.InventoryTrack.dao.OrderItemDao;
//import com.yasser.InventoryTrack.dao.ProductDao;
//import com.yasser.InventoryTrack.dto.OrderDto;
//import com.yasser.InventoryTrack.dto.ProductsDto;
//import com.yasser.InventoryTrack.dto.OrderItemDto;
//import com.yasser.InventoryTrack.entity.Order;
//import com.yasser.InventoryTrack.entity.OrderItem;
//import com.yasser.InventoryTrack.entity.Product;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ServiceRepository {
//
//    private final OrderDao orderDAO;
//    private final ProductDao productDAO;
//    private final UserDAO userDAO;
//    private final OrderItemDao orderItemDAO;
//
//    public ServiceRepository(OrderDao orderDAO, ProductDao productDAO, UserDAO userDAO, OrderItemDao orderItemDAO) {
//        this.orderDAO = orderDAO;
//        this.productDAO = productDAO;
//        this.userDAO = userDAO;
//        this.orderItemDAO = orderItemDAO;
//    }
//
//    // ORDER METHODS
//    public List<OrderResponseDto> getOrders() {
//
//        List<Order> orders = orderDAO.findAll();
//        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
//
//        for (Order order : orders) {
//            OrderResponseDto dto = new OrderResponseDto();
//            dto.setOrderId(order.getId());
//            dto.setUserId(order.getUser().getId());
//
//            dto.setProductNames(order.getOrderItems()
//                    .stream()
//                    .collect(Collectors.toMap(
//                            OrderItem::getItemName,
//                            OrderItem::getQuantity
//                    )));
//
//            dto.setTotalPrice(order.getTotalPrice());
//            orderResponseDtos.add(dto);
//
//        }
//        return orderResponseDtos;
//    }
//
//    // ORDER METHODS
//    public OrderResponseDto getOrderById(Integer id) {
//
//        Order order = orderDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        OrderResponseDto dto = new OrderResponseDto();
//
//        dto.setUserId(order.getUser().getId());
//        dto.setUser(order.getUser().getName());
//
//        dto.setProductNames(order.getOrderItems()
//                .stream()
//                .collect(Collectors.toMap(
//                        OrderItem::getItemName,
//                        OrderItem::getQuantity
//                )));
//
//        dto.setTotalPrice(order.getTotalPrice());
//
//        return dto;
//
//    }
//
//
//    // ORDER METHODS
//    public OrderResponseDto addOrder(OrderDto orderDTO) {
//
//        // Creating User
//        User user = userDAO.findById(orderDTO.getUserId())
//                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        // Creating OrderedItems
//        List<OrderItemDto> orderedProducts = orderDTO.getOrderItems();
//        List<OrderItem> orderedItems = new ArrayList<>();
//
//        for (OrderItemDto orderitemDTO : orderedProducts) {
//
//            OrderItem orderItem = new OrderItem();
//
//            orderItem.setQuantity(orderitemDTO.getQuantity());
//
//            Product product = productDAO.findById(orderitemDTO.getProductId())
//                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
//
//            orderItem.setProduct(product);
//            orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
//            orderItem.setItemName(product.getName());
//
//
//            product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
//            productDAO.save(product);
//
//
//            orderedItems.add(orderItem);
//
//        }
//
//        // Creating Order
//        Order order = new Order();
//        order.setUser(user);
//        order.setOrderItems(orderedItems);
//        Double totalPrice = orderedItems.stream().mapToDouble(OrderItem::getPrice).sum();
//        order.setTotalPrice(totalPrice);
//        Order savedOrder = orderDAO.save(order);
//
//        for (OrderItem item : orderedItems) {
//            item.setOrder(order);
//            orderItemDAO.save(item);
//        }
//
//        // Creating response
//        OrderResponseDto response = new OrderResponseDto();
//        response.setOrderId(savedOrder.getId());
//        response.setUserId(user.getId());
//        response.setUser(user.getName());
//
//        response.setProductNames(savedOrder.getOrderItems()
//                .stream()
//                .collect(Collectors.toMap(
//                OrderItem::getItemName,
//                OrderItem::getQuantity
//        )));
//
//        response.setTotalPrice(totalPrice);
//
//        return response;
//
//    }
//
//    // ORDER METHODS
//    public void deleteOrder(Integer id) {
//        Order order = orderDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order id Not Found"));
//
//        orderDAO.delete(order);
//    }
//
//
//
//
//
//
//    // PRODUCTS METHODS
//    public List<ProductsDto> getProducts() {
//
//        List<Product> products = productDAO.findAll();
//        List<ProductsDto> productsDtos = new ArrayList<>();
//
//        for (Product productDetails : products) {
//            ProductsDto dto = new ProductsDto();
//
//            dto.setId(productDetails.getId());
//            dto.setName(productDetails.getName());
//            dto.setPrice(productDetails.getPrice());
//            dto.setDescription(productDetails.getDescription());
//            dto.setQuantity(productDetails.getStockQuantity());
//            productsDtos.add(dto);
//        }
//        return productsDtos;
//    }
//
//    // PRODUCTS METHODS
//    public ProductsDto getProductById(Integer id) {
//
//        Product product = productDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));
//
//        ProductsDto dto = new ProductsDto();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setPrice(product.getPrice());
//        dto.setDescription(product.getDescription());
//        dto.setQuantity(product.getStockQuantity());
//
//        return dto;
//
//    }
//
//    // PRODUCTS METHODS
//    public ProductsDto addProduct(ProductsDto productsDTO) {
//
//        Product theProduct = new Product();
//
//        theProduct.setName(productsDTO.getName());
//        theProduct.setDescription(productsDTO.getDescription());
//        theProduct.setPrice(productsDTO.getPrice());
//        theProduct.setStockQuantity(productsDTO.getQuantity());
//
//        Product savedProduct = productDAO.save(theProduct);
//
//        ProductsDto dto = new ProductsDto();
//
//        dto.setName(savedProduct.getName());
//        dto.setId(savedProduct.getId());
//        dto.setDescription(savedProduct.getDescription());
//        dto.setPrice(savedProduct.getPrice());
//        dto.setQuantity(savedProduct.getStockQuantity());
//
//
//        return dto;
//    }
//
//    // PRODUCTS METHODS
//    public void deleteProduct(Integer id) {
//
//        Product product = productDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id Not Found"));
//
//        productDAO.delete(product);
//
//    }
//
//
//
//
//
//    // USER METHODS
//    public List<UserDTO> getUsers() {
//
//        List<User> users = userDAO.findAll();
//        List<UserDTO> usersDTOS = new ArrayList<>();
//        for (User user : users) {
//
//            UserDTO dto = new UserDTO();
//            dto.setId(user.getId());
//            dto.setName(user.getName());
//            dto.setRole(user.getRole());
//            dto.setUserName(user.getUserName());
//            usersDTOS.add(dto);
//        }
//        return usersDTOS;
//    }
//
//    // USER METHODS
//    public UserDTO getUserById(Integer id) {
//
//        User user = userDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id Not Found"));
//
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setName(user.getName());
//        dto.setRole(user.getRole());
//        dto.setUserName(user.getUserName());
//        return dto;
//    }
//
//    // USER METHODS
//    public UserDTO addUser(UserDTO userDTO) {
//
//        User user = new User();
//
//        user.setName(userDTO.getName());
//        user.setRole(userDTO.getRole());
//        user.setUserName(userDTO.getUserName());
//        user.setPassword(userDTO.getPassword());
//
//        User saveUser = userDAO.save(user);
//
//        UserDTO dto = new UserDTO();
//
//        dto.setId(saveUser.getId());
//        dto.setName(userDTO.getName());
//        dto.setRole(saveUser.getRole());
//        dto.setUserName(saveUser.getUserName());
//
//        return dto;
//    }
//
//    public void deleteUser(Integer id) {
//
//        User user = userDAO.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id Not Found"));
//
//        userDAO.delete(user);
//    }
//
//}
