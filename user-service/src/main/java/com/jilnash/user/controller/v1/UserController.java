package com.jilnash.user.controller.v1;

import com.jilnash.user.dto.RegistrationDTO;
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
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String login,
                                      @RequestParam(required = false) String email) {

        if (login != null)
            return ResponseEntity.ok(
                    userService.getUser(login)
            );

        if (email != null)
            return ResponseEntity.ok(
                    userService.getUser(email)
            );

        return ResponseEntity.ok(
                userService.getUsers()
        );
    }

    @PutMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationDTO registrationDTO) {

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        if (userService.getUser(registrationDTO.getLogin()) != null) {
            return ResponseEntity.badRequest().body("User with this login already exists");
        }

        if (userService.getUserByEmail(registrationDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        if (userService.getUserByPhone(registrationDTO.getPhone()) != null) {
            return ResponseEntity.badRequest().body("User with this phone already exists");
        }

        return ResponseEntity.ok(
                userService.saveUser(userMapper.toEntity(registrationDTO))
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
