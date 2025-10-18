package com.yasser.InventoryTrack.controller;

import com.yasser.InventoryTrack.dao.UserDAO;
import com.yasser.InventoryTrack.dto.UserDTO;
import com.yasser.InventoryTrack.entity.User;
import com.yasser.InventoryTrack.service.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ServiceRepository serviceRepository;

    public UserController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler
    public UserDTO getUser(@PathVariable int id) throws ResponseStatusException {
        return serviceRepository.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        return serviceRepository.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@RequestBody @Valid UserDTO userDTO) {

        return serviceRepository.addUser(userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) throws ResponseStatusException {
        serviceRepository.deleteUser(id);
    }
}
