package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User saveUser(User user);
    User createUser(User user, List<Integer> roleIds);
    User updateUser(Integer id, User updatedUser, List<Integer> roleIds);
    Optional<User> findByEmail(String email);
    List<User> findAllUsers();
    Optional<User> findUserById(Integer id);
    void deleteUser(Integer id);
}
