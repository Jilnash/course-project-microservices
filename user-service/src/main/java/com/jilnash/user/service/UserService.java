package com.jilnash.user.service;

import com.jilnash.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(String login, String email);

    User getUser(Long id);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    User saveUser(User user);
}
