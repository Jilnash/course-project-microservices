package com.jilnash.user.service;

import com.jilnash.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUser(Long id);

    User getUser(String login);

    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    User saveUser(User user);
}
