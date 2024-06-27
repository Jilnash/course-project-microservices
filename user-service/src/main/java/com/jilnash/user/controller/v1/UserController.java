package com.jilnash.user.controller.v1;

import com.jilnash.user.dto.UserDTO;
import com.jilnash.user.mapper.UserMapper;
import com.jilnash.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
        );
    }

    @PutMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(
                userService.saveUser(userMapper.toEntity(userDTO))
        );
    }

    @PostMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(
                userService.saveUser(userMapper.toEntity(userDTO))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }
}
