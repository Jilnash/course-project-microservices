package com.jilnash.user.controller.v1;

import com.jilnash.user.dto.AppResponse;
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
    public ResponseEntity<?> getUsers(@RequestParam(required = false, defaultValue = "") String login,
                                      @RequestParam(required = false, defaultValue = "") String email) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Users found successfully",
                        userService.getUsers(login, email)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationDTO registrationDTO) {

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    new AppResponse(
                            400,
                            "Passwords do not match",
                            null
                    )
            );
        }

        StringBuilder fields = new StringBuilder();

        if (userService.existsByLogin(registrationDTO.getLogin()))
            fields.append("login ");

        if (userService.existsByEmail(registrationDTO.getEmail()) != null)
            fields.append("email ");

        if (userService.existsByPhone(registrationDTO.getPhone()) != null)
            fields.append("phone ");

        if (!fields.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new AppResponse(
                            400,
                            "User with this " + fields + "already exists",
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Success",
                        userService.saveUser(userMapper.toEntity(registrationDTO))
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "User updated successfully",
                        userService.saveUser(userMapper.toEntity(userDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }
}
