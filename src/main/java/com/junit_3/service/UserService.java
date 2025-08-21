package com.junit_3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.junit_3.entity.User;
import com.junit_3.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) { // constructor-based DI
        this.repository = repository;
    }

    public String getUserName(int id) {
        return repository.findById(id).map(User::getName).orElse("Unknown User");
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public Optional<User> updateUser(int id, User user) {
        return repository.findById(id).map(existing -> repository.save(new User(id, user.getName())));
    }

    public boolean deleteUser(int id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<User> findAllUsers() {
        return repository.findAll();
    }
    
}
