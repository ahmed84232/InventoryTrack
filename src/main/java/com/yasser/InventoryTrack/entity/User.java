package com.yasser.InventoryTrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yasser.InventoryTrack.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @NotBlank(message = "Name is required.")
    private String name;

    @Column(name = "user_name", unique = true)
    @NotBlank(message = "You must Provide Username.")
    private String userName;

    @Column(name = "password")
    @NotBlank(message = "You must Provide Password.")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Order> order;

}
