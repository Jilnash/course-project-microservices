package com.jilnash.user.service;

import com.jilnash.user.model.User;
import com.jilnash.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUser(Long id) {
        return userRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    @Override
    public List<User> getUsers(String login, String email) {
        return userRepo.findAllByEmailStartingWithAndLoginStartingWith(email, login);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Boolean existsByLogin(String login) {
        return userRepo.existsUserByLogin(login);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepo.existsUserByEmail(email);
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return userRepo.existsUserByPhone(phone);
    }
}
