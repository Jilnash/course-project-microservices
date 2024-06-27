package com.jilnash.user.mapper;

import com.jilnash.user.dto.RegistrationDTO;
import com.jilnash.user.dto.UserDTO;
import com.jilnash.user.model.User;
import com.jilnash.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserDTO toDTO(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public User toEntity(RegistrationDTO registrationDTO) {

        User user = new User();

        user.setLogin(registrationDTO.getLogin());
        user.setEmail(registrationDTO.getEmail());
        user.setPhone(registrationDTO.getPhone());
        user.setPassword(registrationDTO.getPassword());

        return user;
    }

    public User toEntity(UserDTO userDTO) {

        if (userDTO.getId() == null) {
            User user = new User();

            user.setLogin(userDTO.getLogin());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());

            return user;
        }

        User user = userService.getUser(userDTO.getId());

        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        return user;
    }
}
