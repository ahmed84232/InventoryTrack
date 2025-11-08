package com.yasser.InventoryTrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer quantity;
    private Double price; // optional, copy from product at order time

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private ProductOrder productOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

}
