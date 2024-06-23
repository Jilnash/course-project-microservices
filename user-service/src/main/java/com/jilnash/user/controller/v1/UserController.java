package com.jilnash.user.controller.v1;

import com.jilnash.user.model.User;
import com.jilnash.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
        );
    }

    @PutMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return ResponseEntity.ok(
                userService.saveUser(user)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }
}
