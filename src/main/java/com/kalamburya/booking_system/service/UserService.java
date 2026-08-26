package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.User;
import com.kalamburya.booking_system.exception.UserNotFoundException;
import com.kalamburya.booking_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("User with email" + user.getEmail() + "already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User updateUser(Long id, User newUser) {
        User userToUpdate = getUserById(id);

        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());

        return repository.save(userToUpdate);
    }

    public void deleteUser(Long id) {
        User userToDelete = getUserById(id);
        repository.delete(userToDelete);
    }


}
