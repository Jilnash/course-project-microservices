package com.jilnash.user.mapper;

import com.jilnash.user.dto.UserDTO;
import com.jilnash.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getLogin());
        userDTO.setEmail(user.getEmail());
//        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
