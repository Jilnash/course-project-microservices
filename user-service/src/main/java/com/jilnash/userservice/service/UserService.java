package com.jilnash.userservice.service;

import com.jilnash.userservice.model.User;

public interface UserService {

    User getUser(Long id);

    User getUser(String login);

    User getUserByEmail(String email);

    User getUserByPhone(String phone);
}
