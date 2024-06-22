package com.jilnash.userservice.service;

import com.jilnash.userservice.model.User;
import com.jilnash.userservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    @Override
    public User getUser(String login) {
        return userRepo.findByLogin(login).orElseThrow();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow();
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepo.findByPhone(phone).orElseThrow();
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }
}
